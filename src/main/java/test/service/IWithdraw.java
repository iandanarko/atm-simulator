package test.service;

import java.math.BigDecimal;

public interface IWithdraw {
  void withdraw(BigDecimal amount) throws Exception;
}
