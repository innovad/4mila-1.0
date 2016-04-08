package com.rtiming.server.race;

import java.util.ArrayList;
import java.util.List;

public class RaceControlBean {

  private Long raceControlNr;
  private Long courseControlNr;
  private Long controlNr;
  private Long sortcode;
  private String controlNo;
  private Long punchTime; // relative to evtZero in ms
  private Long typeUid;
  private Long shiftTime; // absolute in ms
  private boolean countLeg;
  private boolean mandatory;
  private boolean manualStatus;

  private Long overallTime; // relative to legstarttime in ms
  private Long legTime; // absolute in ms
  private Long controlStatusUid;

  private String replacementControls;
  private final List<String> replacementControlNos;

  public RaceControlBean() {
    replacementControlNos = new ArrayList<String>();
  }

  public Long getRaceControlNr() {
    return raceControlNr;
  }

  public void setRaceControlNr(Long raceControlNr) {
    this.raceControlNr = raceControlNr;
  }

  public Long getSortcode() {
    return sortcode;
  }

  public void setSortcode(Long sortcode) {
    this.sortcode = sortcode;
  }

  public String getControlNo() {
    return controlNo;
  }

  public void setControlNo(String controlNo) {
    this.controlNo = controlNo;
  }

  public Long getPunchTime() {
    return punchTime;
  }

  public void setPunchTime(Long time) {
    this.punchTime = time;
  }

  public Long getOverallTime() {
    return overallTime;
  }

  public void setOverallTime(Long overallTime) {
    this.overallTime = overallTime;
  }

  public Long getLegTime() {
    return legTime;
  }

  public void setLegTime(Long legTime) {
    this.legTime = legTime;
  }

  public Long getControlStatusUid() {
    return controlStatusUid;
  }

  public void setControlStatusUid(Long controlStatusUid) {
    this.controlStatusUid = controlStatusUid;
  }

  public List<String> getReplacementControlNos() {
    return replacementControlNos;
  }

  public void setReplacementControlsByList(List<String> replacementControls) {
    this.replacementControlNos.clear();
    if (replacementControls != null) {
      this.replacementControlNos.addAll(replacementControls);
    }
  }

  public String getReplacementControls() {
    return replacementControls;
  }

  public void setCountLeg(boolean countLeg) {
    this.countLeg = countLeg;
  }

  public boolean isCountLeg() {
    return countLeg;
  }

  public Long getTypeUid() {
    return typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

  public Long getShiftTime() {
    return shiftTime;
  }

  public void setShiftTime(Long shiftTime) {
    this.shiftTime = shiftTime;
  }

  public boolean isManualStatus() {
    return manualStatus;
  }

  public void setManualStatus(boolean manualStatus) {
    this.manualStatus = manualStatus;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((controlNo == null) ? 0 : controlNo.hashCode());
    result = prime * result + ((raceControlNr == null) ? 0 : raceControlNr.hashCode());
    result = prime * result + ((sortcode == null) ? 0 : sortcode.hashCode());
    result = prime * result + ((punchTime == null) ? 0 : punchTime.hashCode());
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
    if (getClass() != obj.getClass()) {
      return false;
    }
    RaceControlBean other = (RaceControlBean) obj;
    if (controlNo == null) {
      if (other.controlNo != null) {
        return false;
      }
    }
    else if (!controlNo.equals(other.controlNo)) {
      return false;
    }
    if (raceControlNr == null) {
      if (other.raceControlNr != null) {
        return false;
      }
    }
    else if (!raceControlNr.equals(other.raceControlNr)) {
      return false;
    }
    if (sortcode == null) {
      if (other.sortcode != null) {
        return false;
      }
    }
    else if (!sortcode.equals(other.sortcode)) {
      return false;
    }
    if (punchTime == null) {
      if (other.punchTime != null) {
        return false;
      }
    }
    else if (!punchTime.equals(other.punchTime)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RaceControlBean [sortcode=" + sortcode + ", controlNo=" + controlNo + ", replacementControls=" + replacementControls + ", controlStatusUid = " + controlStatusUid + "]";
  }

}
