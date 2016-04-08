package com.rtiming.shared.common.database.sql;

import java.io.Serializable;
import java.util.Date;

import com.rtiming.shared.common.EntityCodeType;

public class RunnerBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long runnerNr;
  private Long clientNr;
  private Long accountNr;
  private String firstName;
  private String lastName;
  private Date evtBirth;
  private String extKey;
  private Long clubNr;
  private Long eCardNr;
  private boolean active;
  private Long nationUid;
  private Long year;
  private Long defaultClassUid;
  private Long sexUid;

  private AddressBean address;
  private AdditionalInformationBean addInfo;

  public RunnerBean() {
    addInfo = new AdditionalInformationBean();
    addInfo.setEntityUid(EntityCodeType.RunnerCode.ID);
    address = new AddressBean();
  }

  public Long getRunnerNr() {
    return runnerNr;
  }

  public void setRunnerNr(Long runnerNr) {
    this.runnerNr = runnerNr;
  }

  public Long getClientNr() {
    return clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  public Long getAccountNr() {
    return accountNr;
  }

  public void setAccountNr(Long accountNr) {
    this.accountNr = accountNr;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Date getEvtBirth() {
    return evtBirth;
  }

  public void setEvtBirth(Date evtBirth) {
    this.evtBirth = evtBirth;
  }

  public String getExtKey() {
    return extKey;
  }

  public void setExtKey(String extKey) {
    this.extKey = extKey;
  }

  public Long getClubNr() {
    return clubNr;
  }

  public void setClubNr(Long clubNr) {
    this.clubNr = clubNr;
  }

  public Long getECardNr() {
    return eCardNr;
  }

  public void setECardNr(Long nr) {
    this.eCardNr = nr;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Long getNationUid() {
    return nationUid;
  }

  public void setNationUid(Long nationUid) {
    this.nationUid = nationUid;
  }

  public Long getYear() {
    return year;
  }

  public void setYear(Long year) {
    this.year = year;
  }

  public Long getDefaultClassUid() {
    return defaultClassUid;
  }

  public void setDefaultClassUid(Long defaultClassUid) {
    this.defaultClassUid = defaultClassUid;
  }

  public Long getSexUid() {
    return sexUid;
  }

  public void setSexUid(Long sexUid) {
    this.sexUid = sexUid;
  }

  public AddressBean getAddress() {
    return address;
  }

  public void setAddress(AddressBean address) {
    this.address = address;
  }

  public AdditionalInformationBean getAddInfo() {
    return addInfo;
  }

  public void setAddInfo(AdditionalInformationBean addInfo) {
    this.addInfo = addInfo;
  }

  @Override
  public String toString() {
    return "RunnerBean [runnerNr=" + runnerNr + ", clientNr=" + clientNr + ", accountNr=" + accountNr + ", firstName=" + firstName + ", lastName=" + lastName + ", evtBirth=" + evtBirth + ", extKey=" + extKey + ", clubNr=" + clubNr + ", eCardNr=" + eCardNr + ", active=" + active + ", nationUid=" + nationUid + ", year=" + year + ", defaultClassUid=" + defaultClassUid + ", sexUid=" + sexUid + ", address=" + address + ", addInfo=" + addInfo + "]";
  }

}
