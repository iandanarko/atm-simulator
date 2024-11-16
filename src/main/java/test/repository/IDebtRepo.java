package test.repository;

import java.math.BigDecimal;
import java.util.List;

import test.entity.Debt;

public interface IDebtRepo {
  List<Debt> findByFrom(String from);
  List<Debt> findByTo(String to);
  Debt findByFromAndTo(String from, String to);
  void insert(String from, String to, BigDecimal amount);
}