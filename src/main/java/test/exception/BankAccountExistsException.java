package test.exception;

public class BankAccountExistsException extends Exception {
  public BankAccountExistsException() {
    super("Bank Account already exists");
  }
}
