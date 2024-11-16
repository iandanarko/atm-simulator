package test.exception;

public class InvalidCommand extends Exception {
  public InvalidCommand() {
    super("Invalid command");
  }
}
