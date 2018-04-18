package net.slipp.todo_list;

import org.springframework.stereotype.Component;

@Component
public class CountableNotiManager extends NotiManager {

  private int callCount = 0;

  public int getCallCount() {
    return callCount;
  }

  @Override
  public void notify(final String title) throws RuntimeException {
    this.callCount++;
    super.notify(title);
  }
}
