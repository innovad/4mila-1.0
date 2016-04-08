package com.rtiming.client.result.split;

public class LegResult {
  private Long legUid;
  private Long legStatus;
  private Object[] resultRow;
  private Long legTime;

  private Long summaryTime;
  private String formattedSummaryTime;

  private Long timeBehind;
  private String formattedTimeBehind;
  private Double percentBehind;

  public void setResultRow(Object[] row, Long time, Long status) {
    this.resultRow = row;
    this.legStatus = status;
    this.legTime = time;
  }

  public Object[] getResultRow() {
    Object[] f = new Object[resultRow.length + 4];
    System.arraycopy(resultRow, 0, f, 1, resultRow.length); // shift array
    return f;
  }

  public Long getLegStatus() {
    return legStatus;
  }

  public Long getLegTime() {
    return legTime;
  }

  public void setLegUid(Long legUid) {
    this.legUid = legUid;
  }

  public Long getLegUid() {
    return legUid;
  }

  public Long getSummaryTime() {
    return summaryTime;
  }

  public void setSummaryTime(Long summaryTime) {
    this.summaryTime = summaryTime;
  }

  public String getFormattedSummaryTime() {
    return formattedSummaryTime;
  }

  public void setFormattedSummaryTime(String formattedTime) {
    this.formattedSummaryTime = formattedTime;
  }

  public Long getTimeBehind() {
    return timeBehind;
  }

  public void setTimeBehind(Long timeBehind) {
    this.timeBehind = timeBehind;
  }

  public Double getPercentBehind() {
    return percentBehind;
  }

  public void setPercentBehind(Double percentBehind) {
    this.percentBehind = percentBehind;
  }

  public void setFormattedTimeBehind(String formattedTimeBehind) {
    this.formattedTimeBehind = formattedTimeBehind;
  }

  public String getFormattedTimeBehind() {
    return formattedTimeBehind;
  }

}
