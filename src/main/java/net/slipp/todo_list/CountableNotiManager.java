package net.slipp.todo_list;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier
public class CountableNotiManager extends NotiManager {
    private int callCount;

    @Override
    public void notify(String message) throws RuntimeException {
        this.callCount++;
        super.notify(message);
    }

    public int getCallCount() {
        return callCount;
    }
}
