package com.rtiming.shared.common.database.sql;

import java.io.Serializable;

public class RaceBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long raceNr;
  private Long clientNr;
  private Long eventNr;
  private Long runnerNr;
  private Long entryNr;
  private Long eCardNr;
  private Long clubNr;
  private Long nationUid;
  private Long legClassUid;
  private Long legTime;
  private Long legStartTime;
  private String bibNo;
  private Long statusUid;
  private boolean manualStatus;

  private RunnerBean runner;
  private AddressBean address;

  public RaceBean() {
    runner = new RunnerBean();
    address = new AddressBean();
  }

  public Long getRaceNr() {
    return raceNr;
  }

  public void setRaceNr(Long raceNr) {
    this.raceNr = raceNr;
  }

  public Long getClientNr() {
    return clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  public Long getEventNr() {
    return eventNr;
  }

  public void setEventNr(Long eventNr) {
    this.eventNr = eventNr;
  }

  public Long getRunnerNr() {
    return runnerNr;
  }

  public void setRunnerNr(Long runnerNr) {
    this.runnerNr = runnerNr;
  }

  public Long getEntryNr() {
    return entryNr;
  }

  public void setEntryNr(Long entryNr) {
    this.entryNr = entryNr;
  }

  public Long getECardNr() {
    return eCardNr;
  }

  public void setECardNr(Long ecardNr) {
    this.eCardNr = ecardNr;
  }

  public Long getClubNr() {
    return clubNr;
  }

  public void setClubNr(Long clubNr) {
    this.clubNr = clubNr;
  }

  public Long getNationUid() {
    return nationUid;
  }

  public void setNationUid(Long nationUid) {
    this.nationUid = nationUid;
  }

  public Long getLegClassUid() {
    return legClassUid;
  }

  public void setLegClassUid(Long legClassUid) {
    this.legClassUid = legClassUid;
  }

  public Long getLegTime() {
    return legTime;
  }

  public void setLegTime(Long legTime) {
    this.legTime = legTime;
  }

  public Long getLegStartTime() {
    return legStartTime;
  }

  public void setLegStartTime(Long legStartTime) {
    this.legStartTime = legStartTime;
  }

  public String getBibNo() {
    return bibNo;
  }

  public void setBibNo(String bibNo) {
    this.bibNo = bibNo;
  }

  public Long getStatusUid() {
    return statusUid;
  }

  public void setStatusUid(Long statusUid) {
    this.statusUid = statusUid;
  }

  public boolean isManualStatus() {
    return manualStatus;
  }

  public void setManualStatus(boolean manualStatus) {
    this.manualStatus = manualStatus;
  }

  public RunnerBean getRunner() {
    return runner;
  }

  public void setRunner(RunnerBean runner) {
    this.runner = runner;
  }

  public AddressBean getAddress() {
    return address;
  }

  public void setAddress(AddressBean address) {
    this.address = address;
  }

}
