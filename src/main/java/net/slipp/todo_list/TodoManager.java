package net.slipp.todo_list;

import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.slipp.todo_list.exception.RepositoryFailedException;

@Component
public class TodoManager {
    static final int DEFAULT_ID_FOR_CREATION = -1;
    static final String EMPTY_STRING = "";
    static final String DEFAULT_CONTENT = EMPTY_STRING;

    @Autowired
    private TodoRepository todoRepository;

    public void create(Todo todo) {
        TodoValidator.validate(todo);

        todo.setId(DEFAULT_ID_FOR_CREATION);

        if (isNull(todo.getContent())) {
            todo.setContent(DEFAULT_CONTENT);
        }

        try {
            todoRepository.store(todo);
        } catch (RepositoryFailedException | RuntimeException e) {
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        if (e.getClass() == RepositoryFailedException.class) {
            throw new RuntimeException("failed to store the given todo");
        }

        if (e.getClass() == IllegalArgumentException.class) {
            throw new IllegalArgumentException("failed to store ", e);
        }

        if (e.getClass() == RuntimeException.class) {
            throw new IllegalArgumentException("failed to store ", e);
        }
    }

    // TODO : remove. 개발 전에 spring DI가 제대로 동작하는 지 확인하기 위한 메소드
    public void dum() {
        try {
            todoRepository.store(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
