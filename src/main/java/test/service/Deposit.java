package test.service;

import java.math.BigDecimal;
import java.util.List;

import test.entity.BalancePrinter;
import test.entity.BankAccount;
import test.entity.Debt;
import test.entity.Session;
import test.exception.InvalidAmountException;
import test.exception.UserNotLoginException;
import test.repository.IBankAccountRepo;
import test.repository.IDebtRepo;

public class Deposit implements IDeposit {
    private IBankAccountRepo bankAccountRepo;
    private IDebtRepo debtRepo;

    public Deposit(IBankAccountRepo bankAccountRepo, IDebtRepo debtRepo) {
      this.bankAccountRepo = bankAccountRepo;
      this.debtRepo = debtRepo;
    }

    public void deposit(BigDecimal amount) throws Exception {
      if (Session.isNullSession()) {
        throw new UserNotLoginException();
      }
      if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidAmountException();
      }
      List<Debt> debts = debtRepo.findByFrom(Session.getUser().getName());
      BankAccount bankAccount = bankAccountRepo.findByUserName(Session.getUser().getName());

      for (Debt d: debts) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
          break;
        }
        if (d.getAmount().compareTo(BigDecimal.ZERO) == 0) {
          continue;
        }

        BigDecimal transferredAmount = d.getAmount();
        if (transferredAmount.compareTo(amount) > 0) {
          transferredAmount = amount;
        }
        amount = amount.subtract(transferredAmount);
        // Do transfer
        bankAccountRepo.increase(d.getTo(), transferredAmount); // increase target balance
        d.increase(transferredAmount.negate()); // decrease debt
        System.out.printf("Transferred $%f to %s\n", transferredAmount, d.getTo());
      }

      if (amount.compareTo(BigDecimal.ZERO) > 0) {
        bankAccountRepo.increase(Session.getUser().getName(), amount);
      }

      BalancePrinter.print(bankAccount, debts, debtRepo.findByTo(Session.getUser().getName()));
    }
}
