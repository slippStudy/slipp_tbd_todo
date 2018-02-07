package net.slipp.todo_list;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, TodoManagerTest.MockBeanConfig.class } )
public class TodoManagerTest {

    @Before
    public void setUp() throws Exception {
        MockTodoRepository.passedTodo = null;
        MockTodoRepository.exception = null;
        MockTodoRepository.isCalledStoreMethod = false;
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

    ////////////////////////////////////////
    // 정상 동작 TC
    ////////////////////////////////////////
    @Test
    public void 전달받은_Todo를_TodoRepository_store에_전달하는_지_확인 () {
        // given
        Todo todo = TodoMockUtil.buildTodo();

        // when
        todoManager.create(todo);
        Todo actual = MockTodoRepository.passedTodo;

        // then
        assertNotNull(actual);
        assertEquals(TodoMockUtil.TITLE, actual.getTitle());
        assertEquals(TodoMockUtil.CONTENT, actual.getContent());
    }


    // create 호출 될 때 mock으로 이상한 id를 넣는 테스트 추가
    @Test
    public void id를_마이너스_1로_설정하는_지_확인() throws Exception {
        // given
        Todo todo = TodoMockUtil.buildTodo();

        // when
        todoManager.create(todo);
        Todo actual = MockTodoRepository.passedTodo;
        final int EXPECTED_ID_FOR_CREATION = -1;

        // then
        assertEquals(EXPECTED_ID_FOR_CREATION, actual.getId());
    }

    ////////////////////////////////////////
    // 파라매터 확인 TC
    ////////////////////////////////////////
    @Test(expected = IllegalArgumentException.class)
    public void Todo가_null일때_IAE던지는_지_확인() throws Exception {
        // given
        Todo nullTodo = null;

        // when
        todoManager.create(nullTodo);

    }

    @Test(expected = IllegalArgumentException.class)
    public void title이_null일때_IAE던지는_지_확인() throws Exception {
        // given
        Todo todo = TodoMockUtil.buildTodoWithTitle(null);

        // when
        todoManager.create(todo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void title_길이가_50_이상이면_IAE던지는_지_확인() throws Exception {
        // given
        String tooMuchLongTitle =
                IntStream.range(10, 30).mapToObj(String::valueOf).collect(Collectors.joining());

        Todo todo = TodoMockUtil.buildTodoWithTitle(tooMuchLongTitle);

        // when
        todoManager.create(todo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void content_길이가_500_이상이면_IAE던지는_지_확인() throws Exception {
        // given
        String tooMuchLongContent =
                IntStream.range(100, 300).mapToObj(String::valueOf).collect(Collectors.joining());

        Todo todo = TodoMockUtil.buildTodoWithContent(tooMuchLongContent);

        // when
        todoManager.create(todo);
    }

    @Test
    public void content가_null일때_빈문자열로_변경하는_지_확인() throws Exception {
        // given
        Todo todo = TodoMockUtil.buildTodoWithContent(null);

        // when
        todoManager.create(todo);

        Todo actual = MockTodoRepository.passedTodo;

        final String EMPTY_STRING_FOR_NULL_CONTENTS = "";
        // then
        assertEquals(EMPTY_STRING_FOR_NULL_CONTENTS, actual.getContent());
    }

    // 위에서 IAE 예외 던지는 것과 비슷한 테스트?
    @Test
    public void 파라매터가_비정상일_때_TodoRepository_store_호출안하는_지_확인() throws Exception {
        // given
        Todo todo = TodoMockUtil.buildTodoWithTitle(null);

        // when
        todoManager.create(todo);

        // then
        assertFalse(MockTodoRepository.isCalledStoreMethod);
    }

    ////////////////////////////////////////
    // Repository 예외 처리 TC
    ////////////////////////////////////////
    @Test(expected=IllegalArgumentException.class)
    public void TodoRepository_store_호출_시에_IAE를_던지면_그대로_IAE_던지는_지_확인 () {
        // given
        Todo todo = TodoMockUtil.buildTodo();

        // when
        MockTodoRepository.exception = new IllegalArgumentException();
        todoManager.create(todo);
    }

    @Test(expected = RuntimeException.class)
    public void TodoRepository_store_호출_시에_RepositoryFailedException을_던지면_RuntimeException_던지는_지_확인()
            throws Exception {
        // given
        Todo todo = TodoMockUtil.buildTodo();

        // when
//        MockTodoRepository.exception = new RepositoryFailedException();
        todoManager.create(todo);
    }

    @Test(expected = RuntimeException.class)
    public void TodoRepository_store_호출_시에_RuntimeException을_던지면_그대로_RuntimeException_던지는_지_확인
            () throws Exception {
        // given
        Todo todo = TodoMockUtil.buildTodo();

        // when
        MockTodoRepository.exception = new RuntimeException();
        todoManager.create(todo);

        // then
    }


    ////////////////////////////////////////
    // Repository 반환 확인 처리 TC
    ////////////////////////////////////////
    @Test
    public void TodoRepository_store의_반환값이_null이_아닌지_확인() throws Exception {
        // given
        Todo todo = TodoMockUtil.buildTodo();

        // when
        todoManager.create(todo);
        Todo actual = MockTodoRepository.passedTodo;

        // then
        assertNotNull(actual);
    }

    @Test
    public void TodoRepository_store의_반환값_Todo의_id가_0_보다_크고_Int_MAX_보다_같거나_작은지_확인()
            throws Exception {
        // given
        Todo todo = TodoMockUtil.buildTodo();

        // when
        todoManager.create(todo);
        Todo actual = MockTodoRepository.passedTodo;

        // then
        assertTrue(actual.getId() > 0);
        assertTrue(actual.getId() <= Integer.MAX_VALUE);
    }

    @Test
    public void TodoRepository_store의_반환값_Todo의_title과_content가_원래값과_동일한지_확인()
            throws Exception {
        // given
        Todo todo = TodoMockUtil.buildTodo();

        // when
        todoManager.create(todo);
        Todo actual = MockTodoRepository.passedTodo;

        // then
        assertEquals(TodoMockUtil.TITLE, actual.getTitle());
        assertEquals(TodoMockUtil.CONTENT, actual.getContent());
    }




    private static class MockTodoRepository extends TodoRepository {

        private static Todo passedTodo;
        private static Exception exception;
        private static boolean isCalledStoreMethod = false;

        @Override
        public Todo store(Todo todo) throws Exception {
            isCalledStoreMethod = true;

            if(exception!=null) { throw exception; }
            passedTodo = todo;
            return todo;
        }

    }


}