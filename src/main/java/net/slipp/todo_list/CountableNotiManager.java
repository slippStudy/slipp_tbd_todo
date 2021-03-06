package net.slipp.todo_list;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class CountableNotiManager extends NotiManager {

  private int callCount = 0;

  public int getCallCount() {
    return this.callCount;
  }

  @Override
  public void notify(String title) throws RuntimeException {
    this.callCount++;
    super.notify(title);
  }
}
