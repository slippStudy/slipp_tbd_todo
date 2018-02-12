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
    		MockTodoRepository.exception = null;
    		MockTodoRepository.passedTodo = null;
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

    @Test(expected=IllegalArgumentException.class)
    public void Todo가_null일때_IAE던지는_지_확인 () {
    		todoManager.create(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void title이_null일때_IAE던지는_지_확인 () {
    		Todo todo = Todo.create(null, "CONTENT");
    		todoManager.create(todo);
    }

    @Test(expected=IllegalArgumentException.class)
    public void title_길이가_50_이상이면_IAE던지는_지_확인 () {
    		String title = "";
    		while (title.length() < 50) {
    			title += "t";
    		}
    		Todo todo = Todo.create(title, "CONTENT");
    		todoManager.create(todo);
    }

    @Test(expected=IllegalArgumentException.class)
    public void content_길이가_500_이상이면_IAE던지는_지_확인() {
    		String content = "";
		while (content.length() < 500) {
			content += "t";
		}
		Todo todo = Todo.create("TITLE", content);
		todoManager.create(todo);
    }

    @Test
    public void content가_null일때_빈문자열로_변경하는_지_확인() {
    		String VALID = "";
    		Todo todo = Todo.create("TITLE", null);
    		todoManager.create(todo);
    		assertEquals(VALID, todo.getContent());
    }

    @Test
    public void 파라매터가_비정상일_때_TodoRepository_store_호출안하는_지_확인() {
    		Todo todo = Todo.create(null, null);
    		try {
    			todoManager.create(todo);
    		} catch (RuntimeException e) {
    			//pass
    		}
    		
    		assertEquals(null, MockTodoRepository.passedTodo);
    }

    @Test(expected=RuntimeException.class)
    public void TodoRepository_store_호출_시에_RepositoryFailedException을_던지면_RuntimeException_던지는_지_확인(){
    		MockTodoRepository.exception = new RepositoryFailedException();
		todoManager.create(Todo.create("TITLE", "CONTENT"));
    }

    @Test(expected=RuntimeException.class)
    public void TodoRepository_store_호출_시에_RuntimeException을_던지면_그대로_RuntimeException_던지는_지_확인() {
    		MockTodoRepository.exception = new RuntimeException();
    		todoManager.create(Todo.create("TITLE", "CONTENT"));
    }

    @Test
    public void TodoRepository_store의_반환값이_null이_아닌지_확인() {
    		todoManager.create(Todo.create("TITLE", "CONTENT"));
    		assertNotNull(MockTodoRepository.passedTodo);
    }

    @Test
    public void TodoRepository_store의_반환값_Todo의_id가_0보다_크고_Int_MAX_보다_같거나_작은지_확인() {
    		todoManager.create(Todo.create("TITLE", "CONTENT"));
    		assertTrue(0 < MockTodoRepository.passedTodo.getId() && Integer.MAX_VALUE >= MockTodoRepository.passedTodo.getId());    		
    }

    @Test
    public void TodoRepository_store의_반환값_Todo의_title과_content가_원래값과_동일한지_확인() {
    		String title = "TITLE";
    		String content = "CONTENT";
    		Todo created = todoManager.create(Todo.create(title, content));
    		assertTrue(created.getTitle().equals(title) && created.getContent().equals(content));
    }

    @Test
    public void id를_마이너스1로_설정하는_지_확인 () {
    		int VALID = -1;
    		Todo todo = Todo.create("TITLE", "CONTENT");
    		try {
			todoManager.create(todo);
		} catch (Exception e) {
			fail();
		}
    		if (MockTodoRepository.passedTodo == null) {
    			assertEquals(VALID, todo.getId());
    		}
    }

    @Test
    public void TodoRepository_store_호출_시에_IAE를_던지면_그대로_IAE_던지는_지_확인 () {

        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);


        MockTodoRepository.exception = new IllegalArgumentException();

        try {
			todoManager.create(todo);
		} catch (Exception e) {
			assertEquals(IllegalArgumentException.class, e.getClass());
		}

    }
    
    private static class MockTodoRepository extends TodoRepository {

    		private static int ID_SEQ = 0;
        private static Todo passedTodo;
        private static Exception exception;

        @Override
        public Todo store(Todo todo) throws RuntimeException {
            if(exception!=null && exception.getClass()==IllegalArgumentException.class) { throw (IllegalArgumentException)exception; }
            if(exception!=null && exception.getClass()==RepositoryFailedException.class) { throw (RuntimeException)exception;}
            if(exception!=null && exception.getClass()==RuntimeException.class) {
            		RuntimeException e = (RuntimeException) exception;
            		throw (RuntimeException) exception;
            	}
            passedTodo = todo.newClone();
            passedTodo.setId(ID_SEQ++);
            return passedTodo;
        }

    }


}