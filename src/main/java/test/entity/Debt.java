package test.entity;

import java.math.BigDecimal;

public class Debt {
  private String from;
  private String to;
  private BigDecimal amount;

  public Debt(String from, String to, BigDecimal amount) {
    this.from = from;
    this.to = to;
    this.amount = amount;
  }

  public String getFrom() {
    return this.from;
  }

  public String getTo() {
    return this.to;
  }

  public BigDecimal getAmount() {
    return this.amount;
  }

  public void increase(BigDecimal amount) {
    this.amount = this.amount.add(amount);
    if (this.amount.compareTo(BigDecimal.ZERO) < 0) {
      this.amount = BigDecimal.ZERO;
    }
  }
}
