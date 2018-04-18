package net.slipp.todo_list;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, CountableNotiManagerTest.MockBeanConfig.class})
public class CountableNotiManagerTest {

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
  public void Noti수가_맞는지_확인() {
    String TITLE = "TITLE";
    String CONTENT = "CONTENT";

    Todo todo = new Todo();
    todo.setTitle(TITLE);
    todo.setContent(CONTENT);

    todoManager.create(todo);
    todoManager.create(todo);
    todoManager.create(todo);
    todoManager.create(todo);
    assertEquals(4, countableNotiManager.getCallCount());
  }
}