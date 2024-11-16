package test.service;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import test.entity.BankAccount;
import test.entity.Debt;
import test.entity.Session;
import test.entity.User;
import test.exception.InvalidAmountException;
import test.exception.UserNotLoginException;
import test.repository.BankAccountRepo;
import test.repository.DebtRepo;
import test.repository.IBankAccountRepo;
import test.repository.IDebtRepo;

public class DepositTest {
    private User user;
    private IBankAccountRepo bankAccountRepo;
    private IDebtRepo debtRepo;
    private IDeposit service;

    @BeforeEach
    void init() {
      try {
        this.user = new User("someone");
        this.bankAccountRepo = Mockito.mock(BankAccountRepo.class);
        this.debtRepo = Mockito.mock(DebtRepo.class);
        this.service = new Deposit(bankAccountRepo, debtRepo);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }

    @AfterEach
    void post() {
      Session.clearSession();
    }

    @Test
    public void depositNotLoginTest() {
      assertThrowsExactly(UserNotLoginException.class, () -> {
        Session.clearSession();
        service.deposit(BigDecimal.TEN);
      });
    }

    @Test
    public void depositInvalidAmountTest() {
      assertThrowsExactly(InvalidAmountException.class, () -> {
        Session.setSession(user);
        service.deposit(BigDecimal.ZERO);
      });
    }

    @Test
    public void depositWithoutDebtTest() {
      try {
        Session.setSession(user);
        BankAccount bankAccount = new BankAccount(user);
        when(debtRepo.findByFrom(user.getName())).thenReturn(List.of());
        when(bankAccountRepo.findByUserName(user.getName())).thenReturn(bankAccount);
        service.deposit(BigDecimal.TEN);
        verify(bankAccountRepo, Mockito.times(1)).increase(user.getName(), BigDecimal.TEN);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }

    @Test
    public void depositWithDebtTest() {
      try {
        Session.setSession(user);
        BankAccount bankAccount = new BankAccount(user);
        Debt debt1 = new Debt(user.getName(), "user 1", BigDecimal.ZERO);
        Debt debt2 = new Debt(user.getName(), "user 2", BigDecimal.valueOf(20));
        Debt debt3 = new Debt(user.getName(), "user 3", BigDecimal.TEN);
        List<Debt> debts = List.of(debt1, debt2, debt3);
        when(debtRepo.findByFrom(user.getName())).thenReturn(debts);
        when(bankAccountRepo.findByUserName(user.getName())).thenReturn(bankAccount);
        service.deposit(BigDecimal.TEN);
        verify(bankAccountRepo, Mockito.times(1)).increase("user 2", BigDecimal.TEN);
        verify(bankAccountRepo, Mockito.times(0)).increase(user.getName(), BigDecimal.TEN);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }
}
