package net.slipp.todo_list;

public class TodoMockUtil {
    public static final String TITLE = "TITLE";
    public static final String CONTENT = "CONTENT";

    public static Todo buildTodo() {
       return buildTodo(TITLE, CONTENT);
    }

    public static Todo buildTodoWithTitle(final String title) {
        return buildTodo(title, CONTENT);
    }

    public static Todo buildTodoWithContent(final String content) {
        return buildTodo(TITLE, content);
    }

    public static Todo buildTodo(final String title, final String content) {
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setContent(content);
        return todo;
    }
}
