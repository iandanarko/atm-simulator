package test.exception;

public class UserNotLogoutException extends Exception {
  public UserNotLogoutException() {
    super("user not logout yet");
  }
}
