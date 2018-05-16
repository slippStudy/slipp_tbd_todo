package net.slipp.todo_list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
@ContextConfiguration(classes = {AppConfig.class, TodoManagerDeleteTest.MockBeanConfig.class})
public class TodoManagerDeleteTest {

  @Before
  public void setUp() throws Exception {
    MockTodoRepository.passedTodo = null;
    MockTodoRepository.notExistedTodo = null;
    MockTodoRepository.passedShouldDelete = false;
    MockTodoRepository.exception = null;
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
      return new TodoManagerDeleteTest.MockTodoRepository();
    }
  }

  @Test
  public void todo_delete가_정상적으로_삭제되는지_확인() {
    String TITLE = "TITLE";
    String CONTENT = "CONTENT";

    Todo todo = new Todo();
    todo.setTitle(TITLE);
    todo.setContent(CONTENT);
    todo.setId(100);

    todoManager.delete(todo);
    assertNull(MockTodoRepository.passedTodo);
  }

  @Test
  public void Todo삭제요청하면_should_delete가_true인_store를_호출하는지_확인() {

    String TITLE = "TITLE";
    String CONTENT = "CONTENT";

    Todo todo = new Todo();
    todo.setTitle(TITLE);
    todo.setContent(CONTENT);
    todo.setId(100);

    todoManager.delete(todo);
    assertEquals(MockTodoRepository.passedShouldDelete, true);
  }

  @Test(expected = RuntimeException.class)
  public void 삭제하려는_todo가_존재하지_않을때_RuntimeException을_던짐() {
    todoManager.delete(MockTodoRepository.notExistedTodo);
  }

  @Test(expected = RuntimeException.class)
  public void null을_todo삭제요청할때_RuntimeException을_던짐() {
    todoManager.delete(null);
  }

  private static class MockTodoRepository extends TodoRepository {

    private static Todo passedTodo;
    private static Todo notExistedTodo;
    private static boolean passedShouldDelete;
    private static Exception exception;

    @Override
    public Todo store(Todo todo, boolean should_delete) throws IllegalArgumentException {

      passedTodo = todo;
      passedShouldDelete = should_delete;

      if (!should_delete) {
        return null;
      }
      if (passedTodo == null || passedTodo.getId() <= 0 || todo.equals(notExistedTodo)) {
        throw new IllegalArgumentException();
      }
      passedTodo = null;
      return null;
    }
  }
}
