package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_runner database table.
 */
@Embeddable
public class RtRunnerKey extends AbstractKey<RtRunnerKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "runner_nr")
  private Long runnerNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtRunnerKey() {
  }

  @Override
  public Long getId() {
    return this.runnerNr;
  }

  @Override
  public void setId(Long runnerNr) {
    this.runnerNr = runnerNr;
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
    if (!(other instanceof RtRunnerKey)) {
      return false;
    }
    RtRunnerKey castOther = (RtRunnerKey) other;
    return this.runnerNr.equals(castOther.runnerNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.runnerNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtRunnerKey create(RtRunnerKey key) {
    if (key == null) {
      key = new RtRunnerKey();
    }
    return (RtRunnerKey) createKeyInternal(key);
  }

  public static RtRunnerKey create(Long id) {
    RtRunnerKey key = new RtRunnerKey();
    key.setId(id);
    return create(key);
  }

}
