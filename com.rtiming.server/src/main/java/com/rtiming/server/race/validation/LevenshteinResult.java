package com.rtiming.server.race.validation;

import java.util.List;

import com.rtiming.server.race.RaceControlBean;

/**
 * 
 */
public class LevenshteinResult {

  List<RaceControlBean> additionalControls;
  private final long raceStatusUid;

  public LevenshteinResult(List<RaceControlBean> additionalControls, long raceStatusUid) {
    super();
    this.additionalControls = additionalControls;
    this.raceStatusUid = raceStatusUid;
  }

  public List<RaceControlBean> getAdditionalControls() {
    return additionalControls;
  }

  public long getRaceStatusUid() {
    return raceStatusUid;
  }

}
