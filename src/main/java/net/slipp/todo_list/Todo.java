package net.slipp.todo_list;


public class Todo {
    private int id;
    private String title;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Todo create(String title, String content) {
    		Todo todo = new Todo();
    		todo.setTitle(title);
    		todo.setContent(content);
    		return todo;
    }

    private static Todo create(int id, String title, String content) {
    		Todo todo = new Todo();
    		todo.setId(id);
    		todo.setTitle(title);
    		todo.setContent(content);
		return todo;
	}

	public Todo newClone() {
		return Todo.create(id, title, content);
	}

}
