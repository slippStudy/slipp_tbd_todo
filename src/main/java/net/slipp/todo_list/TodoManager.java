package net.slipp.todo_list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TodoManager {


    static final String EMPTY_STRING = "";
    static final String DEFAULT_CONTENT = EMPTY_STRING;


    @Autowired
    private TodoRepository todoRepository;


    public void create(Todo todo) throws Exception {

        if (todo == null) {
            throw new IllegalArgumentException("todo is null");
        }

        if(todo.getTitle()==null) {
            throw new IllegalArgumentException("todo.title is null");
        }

        final String title = todo.getTitle();
        if (title.length() >= 50) {
            throw new IllegalArgumentException("todo.title이 50글자 이상입니다");
        }

        final String content = todo.getContent();
        if (content != null && content.length() >= 500) {
            throw new IllegalArgumentException("todo.content가 500글자 이상입니다");
        }

        if (content == null) {
            todo.setContent(DEFAULT_CONTENT);
        }

        todo.setId(-1);
        try {
            todoRepository.store(todo);
        } catch (RepositoryFailedException e) {
            throw new RuntimeException();
        } catch (RuntimeException r) {
            throw r;
        }
    }

    // TODO : remove. 개발 전에 spring DI가 제대로 동작하는 지 확인하기 위한 메소드
    public void dum() throws Exception {
        todoRepository.store(null);
    }

}
