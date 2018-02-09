package net.slipp.todo_list;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, TodoManagerTest.MockBeanConfig.class } )
public class TodoManagerTest {
    private static final String TITLE = "TITLE";
    private static final String CONTENT = "CONTENT";
    private static final int MAX_TITLE_LENGTH = 49;
    private static final int MAX_CONTENT_LENGTH = 499;

    @Before
    public void setUp() throws Exception {
        MockTodoRepository.exception = null;
        MockTodoRepository.passedTodo = null;
        MockTodoRepository.id = 0;
    }

    @After
    public void tearDown() throws Exception {
    }


    @Autowired
    private TodoManager todoManager;

    @Configurable
    public static class MockBeanConfig {
        @Bean
        @Primary
        public TodoRepository todoRepository() {
            return new MockTodoRepository();
        }
    }

    // 정상 동작

    @Test
    public void 전달받은_Todo를_TodoRepository_store에_전달하는_지_확인 () {
        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        todoManager.create(todo);

        Todo actual = MockTodoRepository.passedTodo;

        assertNotNull(actual);
        assertEquals(TITLE, actual.getTitle());
        assertEquals(CONTENT, actual.getContent());
    }

    @Test
    public void id에_값이_없을때_마이너스1로_설정하는_지_확인 () {
        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        todoManager.create(todo);

        int actual = MockTodoRepository.id;

        assertEquals(actual, -1);
    }

    @Test
    public void id에_값이_있을때_마이너스1로_설정하는_지_확인 () {
        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);
        todo.setId(5);

        todoManager.create(todo);

        int actual = MockTodoRepository.id;

        assertEquals(actual, -1);
    }

    // 파라메터 확인
    @Test(expected=IllegalArgumentException.class)
    public void Todo가_null일때_IAE던지는_지_확인() {
        assertStoreNotCalled(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void title이_null일때_IAE던지는_지_확인() {
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(null);
        todo.setContent(CONTENT);

        assertStoreNotCalled(todo);
    }

    @Test(expected=IllegalArgumentException.class)
    public void title_길이가_50_이상이면_IAE던지는_지_확인() {
        StringBuilder title = new StringBuilder();
        for(int i=0; i< MAX_TITLE_LENGTH + 1; i++) {
            title.append(i);
        }

        Todo todo = new Todo();
        todo.setTitle(title.toString());
        todo.setContent(CONTENT);

        assertStoreNotCalled(todo);
    }

    @Test(expected=IllegalArgumentException.class)
    public void content_길이가_500_이상이면_IAE던지는_지_확인() {

        StringBuilder content = new StringBuilder();
        for(int i=0; i< MAX_CONTENT_LENGTH + 1; i++) {
            content.append(i);
        }

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(content.toString());

        assertStoreNotCalled(todo);
    }

    private void assertStoreNotCalled(Todo todo) {
        try {
            todoManager.create(todo);
        } catch (Exception e) {
            Todo actual = MockTodoRepository.passedTodo;
            assertNull(actual);

            throw e;
        }
    }

    @Test
    public void content가_null일때_빈문자열로_변경하는_지_확인() {
        String TITLE = "TITLE";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(null);

        todoManager.create(todo);

        Todo actual = MockTodoRepository.passedTodo;

        assertNotNull(actual);
        assertEquals(actual.getContent(), "");
    }

    // Repository 예외 처리
    @Test(expected=IllegalArgumentException.class)
    public void TodoRepository_store_호출_시에_IAE를_던지면_그대로_IAE_던지는_지_확인 () {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        MockTodoRepository.exception = new IllegalArgumentException();

        todoManager.create(todo);
    }

    @Test(expected=RuntimeException.class)
    public void TodoRepository_store_호출_시에_RepositoryFailedException을_던지면_RuntimeException_던지는_지_확인() {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        MockTodoRepository.exception = new RepositoryFailedException();

        todoManager.create(todo);
    }

    @Test(expected=RuntimeException.class)
    public void TodoRepository_store_호출_시에_RuntimeException을_던지면_그대로_RuntimeException_던지는_지_확인() {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        MockTodoRepository.exception = new RuntimeException();

        todoManager.create(todo);
    }

    private static class MockTodoRepository extends TodoRepository {

        private static Todo passedTodo;
        private static Exception exception;
        private static int id;

        @Override
        public Todo store(Todo todo) throws IllegalArgumentException, RepositoryFailedException {
            if(exception!=null && exception.getClass()==IllegalArgumentException.class) { throw (IllegalArgumentException)exception; }
            if(exception!=null && exception.getClass()==RepositoryFailedException.class) { throw (RepositoryFailedException)exception; }
            if(exception!=null && exception.getClass()==RuntimeException.class) { throw (RuntimeException)exception; }

            passedTodo = todo;
            id = todo.getId();

            return todo;
        }

    }


}