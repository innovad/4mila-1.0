package com.rtiming.client.result.split;

public class LegTime implements Comparable<LegTime> {

  public LegTime() {
  }

  private String legTime;
  private Long legTimeRaw;
  private Long legRank;
  private Long timeBehind;

  public void setLegTime(String time) {
    this.legTime = time;
  }

  public String getLegTime() {
    return legTime;
  }

  public Long getLegTimeRaw() {
    return legTimeRaw;
  }

  public void setLegTimeRaw(Long legTimeRaw) {
    this.legTimeRaw = legTimeRaw;
  }

  public void setLegRank(Long legRank) {
    this.legRank = legRank;
  }

  public Long getLegRank() {
    return legRank;
  }

  @Override
  public String toString() {
    return "[" + legTime + "]";
  }

  public Long getTimeBehind() {
    return timeBehind;
  }

  public void setTimeBehind(Long timeBehind) {
    this.timeBehind = timeBehind;
  }

  @Override
  public int compareTo(LegTime other) {
    if (other == null) {
      return -1;
    }
    if (getLegTimeRaw() == null) {
      return 1;
    }
    if (other.getLegTimeRaw() == null) {
      return -1;
    }
    return getLegTimeRaw().compareTo(other.getLegTimeRaw());
  }

}
