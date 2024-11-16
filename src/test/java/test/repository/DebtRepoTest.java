package test.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import test.entity.Debt;

public class DebtRepoTest {
  @Test
  public void findByFromNotFoundTest() {
    DebtRepo repo = new DebtRepo();
    List<Debt> debt = repo.findByFrom("someone");
    assertTrue(debt.isEmpty());
  }

  @Test
  public void findByToNotFoundTest() {
    DebtRepo repo = new DebtRepo();
    List<Debt> debt = repo.findByTo("someone");
    assertTrue(debt.isEmpty());
  }

  @Test
  public void insertNewDebtTest() {
    DebtRepo repo = new DebtRepo();
    repo.insert("one", "two", BigDecimal.TEN);
    Debt debt = repo.findByFromAndTo("one", "two");
    assertEquals(BigDecimal.TEN, debt.getAmount());
  }

  @Test
  public void insertOldDebtTest() {
    DebtRepo repo = new DebtRepo();
    repo.insert("one", "two", BigDecimal.TEN);
    repo.insert("one", "two", BigDecimal.TEN);
    Debt debt = repo.findByFromAndTo("one", "two");
    assertEquals(BigDecimal.valueOf(20), debt.getAmount());
  }
}
