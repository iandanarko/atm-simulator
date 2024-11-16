package test.exception;

public class InvalidAmountException extends Exception {
  public InvalidAmountException() {
    super("Invalid amount");
  }
}
