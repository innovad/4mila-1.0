package com.rtiming.shared.event.course;

import java.io.Serializable;

public class CourseControlRowData implements Serializable, Cloneable {

  private static final long serialVersionUID = 1L;

  private Long courseControlNr;
  private Long controlNr;
  private String controlNo;
  private Long forkMasterCourseControlNr;
  private String forkMasterCourseControlNo;
  private String forkVariantCode;
  private Long forkTypeUid;
  private Long typeUid;
  private Long sortCode;
  private boolean countLeg;
  private boolean mandatory;

  public Long getCourseControlNr() {
    return courseControlNr;
  }

  public void setCourseControlNr(Long courseControlNr) {
    this.courseControlNr = courseControlNr;
  }

  public Long getControlNr() {
    return controlNr;
  }

  public void setControlNr(Long controlNr) {
    this.controlNr = controlNr;
  }

  public String getControlNo() {
    return controlNo;
  }

  public void setControlNo(String controlNo) {
    this.controlNo = controlNo;
  }

  public Long getForkMasterCourseControlNr() {
    return forkMasterCourseControlNr;
  }

  public void setForkMasterCourseControlNr(Long forkMasterCourseControlNr) {
    this.forkMasterCourseControlNr = forkMasterCourseControlNr;
  }

  public String getForkMasterCourseControlNo() {
    return forkMasterCourseControlNo;
  }

  public void setForkMasterCourseControlNo(String forkMasterCourseControlNo) {
    this.forkMasterCourseControlNo = forkMasterCourseControlNo;
  }

  public String getForkVariantCode() {
    return forkVariantCode;
  }

  public void setForkVariantCode(String forkVariantCode) {
    this.forkVariantCode = forkVariantCode;
  }

  public Long getTypeUid() {
    return typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

  public Long getSortCode() {
    return sortCode;
  }

  public void setSortCode(Long sortCode) {
    this.sortCode = sortCode;
  }

  public boolean isCountLeg() {
    return countLeg;
  }

  public void setCountLeg(boolean countLeg) {
    this.countLeg = countLeg;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public Long getLoopTypeUid() {
    return forkTypeUid;
  }

  public void setForkTypeUid(Long forkTypeUid) {
    this.forkTypeUid = forkTypeUid;
  }

  @Override
  public String toString() {
    return "CourseControlRowData [controlNr=" + controlNr + ", controlNo=" + controlNo + ", sortCode=" + sortCode + "]";
  }

  @Override
  public CourseControlRowData clone() {
    CourseControlRowData copy = new CourseControlRowData();
    copy.setCourseControlNr(courseControlNr);
    copy.setControlNo(controlNo);
    copy.setControlNr(controlNr);
    copy.setCountLeg(countLeg);
    copy.setForkMasterCourseControlNo(forkMasterCourseControlNo);
    copy.setForkMasterCourseControlNr(forkMasterCourseControlNr);
    copy.setForkVariantCode(forkVariantCode);
    copy.setForkTypeUid(forkTypeUid);
    copy.setMandatory(mandatory);
    copy.setSortCode(sortCode);
    copy.setTypeUid(typeUid);
    return copy;
  }

  public void removeVariantInformation() {
    this.setForkMasterCourseControlNo(null);
    this.setForkMasterCourseControlNr(null);
    this.setForkVariantCode(null);
  }

}
