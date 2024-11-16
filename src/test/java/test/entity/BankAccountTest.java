package test.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.exception.InsufficientBalanceException;

public class BankAccountTest {
  private User user;
  @BeforeEach
  void init() {
    try {
      user = new User("someone");
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void getNameTest() {
    BankAccount bankAccount = new BankAccount(user);
    assertNotNull(user);
    assertEquals(this.user.getName(), bankAccount.getName());
  }

  @Test
  public void getOwedTest() {
    BigDecimal expected = BigDecimal.ONE;
    BankAccount bankAccount = new BankAccount(user);
    bankAccount.increaseOwed("someone", expected);
    assertEquals(expected, bankAccount.getOwed().get("someone"));
  }

  @Test
  public void getBalanceTest() {
    BigDecimal expected = BigDecimal.TEN;
    BankAccount bankAccount = new BankAccount(user);
    bankAccount.increase(expected);
    assertEquals(expected, bankAccount.getBalance());
  }

  @Test
  public void increaseOwedTest() {
    BigDecimal amount = BigDecimal.TEN;
    BankAccount bankAccount = new BankAccount(user);
    bankAccount.increaseOwed("someone", amount);
    assertEquals(amount, bankAccount.getOwed().get("someone"));
    bankAccount.increaseOwed("someone", amount.multiply(new BigDecimal(-1)));
    assertEquals(null, bankAccount.getOwed().get("someone"));
  }

  @Test
  public void getBalanceSuccessTest() {
    BigDecimal expected = BigDecimal.TEN;
    BankAccount bankAccount = new BankAccount(user);
    bankAccount.increase(expected);
    assertEquals(expected, bankAccount.getBalance());
  }

  @Test
  public void increaseTest() {
    BigDecimal expected = BigDecimal.TEN;
    BankAccount bankAccount = new BankAccount(user);
    bankAccount.increase(expected);
    assertEquals(expected, bankAccount.getBalance());;
  }

  @Test
  public void decreaseSuccessTest() {
    BankAccount bankAccount = new BankAccount(user);
    bankAccount.increase(BigDecimal.TEN);
    BigDecimal amount = BigDecimal.ONE;
    BigDecimal expected = new BigDecimal(9);
    try {
      bankAccount.decrease(amount);
      assertEquals(expected, bankAccount.getBalance());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void decreaseFailedTest() {
    BankAccount bankAccount = new BankAccount(user);
    BigDecimal amount = BigDecimal.ONE;
    assertThrowsExactly(InsufficientBalanceException.class, () -> { 
      bankAccount.decrease(amount);
    });
  }
}
