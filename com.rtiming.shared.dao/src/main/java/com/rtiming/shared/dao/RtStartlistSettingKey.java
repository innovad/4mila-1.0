package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_startlist_setting database table.
 */
@Embeddable
public class RtStartlistSettingKey extends AbstractKey<RtStartlistSettingKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "startlist_setting_nr")
  private Long startlistSettingNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtStartlistSettingKey() {
  }

  @Override
  public Long getId() {
    return this.startlistSettingNr;
  }

  @Override
  public void setId(Long startlistSettingNr) {
    this.startlistSettingNr = startlistSettingNr;
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
    if (!(other instanceof RtStartlistSettingKey)) {
      return false;
    }
    RtStartlistSettingKey castOther = (RtStartlistSettingKey) other;
    return this.startlistSettingNr.equals(castOther.startlistSettingNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.startlistSettingNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtStartlistSettingKey create(RtStartlistSettingKey key) {
    if (key == null) {
      key = new RtStartlistSettingKey();
    }
    return (RtStartlistSettingKey) createKeyInternal(key);
  }

  public static RtStartlistSettingKey create(Long id) {
    RtStartlistSettingKey key = new RtStartlistSettingKey();
    key.setId(id);
    return create(key);
  }

}
