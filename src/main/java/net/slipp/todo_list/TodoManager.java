package net.slipp.todo_list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TodoManager {


    static final String EMPTY_STRING = "";
    static final String DEFAULT_TITLE = EMPTY_STRING;


    @Autowired
    private TodoRepository todoRepository;


    public void create(Todo todo) {
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



        todoRepository.store(todo);
    }

    // TODO : remove. 개발 전에 spring DI가 제대로 동작하는 지 확인하기 위한 메소드
    public void dum() {
        todoRepository.store(null);
    }

}
