package net.slipp.todo_list;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = { AppConfig.class, CountableNotiManagerTest.MockBeanConfig.class } )
public class CountableNotiManagerTest extends TodoListApplicationTests {

    @Configurable
    public static class MockBeanConfig {
        @Bean
        @Primary
        public CountableNotiManager countableNotiManager() {
            return new CountableNotiManager();
        }
    }

    @Autowired
    private TodoManager todoManager;

    @Autowired
    private CountableNotiManager countableNotiManager;

    @Test
    public void Noti_개수_확인() {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        todoManager.create(todo);
        assertEquals(1, countableNotiManager.getCount());
        todoManager.create(todo);
        assertEquals(2, countableNotiManager.getCount());
        todoManager.create(todo);
        assertEquals(3, countableNotiManager.getCount());
        todoManager.create(todo);
        assertEquals(4, countableNotiManager.getCount());
        todoManager.create(todo);
        assertEquals(5, countableNotiManager.getCount());
    }
}
