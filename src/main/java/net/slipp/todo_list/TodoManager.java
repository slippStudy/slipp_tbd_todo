package net.slipp.todo_list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TodoManager {

    private static final String EMPTY_STRING = "";
    private static final String DEFAULT_TITLE = EMPTY_STRING;
    private static final int MAX_TITLE_LENGTH = 49;
    private static final int MAX_CONTENT_LENGTH = 499;

    @Autowired
    private TodoRepository todoRepository;

    public void create(Todo todo) {
        validateAndInitDefaultValues(todo);
        try {
            todoRepository.store(todo);
        } catch (RepositoryFailedException rfe) {
            throw new RuntimeException();
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (RuntimeException rfe) {
            throw rfe;
        }
    }

    private void validateAndInitDefaultValues(Todo todo) {
        if(todo == null) {
            throw new IllegalArgumentException();
        }

        if(todo.getContent() == null) { todo.setContent(DEFAULT_TITLE); }

        if(todo.getTitle() == null) {
            throw new IllegalArgumentException();
        }

        if(todo.getTitle().length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException();
        }

        if(todo.getContent().length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException();
        }

        todo.setId(-1);
    }

    // TODO : remove. 개발 전에 spring DI가 제대로 동작하는 지 확인하기 위한 메소드
    public void dum() throws RepositoryFailedException {
        todoRepository.store(null);
    }

}
