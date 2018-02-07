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
  public void 전달받은_Todo를_TodoRepository_store에_전달하는_지_확인() {
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
  public void id를_마이너스일로_설정하는_지_확인() {
    int id = -1;
    Todo todo = new Todo();
    todo.setId(id);
    todoManager.create(todo);
    Todo actual = MockTodoRepository.passedTodo;
    assertEquals(-1, actual.getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void Todo가_null일때_IAE던지는_지_확인() {
    todoManager.create(null);
    Todo actual = MockTodoRepository.passedTodo;
  }

  @Test(expected = IllegalArgumentException.class)
  public void title이_null일때_IAE던지는_지_확인() {
    Todo todo = new Todo();
    todo.setTitle(null);
    todoManager.create(todo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void title_길이가_50_이상이면_IAE던지는_지_확인() {
    String title = "TITLETITLETITLETITLETITLETITLETITLETITLETITLETITLE";
    Todo todo = new Todo();
    todo.setTitle(title);
    todoManager.create(todo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void content_길이가_500_이상이면_IAE던지는_지_확인() {
    String content = "contentcontentcontentcontentcontentcontentcontentconten"
        + "ontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentconten"
        + "ontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentconten"
        + "ontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentconten"
        + "ontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentconten"
        + "ontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentconten";
    Todo todo = new Todo();
    todo.setContent(content);
    todoManager.create(todo);
  }

  @Test
  public void content가_null일때_빈문자열로_변경하는_지_확인() {
    Todo todo = new Todo();
    todo.setTitle("TITLE");
    todo.setContent(null);
    todoManager.create(todo);
    Todo actual = MockTodoRepository.passedTodo;
    assertEquals("", actual.getContent());
  }

  // @Test
  // public void 파라매터가_비정상일_때_TodoRepository_store_호출안하는_지_확인() {
  //
  // }

  @Test(expected = IllegalArgumentException.class)
  public void TodoRepository_store_호출_시에_IAE를_던지면_그대로_IAE_던지는_지_확인() {
    String TITLE = "TITLE";
    String CONTENT = "CONTENT";
    Todo todo = new Todo();
    todo.setTitle(TITLE);
    todo.setContent(CONTENT);
    MockTodoRepository.exception = new IllegalArgumentException();
    todoManager.create(todo);
  }

  @Test(expected = RuntimeException.class)
  public void TodoRepository_store_호출_시에_RepositoryFailedException을_던지면_RuntimeException_던지는_지_확인() {
    String TITLE = "TITLE";
    String CONTENT = "CONTENT";
    Todo todo = new Todo();
    todo.setTitle(TITLE);
    todo.setContent(CONTENT);
    MockTodoRepository.exception = new RepositoryFailedException();
    todoManager.create(todo);
  }

  @Test(expected = RuntimeException.class)
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

    @Override
    public Todo store(Todo todo) throws IllegalArgumentException {
      if (exception != null && exception.getClass() == IllegalArgumentException.class) {
        throw (IllegalArgumentException) exception;
      }
      if (exception != null && (exception.getClass() == RepositoryFailedException.class
          || exception.getClass() == RuntimeException.class)) {
        throw (RuntimeException) exception;
      }
      if (todo == null) {
        throw new IllegalArgumentException();
      }
      passedTodo = todo;
      return todo;
    }
  }
}