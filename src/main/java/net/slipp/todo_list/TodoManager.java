package net.slipp.todo_list;

import net.slipp.exception.IlligalTodoException;
import net.slipp.exception.NotNotiException;
import net.slipp.exception.RepositoryFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TodoManager {


  static final String EMPTY_STRING = "";
  static final String DEFAULT_TITLE = EMPTY_STRING;
  static final String JIRA_STRING = "[JIRA]";
  static final String URGENT_STRING = "URGENT";
  static final String SPAM_STRING = "SPAM";

  private static final int ID_BEFORE_CREATE = -1;

  @Autowired
  private TodoRepository todoRepository;

  @Autowired
  private NotiManager notiManager;

  public void create(Todo todo) {

    try {
      validate(todo);
    } catch (IlligalTodoException e) {
      return;
    }
    todo.setId(ID_BEFORE_CREATE);

    try {
      todoRepository.store(todo);
      notify_silently(todo.getTitle());

    } catch (RepositoryFailedException e) {
      throw new RuntimeException();
    }

  }

  private void notify_silently(String notiMessage) {
    try {
      notiValidate(notiMessage);
    } catch (NotNotiException e) {
      return;
    }
    try {
      notiManager.notify(notiMessage);


    } catch (RuntimeException e) {
      //ignore
    }
  }

  private void notiValidate(String title) throws NotNotiException {
    if (title.startsWith(JIRA_STRING) && !title.contains(URGENT_STRING)) {
      throw new NotNotiException();
    }
  }

  private void validate(Todo todo) throws IlligalTodoException {
    if (todo == null) {
      throw new IllegalArgumentException("todo is null");
    }

    if (todo.getTitle() == null) {
      throw new IllegalArgumentException("title is null");
    }

    if (todo.getTitle().length() >= 50) {
      throw new IllegalArgumentException("title 길이는 50자 이상일 수 없습니다.");
    }

    if (todo.getContent() == null) {
      todo.setContent("");
    }

    if (todo.getContent().length() >= 500) {
      throw new IllegalArgumentException("content 길이는 500자 이상일 수 없습니다.");
    }

    if (todo.getTitle().contains(SPAM_STRING)) {
      throw new IlligalTodoException();
    }

  }

}
