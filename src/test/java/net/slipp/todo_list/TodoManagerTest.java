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

import java.util.stream.IntStream;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, TodoManagerTest.MockBeanConfig.class } )
public class TodoManagerTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        MockTodoRepository.passedTodo = null;
        MockTodoRepository.exception = null;
    }


    private static final String NULL = null;

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


    /**
     * 정상 동작
     */
    @Test
    public void 전달받은_Todo를_TodoRepository_store에_전달하는_지_확인 () throws Exception {

        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = getTestTodo(TITLE, CONTENT);
        todoManager.create(todo);

        Todo actual = MockTodoRepository.passedTodo;

        assertNotNull(actual);
        assertEquals(TITLE, actual.getTitle());
        assertEquals(CONTENT, actual.getContent());
    }

    @Test
    public void id를_마이너스1로_설정하는_지_확인() throws Exception {
        Todo todo = getTestTodo();
        todoManager.create(todo);
        final Todo actual = MockTodoRepository.passedTodo;

        assertNotNull(actual);
        assertEquals(-1, actual.getId());
    }


    /**
     * 파라미터 확인
     */
    @Test(expected = IllegalArgumentException.class)
    public void Todo가_null일때_IAE던지는_지_확인() throws Exception {
        todoManager.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void title이_null일때_IAE던지는_지_확인() throws Exception {
        final Todo todo = getTestTodo();
        todo.setTitle(null);
        todoManager.create(todo);
    }

    @Test
    public void title_길이가_50_이상이면_IAE던지는_지_확인() throws Exception {
        final Todo todo = getTestTodo();
        todo.setTitle(makeLongString(50));
        try {
            todoManager.create(todo);
            fail();
        } catch (IllegalArgumentException iae) {}

        todo.setTitle(makeLongString(100));
        try {
            todoManager.create(todo);
            fail();
        } catch (IllegalArgumentException iae) {}

        todo.setTitle(makeLongString(35));
        try {
            todoManager.create(todo);
        } catch (IllegalArgumentException iae) {
            fail();
        }

        todo.setTitle(makeLongString(0));
        try {
            todoManager.create(todo);
        } catch (IllegalArgumentException iae) {
            fail();
        }
    }

    @Test
    public void content_길이가_500_이상이면_IAE던지는_지_확인() throws Exception {
        final Todo todo = getTestTodo();
        todo.setContent(makeLongString(500));
        try {
            todoManager.create(todo);
            fail();
        } catch (IllegalArgumentException iae) {}

        todo.setContent(makeLongString(550));
        try {
            todoManager.create(todo);
            fail();
        } catch (IllegalArgumentException iae) {}

        todo.setContent(makeLongString(499));
        try {
            todoManager.create(todo);
        } catch (IllegalArgumentException iae) {
            fail();
        }

        todo.setContent(makeLongString(0));
        try {
            todoManager.create(todo);
        } catch (IllegalArgumentException iae) {
            fail();
        }
    }

    @Test
    public void content가_null일때_빈문자열로_변경하는_지_확인() throws Exception {
        final Todo todo = getTestTodo("TEST TITLE", null);
        todoManager.create(todo);
        final Todo actual = MockTodoRepository.passedTodo;

        assertNotNull(actual);
        assertEquals("", actual.getContent());
    }

    @Test
    public void 파라매터가_비정상일_때_TodoRepository_store_호출안하는_지_확인() {
        final Todo todo = getTestTodo(null, null);
        try {
            todoManager.create(todo);
        } catch (Exception e) {}

        final Todo actual = MockTodoRepository.passedTodo;
        assertNull(actual);
    }

    /**
     * Repository 예외 처리
     */
    @Test(expected=IllegalArgumentException.class)
    public void TodoRepository_store_호출_시에_IAE를_던지면_그대로_IAE_던지는_지_확인 () throws Exception {
        Todo todo = getTestTodo();
        MockTodoRepository.exception = new IllegalArgumentException();

        todoManager.create(todo);
    }

    @Test(expected = RuntimeException.class)
    public void TodoRepository_store_호출_시에_RepositoryFailedException을_던지면_RuntimeException_던지는_지_확인() throws Exception {
        final Todo todo = getTestTodo();
        MockTodoRepository.exception = new RepositoryFailedException();

        todoManager.create(todo);
    }

    @Test(expected = RuntimeException.class)
    public void TodoRepository_store_호출_시에_RuntimeException을_던지면_그대로_RuntimeException_던지는_지_확인() throws Exception {
        final Todo todo = getTestTodo();
        MockTodoRepository.exception = new RuntimeException();

        todoManager.create(todo);
    }

    private Todo getTestTodo(String TITLE, String CONTENT) {
        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        return todo;
    }

    private Todo getTestTodo() {
        return getTestTodo("TITLE", "CONTENT");
    }

    private String makeLongString(int length) {
        StringBuilder longStringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            longStringBuilder.append("a");
        }
        assertTrue(longStringBuilder.length() >= length);
        return longStringBuilder.toString();
    }

    private static class MockTodoRepository extends TodoRepository {

        private static Todo passedTodo;
        private static Exception exception;

        @Override
        public Todo store(Todo todo) throws Exception {
            // if(exception!=null && exception.getClass()==IllegalArgumentException.class) { throw (IllegalArgumentException)exception; }
            if (exception != null) {
                throw exception;
            }
            passedTodo = todo;
            return todo;
        }
    }


}