package net.slipp.todo_list;

import org.springframework.stereotype.Component;

/**
 * @author jyp-airmac
 * @date 2018. 4. 4.
 */
@Component
public class CountableNotiManager extends NotiManager {

    private int callCount = 0;

    public int getCallCount() {
        return this.callCount;
    }

    @Override
    public void notify(String title) throws RuntimeException {
        super.notify(title);
        this.callCount++;
    }
}
