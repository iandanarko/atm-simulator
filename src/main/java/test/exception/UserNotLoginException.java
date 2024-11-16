package test.exception;

public class UserNotLoginException extends Exception {
  public UserNotLoginException() {
    super("user not login yet");
  }
}
