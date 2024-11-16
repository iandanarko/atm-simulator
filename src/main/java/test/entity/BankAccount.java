package test.entity;

import java.util.HashMap;
import java.util.Map;

import test.exception.InsufficientBalanceException;

import java.math.BigDecimal;

public class BankAccount {
  private User user;
  private BigDecimal balance;
  private HashMap<String, BigDecimal> oweMap;

  public BankAccount(User user) {
    this.user = user;
    this.balance = BigDecimal.ZERO;
    this.oweMap = new HashMap<>();
  }

  public String getName() {
    return this.user.getName();
  }

  public BigDecimal getBalance() {
    return this.balance;
  }

  public HashMap<String, BigDecimal> getOwed() {
    return this.oweMap;
  }

  public void increaseOwed(String target, BigDecimal amount) {
    BigDecimal owe = this.oweMap.get(target) == null ? new BigDecimal(0) : this.oweMap.get(target);
    owe = owe.add(amount);

    if (owe.equals(BigDecimal.ZERO)) {
      this.oweMap.remove(target);
      return;
    }
    this.oweMap.put(target, owe);
  }

  public void printBalance() {
    System.out.printf("Your balance is $%f\n", this.balance);
    for (Map.Entry<String, BigDecimal> set : oweMap.entrySet()) {
      BigDecimal amount = set.getValue();
      if (amount.equals(BigDecimal.ZERO)) {
        continue;
      }
      String s = (amount.compareTo(BigDecimal.ZERO) > 0) ? "from" : "to";
      System.out.printf("Owed $%f %s %s\n", amount.abs(), s, set.getKey());
    }
  }

  public void decrease(BigDecimal amount) throws Exception{
    if (amount.compareTo(this.balance) > 0) {
      throw new InsufficientBalanceException();
    }
    this.balance = this.balance.subtract(amount);
  }

  public void increase(BigDecimal amount) {
    this.balance = this.balance.add(amount);
  }
}

