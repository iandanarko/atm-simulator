package test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import test.entity.BankAccount;
import test.entity.Debt;
import test.entity.Session;
import test.entity.User;
import test.exception.UserNotLogoutException;
import test.repository.BankAccountRepo;
import test.repository.DebtRepo;
import test.repository.IBankAccountRepo;
import test.repository.IDebtRepo;
import test.repository.IUserRepo;
import test.repository.UserRepo;

public class LoginTest {
  private ILogin service;
  private IUserRepo userRepo;
  private IBankAccountRepo bankAccountRepo;
  private IDebtRepo debtRepo;
  private User user;

  @BeforeEach
  void init() {
    userRepo  = Mockito.mock(UserRepo.class);
    bankAccountRepo = Mockito.mock(BankAccountRepo.class);
    debtRepo = Mockito.mock(DebtRepo.class);
    service = new Login(userRepo, bankAccountRepo, debtRepo);
    Session.clearSession();
    try {
      user = new User("someone");
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @AfterEach
  void post() {
    Session.clearSession();
  }

  @Test
  public void loginUserNotLogoutTest() {
    Session.setSession(this.user);
    assertThrowsExactly(UserNotLogoutException.class, () -> {service.login("other one");});
  }

  @Test
  public void loginSuccessTest() {
    try {
      List<Debt> debtList = new ArrayList<>(); 
      when(userRepo.findUserByName(this.user.getName())).thenReturn(this.user);
      when(bankAccountRepo.findByUserName(this.user.getName())).thenReturn(new BankAccount(user));
      when(debtRepo.findByFrom(this.user.getName())).thenReturn(debtList);
      when(debtRepo.findByTo(this.user.getName())).thenReturn(debtList);
      service.login(this.user.getName());
      assertEquals(user, Session.getUser());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void loginNewUserSuccessTest() {
    try {
      String exp = "who";
      List<Debt> debtList = new ArrayList<>(); 
      when(userRepo.findUserByName(exp)).thenReturn(null);
      when(bankAccountRepo.findByUserName(exp)).thenReturn(new BankAccount(user));
      when(debtRepo.findByFrom(exp)).thenReturn(debtList);
      when(debtRepo.findByTo(exp)).thenReturn(debtList);
      service.login(exp);
      verify(userRepo, Mockito.times(1)).findUserByName(exp);
      verify(userRepo, Mockito.times(1)).insert(any());
      verify(bankAccountRepo, Mockito.times(1)).create(any());
      assertEquals(exp, Session.getUser().getName());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
