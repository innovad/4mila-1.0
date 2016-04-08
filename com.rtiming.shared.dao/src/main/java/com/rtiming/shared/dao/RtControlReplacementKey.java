package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the rt_control_replacement database table.
 */
@Embeddable
public class RtControlReplacementKey implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "control_nr")
  private Long controlNr;

  @Column(name = "replacement_control_nr")
  private Long replacementControlNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtControlReplacementKey() {
  }

  public Long getControlNr() {
    return this.controlNr;
  }

  public void setControlNr(Long controlNr) {
    this.controlNr = controlNr;
  }

  public Long getReplacementControlNr() {
    return this.replacementControlNr;
  }

  public void setReplacementControlNr(Long replacementControlNr) {
    this.replacementControlNr = replacementControlNr;
  }

  public Long getClientNr() {
    return clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((clientNr == null) ? 0 : clientNr.hashCode());
    result = prime * result + ((controlNr == null) ? 0 : controlNr.hashCode());
    result = prime * result + ((replacementControlNr == null) ? 0 : replacementControlNr.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof RtControlReplacementKey)) {
      return false;
    }
    RtControlReplacementKey other = (RtControlReplacementKey) obj;
    if (clientNr == null) {
      if (other.clientNr != null) {
        return false;
      }
    }
    else if (!clientNr.equals(other.clientNr)) {
      return false;
    }
    if (controlNr == null) {
      if (other.controlNr != null) {
        return false;
      }
    }
    else if (!controlNr.equals(other.controlNr)) {
      return false;
    }
    if (replacementControlNr == null) {
      if (other.replacementControlNr != null) {
        return false;
      }
    }
    else if (!replacementControlNr.equals(other.replacementControlNr)) {
      return false;
    }
    return true;
  }

}
