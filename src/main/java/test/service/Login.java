package test.service;

import java.util.List;

import test.entity.BalancePrinter;
import test.entity.BankAccount;
import test.entity.Debt;
import test.entity.Session;
import test.entity.User;
import test.exception.UserNotLogoutException;
import test.repository.IBankAccountRepo;
import test.repository.IDebtRepo;
import test.repository.IUserRepo;

public class Login implements ILogin {
  private IUserRepo userRepo;
  private IBankAccountRepo bankAccountRepo;
  private IDebtRepo debtRepo;

  public Login(IUserRepo userRepo, IBankAccountRepo bankAccountRepo, IDebtRepo debtRepo) {
    this.userRepo = userRepo;
    this.bankAccountRepo = bankAccountRepo;
    this.debtRepo = debtRepo;
  }

  public void login(String name) throws Exception {
    if (!Session.isNullSession()) {
      throw new UserNotLogoutException();
    }

    User user = userRepo.findUserByName(name);
    if (user == null) {
      user = createNewUser(name);
    }

    Session.setSession(user);
    System.out.printf("Hello, %s!\n", user.getName());
    BankAccount bankAccount = bankAccountRepo.findByUserName(name);
    List<Debt> debtsFrom = debtRepo.findByFrom(name);
    List<Debt> debtsTo = debtRepo.findByTo(name);
    BalancePrinter.print(bankAccount, debtsFrom, debtsTo);
  }

  private User createNewUser(String name) throws Exception {
    User user = new User(name);
    userRepo.insert(user);
    BankAccount ba = new BankAccount(user);
    bankAccountRepo.create(ba);
    return user;
  }
}
