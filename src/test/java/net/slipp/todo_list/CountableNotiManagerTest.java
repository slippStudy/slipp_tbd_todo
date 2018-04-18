package net.slipp.todo_list;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class } )
public class CountableNotiManagerTest {

    private static final String TEST_STR = "Test";

    @Autowired
    private CountableNotiManager countableNotiManager;

    @Test
    public void CountableNotiManager의_정상_Count를_확인(){

        countableNotiManager.notify(TEST_STR);

        assertEquals(1, countableNotiManager.getCallCount());
    }


    @Test
    public void CountableNotiManager의_랜덤으로_호출시_정상_Count를_확인(){

        int maxCount = (int)Math.random() * 10;

        for(int i = 0; i < maxCount; i++){
            countableNotiManager.notify(TEST_STR);
        }

        assertEquals(maxCount, countableNotiManager.getCallCount());
    }
}
