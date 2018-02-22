package net.slipp.todo_list;

import net.slipp.exception.RepositoryFailedException;
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

    @Before
    public void setUp() throws Exception {
        MockTodoRepository.passedTodo = null;
        MockTodoRepository.exception = null;
    }

    @After
    public void tearDown() throws Exception {

    }


    private static final String NULL = null;

    @Autowired
    private TodoManager todoManager;

    @Autowired
    private NotiManager notiManager;

    @Configurable
    public static class MockBeanConfig {
        @Bean
        @Primary
        public TodoRepository todoRepository() {
            return new MockTodoRepository();
        }

        @Bean
        @Primary
        public NotiManager notiManager() {
            return new MockNotifyRepository();
        }
    }


    @Test
    public void 전달받은_Todo를_TodoRepository_store에_전달하는_지_확인 () {

        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

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
    public void id를_minus1로_설정하는_지_확인 () {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setId(10); // <---------------------------
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        todoManager.create(todo);

        Todo actual = MockTodoRepository.passedTodo;

        assertNotNull(actual);
        assertEquals(-1, actual.getId()); // <-----------------
        assertEquals(TITLE, actual.getTitle());
        assertEquals(CONTENT, actual.getContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void Todo가_null일때_IAE던지는_지_확인 () {
        Todo todo = null;
        todoManager.create(todo);
    }

    @Test(expected=IllegalArgumentException.class)
    public void title이_null일때_IAE던지는_지_확인 () {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        TITLE = null; // <------------------------
        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        todoManager.create(todo);
    }

    @Test(expected=IllegalArgumentException.class)
    public void title_길이가_50_이상이면_IAE던지는_지_확인 () {
        String TITLE = "0123456789012345678901234567890123456789012345678901234";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        assertTrue(todo.getTitle().length() >= 50);
        todoManager.create(todo);
    }

    @Test(expected=IllegalArgumentException.class)
    public void content_길이가_500_이상이면_IAE던지는_지_확인 () {
        String TITLE = "TITLE";
        String CONTENT = "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다." +
                "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다." +
                "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다." +
                "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다." +
                "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다." +
                "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다." +
                "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다." +
                "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다." +
                "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다." +
                "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다." +
                "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다." +
                "오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.오백자가넘는내용입니다.";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        assertTrue(todo.getContent().length() >= 500);
        todoManager.create(todo);
    }

    @Test
    public void content가_null일때_빈문자열로_변경하는_지_확인() {
        String TITLE = "TITLE";
        String CONTENT = null;

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        todoManager.create(todo);

        Todo actual = MockTodoRepository.passedTodo;

        assertNotNull(actual);
        assertEquals(TITLE, actual.getTitle());
        assertEquals("", actual.getContent());
    }

    @Test
    public void 파라미터가_비정상일_때_TodoRepository_store_호출안하는_지_확인() {
        String TITLE = null;
        String CONTENT = null;

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        try {
            todoManager.create(todo);
        } catch (IllegalArgumentException e) {
            // ignore
        }

        Todo actual = MockTodoRepository.passedTodo;

        assertNull(actual);
    }

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
    public void TodoRepository_store_호출_시에_RepositoryFailedException을_던지면_RuntimeException_던지는_지_확인 () {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);


        MockTodoRepository.exception = new RepositoryFailedException();

        todoManager.create(todo);
    }

    @Test(expected = RuntimeException.class)
    public void TodoRepository_store_호출_시에_RuntimeException을_던지면_그대로_RuntimeException_던지는_지_확인 () {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);


        MockTodoRepository.exception = new RuntimeException();

        todoManager.create(todo);
    }

    @Test
    public void NotiManager_notify_TodoRepository_store_호출_성공_후_nofify_호출하는지_확인() {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        todoManager.create(todo);
        final String passedTitle = MockNotifyRepository.passedTitle;
        notiManager.notify(passedTitle);

        assertEquals(TITLE, passedTitle);
    }

    @Test(expected = RuntimeException.class)
    public void NotiManager_호출시에_TITLE이_NULL이면_RuntimeException을_던지는지_확인() {
        MockNotifyRepository.exception = new RuntimeException();
        notiManager.notify(null);
    }

    private static class MockNotifyRepository extends NotiManager {

        private static Exception exception;
        private static String passedTitle;

        @Override
        public void notify(final String title) throws RuntimeException {
            passedTitle = title;
            if (exception != null && exception.getClass() == RuntimeException.class) {
                throw (RuntimeException) exception;
            }
        }
    }

    private static class MockTodoRepository extends TodoRepository {

        private static Todo passedTodo;
        private static Exception exception;

        @Override
        public Todo store(Todo todo) throws IllegalArgumentException, RepositoryFailedException {
            passedTodo = todo;

            if(exception!=null && exception.getClass()==IllegalArgumentException.class) { throw (IllegalArgumentException)exception; }
            if(exception!=null && exception.getClass()==RepositoryFailedException.class) { throw (RepositoryFailedException)exception; }
            if(exception!=null && exception.getClass()==RuntimeException.class) { throw (RuntimeException)exception; }

            return todo;
        }

    }

}