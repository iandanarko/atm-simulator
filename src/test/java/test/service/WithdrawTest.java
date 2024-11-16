package test.service;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import test.entity.BankAccount;
import test.entity.Session;
import test.entity.User;
import test.exception.InsufficientBalanceException;
import test.exception.UserNotLoginException;
import test.repository.BankAccountRepo;
import test.repository.IBankAccountRepo;

public class WithdrawTest {
  private IWithdraw service;
  private IBankAccountRepo bankAccountRepo;
  private User user;

  @BeforeEach
  void init() {
    try {
      bankAccountRepo = Mockito.mock(BankAccountRepo.class);
      service = new Withdraw(bankAccountRepo);
      user = new User("someone");
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void withdrawFailedNotLoginTest() {
    assertThrowsExactly(UserNotLoginException.class, () -> {
      service.withdraw(BigDecimal.TEN);
    });
  }

  @Test
  public void withdrawSuccessTest() {
    try {
      Session.setSession(user);
      BankAccount ba = new BankAccount(user);
      ba.increase(BigDecimal.TEN);
      when(bankAccountRepo.findByUserName(anyString())).thenReturn(ba);
      service.withdraw(BigDecimal.TEN);
      verify(bankAccountRepo, Mockito.times(1)).decrease(user.getName(), BigDecimal.TEN);
      verify(bankAccountRepo, Mockito.times(1)).findByUserName(user.getName());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void withdrawInsufficentBalanceFailedTest() {
    assertThrowsExactly(InsufficientBalanceException.class, () -> {
      Session.setSession(user);
      BankAccount ba = new BankAccount(user);
      when(bankAccountRepo.findByUserName(anyString())).thenReturn(ba);
      service.withdraw(BigDecimal.TEN);
      verify(bankAccountRepo, Mockito.times(1)).findByUserName(user.getName());
    });
  }
}
