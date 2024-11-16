package test.repository;

import java.math.BigDecimal;
import java.util.HashMap;

import test.entity.BankAccount;
import test.exception.BankAccountExistsException;

public class BankAccountRepo implements IBankAccountRepo {
  HashMap<String, BankAccount> db;
  
  public BankAccountRepo() {
    this.db = new HashMap<>();
  }

  public BankAccount findByUserName(String name) {
    return this.db.get(name);
  }

  public void create(BankAccount ba) throws Exception {
    if (db.get(ba.getName()) != null) {
      throw new BankAccountExistsException();
    }
    this.db.put(ba.getName(), ba);
  }

  public void increase(String name, BigDecimal amount) {
    this.db.get(name).increase(amount);
  }

  public void decrease(String name, BigDecimal amount) throws Exception {
    this.db.get(name).decrease(amount);
  }
}
