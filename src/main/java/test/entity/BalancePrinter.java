package test.entity;

import java.math.BigDecimal;
import java.util.List;

public class BalancePrinter {
  public static void print(BankAccount bankAccount, List<Debt> debtsFrom, List<Debt> debtsTo) {
    System.out.printf("Your balance is $%f\n", bankAccount.getBalance());
    if (debtsTo != null) {
      for (Debt d: debtsFrom) {
        if (d != null && d.getAmount().compareTo(BigDecimal.ZERO) != 0) {
          System.out.printf("Owed $%f to %s\n", d.getAmount(), d.getTo());
        }
      }
    }
    if (debtsFrom != null) {
      for (Debt d: debtsTo) {
        if (d != null && d.getAmount().compareTo(BigDecimal.ZERO) != 0) {
          System.out.printf("Owed $%f from %s\n", d.getAmount(), d.getFrom());
        }
      }
    }
  }
}
