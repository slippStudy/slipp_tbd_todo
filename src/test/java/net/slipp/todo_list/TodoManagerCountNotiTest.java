package net.slipp.todo_list;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class} )
public class TodoManagerCountNotiTest {

    @Autowired
    private TodoManager todoManager;

    @Autowired
    private NotiManager notiManager;
    
    @Test
    public void NotiManager를_호출한_수가_실제_SMS서비스_업체가_받은_수와_다르다고_한다_이_쪽의_오류가_없는_것을_확인() {
        String TITLE = "TITLE";
        String CONTENT = "CONTENT";

        Todo todo = new Todo();
        todo.setTitle(TITLE);
        todo.setContent(CONTENT);
        todoManager.create(todo);
        CountableNotiManager notiManager = (CountableNotiManager) this.notiManager;
        int count = notiManager.getCallCount();
        assertEquals(1, count);
    }

}
