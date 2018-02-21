package net.slipp.todo_list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.slipp.exception.RepositoryFailedException;


@Component
public class TodoManager {


    static final String EMPTY_STRING = "";
    static final String DEFAULT_TITLE = EMPTY_STRING;

    private static final int ID_BEFORE_CREATE = -1;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private NotiManager notiManager;


    public void create(Todo todo) {

        validate(todo);

        todo.setId(ID_BEFORE_CREATE);

        try {
            todoRepository.store(todo);
            notiManager.notify(todo.getTitle());
        } catch (RepositoryFailedException e) {
            throw new RuntimeException(e);
        }
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
