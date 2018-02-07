package net.slipp.todo_list;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class TodoValidator {
    static final int MAX_TITLE_LENGTH = 49;
    static final int MAX_CONTENT_LENGTH = 499;
    public static void validate(Todo todo) {
        if (isNull(todo)) {
            throw new IllegalArgumentException("todo parameter must be not null");
        }

        if (isNull(todo.getTitle())) {
            throw new IllegalArgumentException("title must be not null");
        }

        if (todo.getTitle().length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("max title length is " + MAX_TITLE_LENGTH);
        }

        if (nonNull(todo.getContent()) && todo.getContent().length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("max title length is " + MAX_CONTENT_LENGTH);
        }
    }
}
