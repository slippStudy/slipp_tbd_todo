package net.slipp.todo_list;

import org.springframework.stereotype.Component;

import net.slipp.todo_list.exception.RepositoryFailedException;

@Component
public class TodoRepository {

    public Todo store(Todo todo) throws RepositoryFailedException { return null; }

}
