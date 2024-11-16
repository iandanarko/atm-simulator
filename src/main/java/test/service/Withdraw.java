package test.service;

import java.math.BigDecimal;
import java.util.List;

import test.entity.BalancePrinter;
import test.entity.BankAccount;
import test.entity.Session;
import test.exception.InsufficientBalanceException;
import test.exception.UserNotLoginException;
import test.repository.IBankAccountRepo;

public class Withdraw implements IWithdraw {
  private IBankAccountRepo bankAccountRepo;

  public Withdraw(IBankAccountRepo bankAccountRepo) {
    this.bankAccountRepo = bankAccountRepo;
  }

  public void withdraw(BigDecimal amount) throws Exception {
    if (Session.isNullSession()) {
      throw new UserNotLoginException();
    }
    BankAccount bankAccount = bankAccountRepo.findByUserName(Session.getUser().getName());
    if (bankAccount.getBalance().compareTo(amount) < 0 ) {
      throw new InsufficientBalanceException();
    }
    this.bankAccountRepo.decrease(Session.getUser().getName(), amount);
    BalancePrinter.print(bankAccount, List.of(), List.of());
  }
}
