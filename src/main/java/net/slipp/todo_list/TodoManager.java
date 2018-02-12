package net.slipp.todo_list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TodoManager {


	static final int TODO_DEFAULT_ID = -1;
    static final String EMPTY_STRING = "";
    static final String DEFAULT_TITLE = EMPTY_STRING;
    static final int TITLE_MAX_LENGTH = 50;
    static final int CONTENT_MAX_LENGTH = 500;

    @Autowired
    private TodoRepository todoRepository;


    public Todo create(Todo todo) {
    		if (todo == null) {
    			throw new IllegalArgumentException();
    		}

    		todo.setId(TODO_DEFAULT_ID);

    		if (todo.getTitle() == null) {
    			throw new IllegalArgumentException();
    		}
    		if (todo.getTitle().length() >= TITLE_MAX_LENGTH) {
    			throw new IllegalArgumentException();
    		}
    		if (todo.getContent() == null) {
    			todo.setContent("");
    		}
    		if (todo.getContent().length() >= CONTENT_MAX_LENGTH) {
    			throw new IllegalArgumentException();
    		}
    		
        return todoRepository.store(todo);
    }

    // TODO : remove. 개발 전에 spring DI가 제대로 동작하는 지 확인하기 위한 메소드
    public void dum() throws Exception {
        todoRepository.store(null);
    }

}
