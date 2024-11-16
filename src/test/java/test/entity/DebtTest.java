package test.entity;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class DebtTest {
  
  @Test
  public void getFromTest() {
    String exp = "someone";
    Debt debt = new Debt(exp, "otherone", BigDecimal.TEN);
    assertEquals(exp, debt.getFrom());
  }

  @Test
  public void getToTest() {
    String exp = "someone";
    Debt debt = new Debt("otherone", exp, BigDecimal.TEN);
    assertEquals(exp, debt.getTo());
  }

  @Test
  public void getAmountTest() {
    BigDecimal exp = BigDecimal.TEN;
    Debt debt = new Debt("otherone", "someone", exp);
    assertEquals(exp, debt.getAmount());
  }

  @Test
  public void increasePositiveTest() {
    Debt debt = new Debt("otherone", "someone", BigDecimal.ZERO);
    debt.increase(BigDecimal.TEN);
    assertEquals(BigDecimal.TEN, debt.getAmount());
  }
}
