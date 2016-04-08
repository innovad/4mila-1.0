package com.rtiming.shared.common.database.sql;

import java.io.Serializable;

/**
 *
 */
public class FeeBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long currencyUid;
  private Double amount;
  private boolean cashPaymentOnRegistration;

  public Long getCurrencyUid() {
    return currencyUid;
  }

  public void setCurrencyUid(Long currencyUid) {
    this.currencyUid = currencyUid;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public boolean isCashPaymentOnRegistration() {
    return cashPaymentOnRegistration;
  }

  public void setCashPaymentOnRegistration(boolean cashPaymentOnRegistration) {
    this.cashPaymentOnRegistration = cashPaymentOnRegistration;
  }

}
