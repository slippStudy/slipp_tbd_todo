package net.slipp.todo_list;

public class RepositoryFailedException extends Exception {

  public RepositoryFailedException() {
    super();
  }

  public RepositoryFailedException(final String message) {
    super(message);
  }

  public RepositoryFailedException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public RepositoryFailedException(final Throwable cause) {
    super(cause);
  }

  protected RepositoryFailedException(final String message, final Throwable cause,
      final boolean enableSuppression,
      final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
