package com.rtiming.shared.common.database.sql;

import java.io.Serializable;
import java.util.Date;

public class PaymentBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long paymentNr;
  private Long clientNr;
  private String paymentNo;
  private Long registrationNr;
  private Date evtPayment;
  private Double amount;
  private Long currencyUid;
  private Long typeUid;

  public Long getPaymentNr() {
    return paymentNr;
  }

  public void setPaymentNr(Long paymentNr) {
    this.paymentNr = paymentNr;
  }

  public Long getClientNr() {
    return clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  public String getPaymentNo() {
    return paymentNo;
  }

  public void setPaymentNo(String paymentNo) {
    this.paymentNo = paymentNo;
  }

  public Long getRegistrationNr() {
    return registrationNr;
  }

  public void setRegistrationNr(Long registrationNr) {
    this.registrationNr = registrationNr;
  }

  public Date getEvtPayment() {
    return evtPayment;
  }

  public void setEvtPayment(Date evtPayment) {
    this.evtPayment = evtPayment;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Long getCurrencyUid() {
    return currencyUid;
  }

  public void setCurrencyUid(Long currencyUid) {
    this.currencyUid = currencyUid;
  }

  public Long getTypeUid() {
    return typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

}
