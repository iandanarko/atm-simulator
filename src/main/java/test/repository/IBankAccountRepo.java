package test.repository;

import java.math.BigDecimal;

import test.entity.BankAccount;

public interface IBankAccountRepo {
  BankAccount findByUserName(String name);
  void create(BankAccount ba) throws Exception;
  void increase(String name, BigDecimal amount);
  void decrease(String name, BigDecimal amount) throws Exception;
}