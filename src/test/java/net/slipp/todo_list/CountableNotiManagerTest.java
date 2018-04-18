package net.slipp.todo_list;

import static org.junit.Assert.*;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CountableNotiManagerTest.MockBeanConfig.class, AppConfig.class})
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

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void Todo를_생성할때마다_count가_증가되는지_확인() {
    String TITLE = "TITLE";
    String CONTENT = "CONTENT";

    Todo todo = new Todo();
    todo.setTitle(TITLE);
    todo.setContent(CONTENT);

    int previousNotiCount = countableNotiManager.getCallCount();
    todoManager.create(todo);
    int afterNotiCount = countableNotiManager.getCallCount();

    assertEquals(afterNotiCount, previousNotiCount + 1);
  }

  private static class MockNotiManager extends CountableNotiManager {

    private static String DEFAULT_PASSED_TITLE = null;
    private static Exception exception;
    private static String passedTitle = DEFAULT_PASSED_TITLE;

    @Override
    public void notify(String title) throws RuntimeException {

      if (exception != null && exception.getClass() == RuntimeException.class) {
        throw (RuntimeException) exception;
      }

      passedTitle = title;
    }
  }


}