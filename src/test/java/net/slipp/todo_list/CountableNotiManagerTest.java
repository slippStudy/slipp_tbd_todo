package net.slipp.todo_list;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class} )
public class CountableNotiManagerTest {
    @Autowired
    NotiManager notiManager;

    @Test
    public void 정상적인_message일때_NotiManager를_호출한_수가_실제_호출한_수와_동일한지_확인() {
        int calledCount = 5;

        for(int i=0; i<calledCount; i++) {
            notiManager.notify("test");
        }

        if(notiManager instanceof CountableNotiManager) {
            Assert.assertEquals(((CountableNotiManager) notiManager).getCallCount(), calledCount);
        }
    }
}
