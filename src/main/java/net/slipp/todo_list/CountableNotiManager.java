package net.slipp.todo_list;

import org.springframework.stereotype.Component;

@Component
public class CountableNotiManager extends NotiManager implements Countable {
    private int count;

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public void notify(String title) {
        this.count++;
        super.notify(title);
    }
}
