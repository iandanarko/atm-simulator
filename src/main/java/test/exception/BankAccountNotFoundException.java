package test.exception;

public class BankAccountNotFoundException extends Exception {
  public BankAccountNotFoundException() {
    super("Bank Account not found");
  }
}
