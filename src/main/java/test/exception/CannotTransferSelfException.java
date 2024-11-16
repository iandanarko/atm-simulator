package test.exception;

public class CannotTransferSelfException extends Exception {
  public CannotTransferSelfException() {
    super("Cannot transfer self");
  }
}
