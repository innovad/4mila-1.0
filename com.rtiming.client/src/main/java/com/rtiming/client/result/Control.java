package com.rtiming.client.result;

/**
 * 
 */
public class Control {

  private final String controlNo;
  private final Long counter;
  private final Long statusUid;
  private final Long typeUid;
  private final Long courseControlNr;

  public Control(String controlNo, Long counter, Long statusUid, Long typeUid, Long courseControlNr) {
    super();
    this.controlNo = controlNo;
    this.counter = counter;
    this.statusUid = statusUid;
    this.typeUid = typeUid;
    this.courseControlNr = courseControlNr;
  }

  public String getControlNo() {
    return controlNo;
  }

  public Long getStatusUid() {
    return statusUid;
  }

  public Long getTypeUid() {
    return typeUid;
  }

  public Long getCourseControlNr() {
    return courseControlNr;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((controlNo == null) ? 0 : controlNo.hashCode());
    result = prime * result + ((counter == null) ? 0 : counter.hashCode());
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
    if (!(obj instanceof Control)) {
      return false;
    }
    Control other = (Control) obj;
    if (controlNo == null) {
      if (other.controlNo != null) {
        return false;
      }
    }
    else if (!controlNo.equals(other.controlNo)) {
      return false;
    }
    if (counter == null) {
      if (other.counter != null) {
        return false;
      }
    }
    else if (!counter.equals(other.counter)) {
      return false;
    }
    return true;
  }

}
