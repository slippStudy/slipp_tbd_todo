package net.slipp.exception;

public class IlligalTodoException extends Exception {

  public IlligalTodoException(String message, Throwable cause) {
    super(message, cause);
  }

  public IlligalTodoException(String message) {
    super(message);
  }
}
