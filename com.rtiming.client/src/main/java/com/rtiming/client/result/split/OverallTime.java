package com.rtiming.client.result.split;

public class OverallTime implements Comparable<OverallTime> {

  private String overallTime;
  private Long overallTimeRaw;
  private Long overallRank;

  public String getOverallTime() {
    return overallTime;
  }

  public void setOverallTime(String overallTime) {
    this.overallTime = overallTime;
  }

  public Long getOverallTimeRaw() {
    return overallTimeRaw;
  }

  public void setOverallTimeRaw(Long overallTimeRaw) {
    this.overallTimeRaw = overallTimeRaw;
  }

  public Long getOverallRank() {
    return overallRank;
  }

  public void setOverallRank(Long overallRank) {
    this.overallRank = overallRank;
  }

  @Override
  public int compareTo(OverallTime other) {
    if (other == null) {
      return -1;
    }
    if (getOverallTimeRaw() == null) {
      return 1;
    }
    if (other.getOverallTimeRaw() == null) {
      return -1;
    }
    return getOverallTimeRaw().compareTo(other.getOverallTimeRaw());
  }

}
