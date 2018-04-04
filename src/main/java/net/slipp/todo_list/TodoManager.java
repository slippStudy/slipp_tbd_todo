package net.slipp.todo_list;

import net.slipp.exception.DisallowWordIncludeTitleException;
import net.slipp.exception.RepositoryFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TodoManager {

    static final String EMPTY_STRING = "";
    static final String DEFAULT_TITLE = EMPTY_STRING;
    static final String DISALLOW_TITLE_INCLUDE_WORD = "SPAM";
    static final String JIRA_TITLE_PREFIX = "[JIRA]";
    static final String URGENT_STRING = "URGENT";
    
    private static final int ID_BEFORE_CREATE = -1;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private NotiManager notiManager;

    public void create(Todo todo) {

    	    try {
    	        validate(todo);
    	        
    	    } catch (DisallowWordIncludeTitleException dwte) {
    	        return;	//ignore
    	    }
        

        todo.setId(ID_BEFORE_CREATE);

        try {

            todoRepository.store(todo);

            notify_silently(todo.getTitle());

        } catch (RepositoryFailedException e) {
            throw new RuntimeException("Repository에 저장이 실패하였습니다", e);
        }

    }

    private void notify_silently(String notiMessage) {
    	    
    	    if (!isUrgent(notiMessage) && isJiraCalled(notiMessage)) {
    	    	    return;
    	    }
    	    
        try{
            notiManager.notify(notiMessage);
        } catch (RuntimeException e){
            //ignore
        }
    }
    
    private boolean isUrgent(String notiMessage) {
    	    return notiMessage.contains(URGENT_STRING);
    }
    
    private boolean isJiraCalled(String notiMessage) {
    	    return notiMessage.startsWith(JIRA_TITLE_PREFIX);
    }
    
	private void validate(Todo todo) {
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
        
        if(todo.getTitle().contains(DISALLOW_TITLE_INCLUDE_WORD)) {
        	    throw new DisallowWordIncludeTitleException();
        }
    }

}
