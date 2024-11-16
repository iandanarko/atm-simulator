package test.exception;

public class InsufficientBalanceException extends Exception {
  public InsufficientBalanceException() {
    super("insufficient balance");
  }
}
