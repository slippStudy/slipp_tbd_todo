package net.slipp.todo_list;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class CountableNotiManager extends NotiManager {

    private int callCount = 0;

    public int getCallCount() {
        return this.callCount;
    }

    @Override
    public void notify(String notiMessage) {
        this.callCount++;
        super.notify(notiMessage);
    }

}