package com.rtiming.client.test.data;

public class ControlTestData {

  private String controlNo;
  private Long sortCode;
  private Long controlTypeUid;
  private String loopMasterCourseControlNo;
  private Long loopMasterSortCode;
  private String loopVariantCode;
  private Long loopTypeUid;

  public ControlTestData(String controlNo, Long sortCode, Long controlTypeUid, Long loopTypeUid, String loopMasterCourseControlNo, Long loopMasterSortCode, String loopVariantCode) {
    super();
    this.controlNo = controlNo;
    this.sortCode = sortCode;
    this.controlTypeUid = controlTypeUid;
    this.loopTypeUid = loopTypeUid;
    this.loopMasterCourseControlNo = loopMasterCourseControlNo;
    this.loopMasterSortCode = loopMasterSortCode;
    this.loopVariantCode = loopVariantCode;
  }

  public String getControlNo() {
    return controlNo;
  }

  public Long getControlTypeUid() {
    return controlTypeUid;
  }

  public Long getSortCode() {
    return sortCode;
  }

  public String getLoopMasterCourseControlNo() {
    return loopMasterCourseControlNo;
  }

  public Long getLoopMasterSortCode() {
    return loopMasterSortCode;
  }

  public String getLoopVariantCode() {
    return loopVariantCode;
  }

  public Long getLoopTypeUid() {
    return loopTypeUid;
  }

}
