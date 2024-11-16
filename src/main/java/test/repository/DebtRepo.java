package test.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import test.entity.Debt;


public class DebtRepo implements IDebtRepo {
  HashMap<String, List<Debt>> debtFrom;
  HashMap<String, List<Debt>> debtTo;

  public DebtRepo() {
    this.debtFrom = new HashMap<>();
    this.debtTo = new HashMap<>();
  }

  public List<Debt> findByFrom(String from) {
    if (this.debtFrom.get(from) == null) {
      return new ArrayList<Debt>();
    }
    return this.debtFrom.get(from);
  } 

  public List<Debt> findByTo(String to) {
    if (this.debtTo.get(to) == null) {
      return new ArrayList<Debt>();
    }
    return this.debtTo.get(to);
  }

  public Debt findByFromAndTo(String from, String to) {
    if (this.debtFrom.get(from) == null) {
      return null;
    }
    for (Debt i : this.debtFrom.get(from)) {
      if (i.getTo().equals(to)) {  
        return i;
      }
    }
    return null;
  }

  public void insert(String from, String to, BigDecimal amount) {
    Debt debtFrom = this.findByFromAndTo(from, to);
    if (debtFrom != null) {
      debtFrom.increase(amount);
      return;
    }
    if (amount.compareTo(BigDecimal.ZERO) < 0 ) {
      amount = amount.negate();
    }
    debtFrom = new Debt(from, to, amount);
    List<Debt> debtListFrom = this.debtFrom.get(from);
    List<Debt> debListTo = this.debtTo.get(to);

    if (debtListFrom == null) {
      debtListFrom = new ArrayList<>();
    }

    if (debListTo == null) {
      debListTo = new ArrayList<>();
    }

    debtListFrom.add(debtFrom);
    debListTo.add(debtFrom);

    this.debtFrom.put(from, debtListFrom);
    this.debtTo.put(to, debListTo);
  }
}
