package com.rtiming.server.ecard.download;

public class ClazzMatchResult {

  private final ClazzMatchCandidate candidate;
  private final Long controlCount;

  public ClazzMatchResult(ClazzMatchCandidate candidate, Long controlCount) {
    super();
    this.candidate = candidate;
    this.controlCount = controlCount;
  }

  public ClazzMatchCandidate getCandidate() {
    return candidate;
  }

  public Long getControlCount() {
    return controlCount;
  }

}
