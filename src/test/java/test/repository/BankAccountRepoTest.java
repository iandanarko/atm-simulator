package test.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.entity.BankAccount;
import test.entity.User;
import test.exception.BankAccountExistsException;
import test.exception.InsufficientBalanceException;

public class BankAccountRepoTest {
  private User user;
  private BankAccountRepo repo;
  
  @BeforeEach
  void init() {
    try {
      user = new User("someone");
      repo = new BankAccountRepo();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void createSuccessTest() {
    try {
      repo.create(new BankAccount(this.user));
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void createFailedDuplicateTest() {
    assertThrowsExactly(BankAccountExistsException.class, () -> {
      repo.create(new BankAccount(this.user));
      repo.create(new BankAccount(this.user));
    });

  }

  @Test
  public void findByUserNameSuccessTest() {
    try {
      BankAccount exp = new BankAccount(this.user);
      repo.create(exp);
      assertEquals(exp, repo.findByUserName(this.user.getName()));
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void findByUserNameNotFoundTest() {
    assertNull(repo.findByUserName("other one"));
  }

  @Test
  public void increaseSuccessTest() {
    try {
      BankAccount ba = new BankAccount(this.user);
      BigDecimal exp = BigDecimal.TEN;
      repo.create(ba);
      repo.increase(this.user.getName(), exp);
      assertEquals(exp, ba.getBalance());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void decreaseSuccessTest() {
    try {
      BankAccount ba = new BankAccount(this.user);
      BigDecimal exp = new BigDecimal(9);
      repo.create(ba);
      repo.increase(this.user.getName(), BigDecimal.TEN);
      repo.decrease(this.user.getName(), BigDecimal.ONE);
      assertEquals(exp, ba.getBalance());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void decreaseFailedInsufficientBalanceTest() {
    assertThrowsExactly(InsufficientBalanceException.class, () -> {
      BankAccount ba = new BankAccount(this.user);
      repo.create(ba);
      repo.decrease(this.user.getName(), BigDecimal.ONE);
    });
  }
}
