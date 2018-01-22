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

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

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


    @Test
    public void 정상똥작_확인 () {

        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        Todo actual = todoManager.create(todo);

        assertNotNull(actual);

        assertEquals(MockTodoRepository.getLastCreatedId(), actual.getId());
        assertEquals(TITLE, actual.getTitle());
        assertEquals(CONTENT, actual.getContent());


    }



    @Test
    public void title이_null일경우_빈문자열로_대치되는_지_확인 () {

        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(NULL); // <<<<<<<<<<<<<<<<<<<<<<<<<<<
        todo.setContent(CONTENT);

        Todo actual = todoManager.create(todo);

        assertEquals(TodoManager.DEFAULT_TITLE, actual.getTitle()); // <<<<<<<<<<<<<<<<<<<<<<<<<

    }



    private static class MockTodoRepository extends TodoRepository {

        public static final int ID = 10;
        @Override
        public Todo save(Todo todo) {
            todo.setId(ID);
            return todo;
        }

        public static int getLastCreatedId() {
            return ID;
        }
    }


}