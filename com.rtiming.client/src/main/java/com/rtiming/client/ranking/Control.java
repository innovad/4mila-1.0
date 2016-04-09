package com.rtiming.client.ranking;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

public class Control {

  private Long statusUid;
  private Long overallTime;
  private Long legTime;
  private Long typeUid;
  private String controlNo;

  public Long getStatusUid() {
    return statusUid;
  }

  public String getStatus() throws ProcessingException {
    return RankingUtility.controlStatus2ExtKey(statusUid);
  }

  public void setStatus(Object status) throws ProcessingException {
    statusUid = RankingUtility.extKey2ControlStatus(status);
  }

  public void setStatusUid(Long statusUid) {
    this.statusUid = statusUid;
  }

  public void setOverallTime(Long overallTime) {
    this.overallTime = overallTime;
  }

  public Long getOverallTime() {
    return overallTime;
  }

  public void setLegTime(Long legTime) {
    this.legTime = legTime;
  }

  public Long getLegTime() {
    return legTime;
  }

  public Long getTypeUid() {
    return typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

  public String getControlNo() {
    return controlNo;
  }

  public void setControlNo(String controlNo) {
    this.controlNo = controlNo;
  }

  @Override
  public String toString() {
    return "Control [statusUid=" + statusUid + ", typeUid=" + typeUid + ", overallTime=" + overallTime + ", legTime=" + legTime + "]";
  }

}
