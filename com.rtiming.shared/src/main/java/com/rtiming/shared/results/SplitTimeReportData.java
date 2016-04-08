package com.rtiming.shared.results;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 
 */
public class SplitTimeReportData implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long eventNr;
  private Map<String, String> parameters;
  private Date eventDate;

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public Long getEventNr() {
    return eventNr;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public Date getEventDate() {
    return eventDate;
  }

  public void setEventDate(Date eventDate) {
    this.eventDate = eventDate;
  }

}
