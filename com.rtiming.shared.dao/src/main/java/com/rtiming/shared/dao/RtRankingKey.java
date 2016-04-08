package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_ranking database table.
 */
@Embeddable
public class RtRankingKey extends AbstractKey<RtRankingKey> implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "ranking_nr")
  private Long rankingNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtRankingKey() {
  }

  @Override
  public Long getId() {
    return this.rankingNr;
  }

  @Override
  public void setId(Long rankingNr) {
    this.rankingNr = rankingNr;
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
    if (!(other instanceof RtRankingKey)) {
      return false;
    }
    RtRankingKey castOther = (RtRankingKey) other;
    return this.rankingNr.equals(castOther.rankingNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.rankingNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtRankingKey create(RtRankingKey key) {
    if (key == null) {
      key = new RtRankingKey();
    }
    return (RtRankingKey) createKeyInternal(key);
  }

  public static RtRankingKey create(Long id) {
    RtRankingKey key = new RtRankingKey();
    key.setId(id);
    return create(key);
  }

}
