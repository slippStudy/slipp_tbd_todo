package net.slipp.todo_list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TodoManager {


    static final String EMPTY_STRING = "";
    static final String DEFAULT_TITLE = EMPTY_STRING;


    @Autowired
    private TodoRepository todoRepository;


    public Todo create(Todo todo) {

        if(todo.getTitle()==null) { todo.setTitle(DEFAULT_TITLE); }
        todo = todoRepository.save(todo);
        return todo;
    }

    // TODO : remove. 개발 전에 spring DI가 제대로 동작하는 지 확인하기 위한 메소드
    public void dum() {
        todoRepository.save(null);
    }

}
