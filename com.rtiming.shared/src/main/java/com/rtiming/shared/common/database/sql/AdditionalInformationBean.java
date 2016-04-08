package com.rtiming.shared.common.database.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdditionalInformationBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long entityUid;
  private Long joinNr;
  private Long clientNr;

  private final List<AdditionalInformationValueBean> values;

  public AdditionalInformationBean() {
    values = new ArrayList<AdditionalInformationValueBean>();
  }

  public Long getEntityUid() {
    return entityUid;
  }

  public void setEntityUid(Long entityUid) {
    this.entityUid = entityUid;
  }

  public Long getJoinNr() {
    return joinNr;
  }

  public void setJoinNr(Long joinNr) {
    this.joinNr = joinNr;
  }

  public Long getClientNr() {
    return clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  public List<AdditionalInformationValueBean> getValues() {
    return values;
  }

  public void addValue(AdditionalInformationValueBean value) {
    values.add(value);
  }

  @Override
  public String toString() {
    return "AdditionalInformationBean [entityUid=" + entityUid + ", joinNr=" + joinNr + ", clientNr=" + clientNr + ", values=" + values + "]";
  }

}
