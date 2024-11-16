package test.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import test.entity.BalancePrinter;
import test.entity.BankAccount;
import test.entity.Debt;
import test.entity.Session;
import test.exception.BankAccountNotFoundException;
import test.exception.CannotTransferSelfException;
import test.exception.InsufficientBalanceException;
import test.exception.InvalidAmountException;
import test.exception.UserNotLoginException;
import test.repository.IBankAccountRepo;
import test.repository.IDebtRepo;

public class Transfer implements ITransfer {
  private IBankAccountRepo bankAccountRepo;
  private IDebtRepo debtRepo;

  public Transfer(IBankAccountRepo bankAccountRepo, IDebtRepo debtRepo) {
    this.bankAccountRepo = bankAccountRepo;
    this.debtRepo = debtRepo;
  }

  public void transfer(String to, BigDecimal amount) throws Exception {
      if (Session.isNullSession()) {
        throw new UserNotLoginException();
      }
      if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidAmountException();
      }

      if (to.equals(Session.getUser().getName())) {
        throw new CannotTransferSelfException();
      }

      Debt debt = debtRepo.findByFromAndTo(to, Session.getUser().getName());
      BankAccount bankAccount = bankAccountRepo.findByUserName(Session.getUser().getName());

      BankAccount bankAccountTarget = bankAccountRepo.findByUserName(to);
      if (bankAccountTarget == null) {
        throw new BankAccountNotFoundException();
      }

      if (debt != null && debt.getAmount().compareTo(BigDecimal.ZERO) > 0) {
        BigDecimal reducedDebt = amount;
        if (debt.getAmount().compareTo(amount) < 0) {
          reducedDebt = debt.getAmount();
        }
        // reduce debt
        debtRepo.insert(to, Session.getUser().getName(), reducedDebt.negate());
        amount = amount.subtract(reducedDebt);
      }

      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        BigDecimal transferredAmount = amount;
        if (bankAccount.getBalance().compareTo(amount) < 0) {
          transferredAmount = bankAccount.getBalance();
          debtRepo.insert(Session.getUser().getName(), to, amount.subtract(bankAccount.getBalance()));
        }
        bankAccountRepo.increase(to, transferredAmount); // increase target balance
        bankAccountRepo.decrease(Session.getUser().getName(), transferredAmount); // decrease current user balance
        System.out.printf("Transferred $%f to %s\n", transferredAmount, to);
      }

      List<Debt> debtsTo = new ArrayList<>();
      if (debt != null) {
        debtsTo.add(debt);
      }
      List<Debt> debtsFrom = new ArrayList<>();
      Debt debtFrom = debtRepo.findByFromAndTo(Session.getUser().getName(), to);
      if (debtFrom != null) {
        debtsFrom.add(debtFrom);
      }
      BalancePrinter.print(bankAccount, debtsFrom, debtsTo);
  }
}
