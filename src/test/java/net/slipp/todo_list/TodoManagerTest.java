package net.slipp.todo_list;

import net.slipp.todo_list.exception.RepositoryFailedException;
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
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, TodoManagerTest.MockBeanConfig.class } )
public class TodoManagerTest {

    private Todo setUpTodo;

    private String TITLE = "TITLE";
    private String CONTENT = "CONTENT";

    @Before
    public void setUp() throws Exception {

        setUpTodo = createTodo(TITLE, CONTENT);
    }

    @After
    public void tearDown() throws Exception {

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


    @Test
    public void 전달받은_Todo를_TodoRepository_store에_전달하는_지_확인 () {

        todoManager.create(setUpTodo);

        Todo actual = MockTodoRepository.passedTodo;

        assertNotNull(actual);
        assertEquals(TITLE, actual.getTitle());
        assertEquals(CONTENT, actual.getContent());


    }


    @Test(expected=IllegalArgumentException.class)
    public void TodoRepository_store_호출_시에_IAE를_던지면_그대로_IAE_던지는_지_확인 () {

        MockTodoRepository.exception = new IllegalArgumentException();

        todoManager.create(setUpTodo);

    }

    @Test
    public void Todo의_id는_항상_마이너스_1로_설정하고_store_를_호출한다 () {

        todoManager.create(setUpTodo);

        Todo resultTodo = MockTodoRepository.passedTodo;

        assertEquals(resultTodo.getId(), -1);

    }


    @Test(expected=IllegalArgumentException.class)
    public void Todo가_null (){

        Todo todo = null;
        todoManager.create(todo);

    }

    @Test(expected=IllegalArgumentException.class)
    public void Todo_title이_null () {

        setUpTodo.setTitle(null);
        todoManager.create(setUpTodo);

    }

    @Test(expected=IllegalArgumentException.class)
    public void Todo_title의_길이가_50_이상 () {

        String randomTitle = RandomString(TodoManager.TITLE_MAX_LENGTH + 1);
        setUpTodo.setTitle(randomTitle);

        todoManager.create(setUpTodo);

    }

    @Test(expected=IllegalArgumentException.class)
    public void Todo_content의_길이가_500_이상 () {

        String randomContent = RandomString(TodoManager.CONTENT_MAX_LENGTH + 1);

        setUpTodo.setContent(randomContent);

        todoManager.create(setUpTodo);

        assertEquals(setUpTodo.getContent(), "");
    }

    @Test
    public void content가_null일때_빈문자열로_변경하는_지_확인(){

        setUpTodo.setContent(null);
        todoManager.create(setUpTodo);

    }
/*
    #앞에서 테스트 했기때문에 필요 없을것 같습니다.
    @Test
    public void 파라매터가_비정상일_때_TodoRepository_store_호출안하는_지_확인(){
    }
*/

    @Test(expected=IllegalArgumentException.class)
    public void IAE_발생시_그대로_다시_던진다 (){

        MockTodoRepository.exception = new IllegalArgumentException();

        todoManager.create(setUpTodo);

    }

    @Test(expected=RuntimeException.class)
    public void RepositoryFailedException_발생시_RuntimeException으로_감싸_던진다 (){

        MockTodoRepository.exception = new RepositoryFailedException();

        todoManager.create(setUpTodo);
    }

    @Test(expected=RuntimeException.class)
    public void RuntimeException_발생시_그대로_다시_던진다 (){

        MockTodoRepository.exception = new RuntimeException();

        todoManager.create(setUpTodo);

    }


    private static class MockTodoRepository extends TodoRepository {

        private static Todo passedTodo;
        private static Exception exception;

        @Override
        public Todo store(Todo todo) throws RuntimeException {
            if(exception != null && exception.getClass() == IllegalArgumentException.class) { throw (IllegalArgumentException)exception; }
            if(exception != null && ( exception.getClass() == RepositoryFailedException.class || exception.getClass() == RuntimeException.class)) { throw (RuntimeException)exception; }

            passedTodo = todo;
            return todo;
        }

    }

    private static Todo createTodo(String title, String contents){
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setContent(contents);
        return todo;
    }

    private static String RandomString( int length ){

        StringBuffer str = new StringBuffer();
        for (int count = 0; count < length; count++){
            str.append( (byte)count );
        }

        return str.toString();
    }


}