package net.slipp.todo_list;

import net.slipp.exception.RepositoryFailedException;
import org.springframework.stereotype.Component;


@Component
public class TodoRepository {

    public Todo store(Todo todo) throws IllegalArgumentException, RepositoryFailedException { return null; }


}
