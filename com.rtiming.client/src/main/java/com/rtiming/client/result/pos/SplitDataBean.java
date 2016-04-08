package com.rtiming.client.result.pos;

/**
 * 
 */
public class SplitDataBean {

  private String controlNo;
  private Long controlTypeUid;
  private Long sortCode;
  private Long controlStatusUid;

  private String legTime;
  private String relativeLegTime;

  public String getControlNo() {
    return controlNo;
  }

  public Long getControlTypeUid() {
    return controlTypeUid;
  }

  public Long getSortCode() {
    return sortCode;
  }

  public Long getControlStatusUid() {
    return controlStatusUid;
  }

  public String getLegTime() {
    return legTime;
  }

  public String getRelativeLegTime() {
    return relativeLegTime;
  }

  public void setControlNo(String controlNo) {
    this.controlNo = controlNo;
  }

  public void setControlTypeUid(Long controlTypeUid) {
    this.controlTypeUid = controlTypeUid;
  }

  public void setSortCode(Long sortCode) {
    this.sortCode = sortCode;
  }

  public void setControlStatusUid(Long controlStatusUid) {
    this.controlStatusUid = controlStatusUid;
  }

  public void setLegTime(String legTime) {
    this.legTime = legTime;
  }

  public void setRelativeLegTime(String relativeLegTime) {
    this.relativeLegTime = relativeLegTime;
  }

}
