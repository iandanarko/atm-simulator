package test.service;

import java.math.BigDecimal;

public interface ITransfer {
  void transfer(String to, BigDecimal amount) throws Exception;
}
