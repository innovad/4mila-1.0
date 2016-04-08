package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_payment database table.
 */
@Embeddable
public class RtPaymentKey extends AbstractKey<RtPaymentKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "payment_nr")
  private Long paymentNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtPaymentKey() {
  }

  @Override
  public Long getId() {
    return this.paymentNr;
  }

  @Override
  public void setId(Long paymentNr) {
    this.paymentNr = paymentNr;
  }

  @Override
  public Long getClientNr() {
    return this.clientNr;
  }

  @Override
  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof RtPaymentKey)) {
      return false;
    }
    RtPaymentKey castOther = (RtPaymentKey) other;
    return this.paymentNr.equals(castOther.paymentNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.paymentNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtPaymentKey create(RtPaymentKey key) {
    if (key == null) {
      key = new RtPaymentKey();
    }
    return (RtPaymentKey) createKeyInternal(key);
  }

  public static RtPaymentKey create(Long id) {
    RtPaymentKey key = new RtPaymentKey();
    key.setId(id);
    return create(key);
  }

}
