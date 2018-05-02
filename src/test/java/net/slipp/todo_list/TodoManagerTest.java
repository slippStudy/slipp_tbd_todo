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
        
        MockNotiManager.passedTitle = null;
        MockNotiManager.exception = null;
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
            return new MockNotiManager();
        }
    }


    @Test
    public void 전달받은_Todo를_TodoManager_create가_정상_동작하는지_확인 () {

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
    public void TodoManager_Create_정상_수행시_notify호출시_RuntimeException을_무시하는지_확인(){

        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        MockNotiManager.exception = new RuntimeException();     // <------------------- NotiManager에 Exception을 던짐

        todoManager.create(todo);

        Todo actual = MockTodoRepository.passedTodo;

        assertNotNull(actual);
        assertEquals(TITLE, actual.getTitle());
        assertEquals(CONTENT, actual.getContent());

    }

    @Test
    public void TodoManager_Create_정상_수행_후_notify호출이_정상적으로_호출되는지_확인(){

        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        todoManager.create(todo);

        Todo actual = MockTodoRepository.passedTodo;
        String resTitle = MockNotiManager.passedTitle;

        assertNotNull(actual);
        assertEquals(TITLE, actual.getTitle());
        assertEquals(CONTENT, actual.getContent());

        assertEquals(todo.getTitle(), resTitle);    // <---------------------------------

    }

    @Test
    public void TodoManager_Create_에서_Exception발생시_NotiManager_notify가_실행안되는지_확인(){

        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        MockTodoRepository.exception = new RuntimeException();

        try {
            todoManager.create(todo);
        } catch( RuntimeException e ){
            //ignore
        }

        String resTitle = MockNotiManager.passedTitle;
        assertEquals(MockNotiManager.DEFAULT_PASSED_TITLE, resTitle);    // <---------------------------------

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

    @Test
    public void Repository에_저장_실패라면_던져지는_예외에_CAUSE가_안담기는_버그_픽스() {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        MockTodoRepository.exception = new RepositoryFailedException();

        try {
            todoManager.create(todo);
        } catch (RuntimeException e) {
            assertNotNull(e.getCause());
        }

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
    public void SPAM이_들어간_제목인_경우_Todo가_생성되서는_안되는지_확인 () {
    	
    	    String TITLE = "titleSPAMtitle";
    	    String CONTENT = "CONTENT";
    	
    	    Todo todo = new Todo();
    	    todo.setTitle(TITLE);
    	    todo.setContent(CONTENT);
    	
    	    todoManager.create(todo);
    	    
    	    Todo actual = MockTodoRepository.passedTodo;
    	
    	    assertNull(actual);
    }

    @Test
    public void jira에서_호출된_Todo의_경우_noti하지_말아달라_확인 () {
    	
    	    String TITLE = "[JIRA] TITLE";
    	    String CONTENT = "CONTENT";
    	
    	    Todo todo = new Todo();
    	    todo.setTitle(TITLE);
    	    todo.setContent(CONTENT);
    	 
    	    todoManager.create(todo);
    	
    	    String resTitle = MockNotiManager.passedTitle;
    	    assertNull(resTitle);
    }
    
    @Test
    public void Jira에서_호출되더라도_급한건_noti해달라_확인 () {
    	    String TITLE = "[JIRA] TITLE URGENT blah";
    	    String CONTENT = "CONTENT";
    	
    	    Todo todo = new Todo();
    	    todo.setTitle(TITLE);
    	    todo.setContent(CONTENT);
    	
    	    todoManager.create(todo);
    	
    	    String resTitle = MockNotiManager.passedTitle;
    	    assertEquals(todo.getTitle(), resTitle);
    }
    
    @Test(expected = RuntimeException.class)
    public void TodoManager_delete에서_존재하지_않는_todo_삭제_시도하면_IllegalArgumentException_잡아서_RuntimeException으로_던지는지_확인() {

        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);
        todo.setId(100);
        MockTodoRepository.passedTodo = todo;

        Todo deleteTargetTodo = new Todo();
        deleteTargetTodo.setTitle(TITLE);
        deleteTargetTodo.setContent(CONTENT);
        deleteTargetTodo.setId(101);

        todoManager.delete(deleteTargetTodo);
    }

    @Test
    public void TodoManager_delete에서_정상적으로_삭제되는경우() {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);

        MockTodoRepository.passedTodo = todo;

        todoManager.delete(todo);

        assertEquals(todo, MockTodoRepository.passedDeleteTargetTodo);
        assertEquals(true, MockTodoRepository.passedShouldDelete);
    }

    private static class MockTodoRepository extends TodoRepository {

        private static Todo passedTodo;
        private static Todo passedDeleteTargetTodo;
        private static boolean passedShouldDelete;
        private static Exception exception;

        @Override
        public Todo store(Todo todo) throws IllegalArgumentException, RepositoryFailedException {
            passedTodo = todo;

            if(exception!=null && exception.getClass()==IllegalArgumentException.class) { throw (IllegalArgumentException)exception; }
            if(exception!=null && exception.getClass()==RepositoryFailedException.class) { throw (RepositoryFailedException)exception; }
            if(exception!=null && exception.getClass()==RuntimeException.class) { throw (RuntimeException)exception; }

            return todo;
        }

        @Override
        public Todo store(Todo todo, boolean shouldDelete) throws IllegalArgumentException {
            passedDeleteTargetTodo = todo;
            passedShouldDelete = shouldDelete;
            if (!shouldDelete) {
                return null;
            }
            if (exception != null && exception.getClass() == IllegalArgumentException.class) {
                throw (IllegalArgumentException)exception;
            }
            if (passedTodo == null || passedTodo.getId() != todo.getId()) {
                throw new IllegalArgumentException();
            }
            passedTodo = null;
            return null;
        }
    }

    private static class MockNotiManager extends NotiManager {

        private static String DEFAULT_PASSED_TITLE = null;
        private static Exception exception;
        private static String passedTitle = DEFAULT_PASSED_TITLE;

        @Override
        public void notify(String title) throws RuntimeException {

            if(exception!=null && exception.getClass()==RuntimeException.class) { throw (RuntimeException)exception; }

            passedTitle = title;
        }
    }
}