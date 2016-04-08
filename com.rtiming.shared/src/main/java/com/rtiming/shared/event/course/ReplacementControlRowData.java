package com.rtiming.shared.event.course;

import java.io.Serializable;

public class ReplacementControlRowData implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long controlNr;
  private Long replacementControlNr;
  private String controlNo;
  private String replacementControlNo;

  public Long getControlNr() {
    return controlNr;
  }

  public void setControlNr(Long controlNr) {
    this.controlNr = controlNr;
  }

  public Long getReplacementControlNr() {
    return replacementControlNr;
  }

  public void setReplacementControlNr(Long replacementControlNr) {
    this.replacementControlNr = replacementControlNr;
  }

  public String getControlNo() {
    return controlNo;
  }

  public void setControlNo(String controlNo) {
    this.controlNo = controlNo;
  }

  public String getReplacementControlNo() {
    return replacementControlNo;
  }

  public void setReplacementControlNo(String replacementControlNo) {
    this.replacementControlNo = replacementControlNo;
  }

}
