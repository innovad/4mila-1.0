package com.rtiming.server.race.validation;


/**
 * 
 */
public class FreeOrderResult {

  private final long raceStatusUid;
  Long freeOrderSortCode;

  /**
   * @param raceStatusUid
   * @param freeOrderSortCodes
   */
  public FreeOrderResult(long raceStatusUid, Long freeOrderSortCode) {
    super();
    this.raceStatusUid = raceStatusUid;
    this.freeOrderSortCode = freeOrderSortCode;
  }

  public Long getFreeOrderSortCode() {
    return freeOrderSortCode;
  }

  public long getRaceStatusUid() {
    return raceStatusUid;
  }

}
