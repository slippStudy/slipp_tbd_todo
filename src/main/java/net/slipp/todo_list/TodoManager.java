package net.slipp.todo_list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.slipp.exception.RepositoryFailedException;


@Component
public class TodoManager {

    static final String EMPTY_STRING = "";
    static final String DEFAULT_TITLE = EMPTY_STRING;

    private static final int ID_BEFORE_CREATE = -1;
    public static final String STR_SPAM = "SPAM";
    public static final String STR_JIRA = "[JIRA]";
    public static final String STR_URGENT = "URGENT";

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private NotiManager notiManager;

    public void create(Todo todo) {
        if(needIgnoreSilently(todo)) {
            return;
        }
        
        validate(todo);

        todo.setId(ID_BEFORE_CREATE);

        try {
            todoRepository.store(todo);

            notify_silently(todo, todo.getTitle());

        } catch (RepositoryFailedException e) {
            throw new RuntimeException();
        }

    }

    private boolean needIgnoreSilently(Todo todo) {
        //“SPAM” 이 들어간 제목의 경우는 예외 던지지 않고 TODO도 생성안됨.
        if(todo != null && todo.getTitle() != null && todo.getTitle().contains(STR_SPAM)) {
            return true;
        }

        return false;
    }

    private void notify_silently(Todo todo, String notiMessage) {
        try{
            if(needNotifyCall(todo)) {
                notiManager.notify(notiMessage);
            }
        } catch (RuntimeException e){
            //ignore
        }
    }

    private boolean needNotifyCall(Todo todo) {
        if(todo != null && todo.getTitle().startsWith(STR_JIRA) && !todo.getTitle().contains(STR_URGENT)) {
            return false;
        }

        return true;
    }

    private void validate(Todo todo) {
        if(todo == null) {
            throw new IllegalArgumentException("todo is null");
        }

        if(todo.getTitle() == null) {
            throw new IllegalArgumentException("title is null");
        }

        if(todo.getTitle().length() >= 50) {
            throw new IllegalArgumentException("title 길이는 50자 이상일 수 없습니다.");
        }

        if(todo.getContent() == null) {
            todo.setContent("");
        }

        if(todo.getContent().length() >= 500) {
            throw new IllegalArgumentException("content 길이는 500자 이상일 수 없습니다.");
        }
    }

}
