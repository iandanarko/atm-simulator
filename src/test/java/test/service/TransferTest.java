package test.service;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import test.entity.BankAccount;
import test.entity.Debt;
import test.entity.Session;
import test.entity.User;
import test.exception.BankAccountNotFoundException;
import test.exception.CannotTransferSelfException;
import test.exception.InsufficientBalanceException;
import test.exception.InvalidAmountException;
import test.exception.UserNotLoginException;
import test.repository.BankAccountRepo;
import test.repository.DebtRepo;
import test.repository.IBankAccountRepo;
import test.repository.IDebtRepo;

public class TransferTest {
    private User user;
    private User target;
    private IBankAccountRepo bankAccountRepo;
    private IDebtRepo debtRepo;
    private ITransfer service;

    @BeforeEach
    void init() {
      try {
        this.user = new User("someone");
        this.target = new User("other one");
        this.bankAccountRepo = Mockito.mock(BankAccountRepo.class);
        this.debtRepo = Mockito.mock(DebtRepo.class);
        this.service = new Transfer(bankAccountRepo, debtRepo);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }

    @AfterEach
    void post() {
      Session.clearSession();
    }

    @Test
    public void transferNotLoginTest() {
      assertThrowsExactly(UserNotLoginException.class, () -> {
        service.transfer("someone", BigDecimal.TEN);
      });
    }

    @Test
    public void transferInvalidAmountTest() {
      assertThrowsExactly(InvalidAmountException.class, () -> {
        Session.setSession(user);
        service.transfer("someone", BigDecimal.ZERO);
      });
    }

    @Test
    public void transferSelfTest() {
      assertThrowsExactly(CannotTransferSelfException.class, () -> {
        Session.setSession(user);
        service.transfer(user.getName(), BigDecimal.TEN);
      });
    }

    @Test
    public void transferInvalidBankAccountTest() {
      assertThrowsExactly(BankAccountNotFoundException.class, () -> {
        Session.setSession(user);
        service.transfer("other one", BigDecimal.TEN);
      });
    }

    @Test
    public void transferInsufficentBalanceTest() {
      try {
        Session.setSession(user);
        BankAccount bankAccount = new BankAccount(user);
        bankAccount.increase(BigDecimal.valueOf(1));
        BankAccount bankAccounttarget = new BankAccount(target);
        when(debtRepo.findByFromAndTo("other one", user.getName())).thenReturn(null);
        when(bankAccountRepo.findByUserName(Session.getUser().getName())).thenReturn(bankAccount);
        when(bankAccountRepo.findByUserName(target.getName())).thenReturn(bankAccounttarget);
        when(debtRepo.findByFromAndTo(user.getName(), target.getName())).thenReturn(new Debt(user.getName(), target.getName(), BigDecimal.valueOf(9)));
        service.transfer(target.getName(), BigDecimal.TEN);
        verify(debtRepo, Mockito.times(1)).insert(user.getName(), target.getName(), BigDecimal.valueOf(9));
        verify(bankAccountRepo, Mockito.times(1)).increase(target.getName(), BigDecimal.valueOf(1));
        verify(bankAccountRepo, Mockito.times(1)).decrease(Session.getUser().getName(), BigDecimal.valueOf(1));
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }

    @Test
    public void transferSuccessWithoutDebtTest() {
      try {
        Session.setSession(user);
        BankAccount bankAccount = new BankAccount(user);
        BankAccount bankAccounttarget = new BankAccount(target);
        bankAccount.increase(BigDecimal.TEN);
        when(debtRepo.findByFromAndTo(target.getName(), user.getName())).thenReturn(null);
        when(bankAccountRepo.findByUserName(Session.getUser().getName())).thenReturn(bankAccount);
        when(bankAccountRepo.findByUserName(target.getName())).thenReturn(bankAccounttarget);
        service.transfer(target.getName(), BigDecimal.TEN);
        verify(bankAccountRepo, Mockito.times(1)).increase(target.getName(), BigDecimal.TEN);
        verify(bankAccountRepo, Mockito.times(1)).decrease(Session.getUser().getName(), BigDecimal.TEN);
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }

    @Test
    public void transferSuccessWithDebtTest() {
      try {
        Session.setSession(user);
        BankAccount bankAccount = new BankAccount(user);
        BankAccount bankAccounttarget = new BankAccount(target);
        bankAccount.increase(BigDecimal.TEN);
        Debt debt = new Debt(target.getName(), user.getName(), BigDecimal.valueOf(1));
        when(debtRepo.findByFromAndTo("other one", user.getName())).thenReturn(debt);
        when(bankAccountRepo.findByUserName(Session.getUser().getName())).thenReturn(bankAccount);
        when(bankAccountRepo.findByUserName(target.getName())).thenReturn(bankAccounttarget);
        service.transfer(target.getName(), BigDecimal.TEN);
        verify(debtRepo, Mockito.times(1)).insert(target.getName(), user.getName(), BigDecimal.valueOf(1).negate());
        verify(bankAccountRepo, Mockito.times(1)).increase(target.getName(), BigDecimal.valueOf(9));
        verify(bankAccountRepo, Mockito.times(1)).decrease(Session.getUser().getName(), BigDecimal.valueOf(9));
      } catch (Exception e) {
        fail(e.getMessage());
      }
    }
}
