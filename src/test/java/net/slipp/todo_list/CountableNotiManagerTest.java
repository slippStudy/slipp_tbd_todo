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

import static org.junit.Assert.assertEquals;

/**
 * @author jyp-airmac
 * @date 2018. 4. 4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class } )
public class CountableNotiManagerTest {

    @Before
    public void setUp() {
        CountableNotiManagerTest.MockTodoRepository.passedTodo = null;
        CountableNotiManagerTest.MockTodoRepository.exception = null;
    }

    @After
    public void tearDown() {}

    @Configurable
    public static class MockBeanConfig {
        @Bean
        @Primary
        public TodoRepository todoRepository() {
            return new CountableNotiManagerTest.MockTodoRepository();
        }
    }

    @Autowired
    private TodoManager todoManager;

    @Autowired
    private CountableNotiManager countableNotiManager;

    @Test
    public void NotiManager가_정상_호출되는경우_count가_증가되는지_확인() {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        int beforeCallCount = countableNotiManager.getCallCount();
        todoManager.create(todo);
        int afterCallCount = countableNotiManager.getCallCount();

        assertEquals(beforeCallCount + 1, afterCallCount);
    }

    @Test
    public void NotiManager가_정상_호출되지않는경우_count가_증가하지않는지_확인() {
        String TITLE = "titleSPAMtitle";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        int beforeCallCount = countableNotiManager.getCallCount();
        todoManager.create(todo);
        int afterCallCount = countableNotiManager.getCallCount();

        assertEquals(beforeCallCount, afterCallCount);
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
