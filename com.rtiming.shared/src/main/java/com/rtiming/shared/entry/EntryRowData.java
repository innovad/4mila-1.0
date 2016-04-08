package com.rtiming.shared.entry;

import java.io.Serializable;
import java.util.Date;

public class EntryRowData implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long entryNr;
  private Long runnerNr;
  private Long raceNr;
  private Long raceNrOrder;
  private Long punchingSystemUid;
  private String bibNumber;
  private String registrationNo;
  private Date entryTime;
  private String extKey;
  private String eCard;
  private boolean rentalCard;
  private String runner;
  private String lastName;
  private String firstName;
  private String nationCountryCode;
  private Long sex;
  private Date birthdate;
  private Long year;
  private String legClassShortcut;
  private String legClassName;
  private Long legClassUid;
  private String courseShortcut;
  private String street;
  private String zip;
  private String city;
  private Long area;
  private String region;
  private Long country;
  private String phone;
  private String fax;
  private String mobilePhone;
  private String eMail;
  private String URL;
  private String nation;
  private String clubShortcut;
  private String clubExtKey;
  private String club;
  private String event;
  private boolean startList;
  private String startblock;
  private Long startlistSettingOption;
  private Long relativeStartTime;
  private Date startTime;
  private Object[] entryAdditionalValues;
  private Object[] runnerAdditionalValues;

  public Long getEntryNr() {
    return entryNr;
  }

  public void setEntryNr(Long entryNr) {
    this.entryNr = entryNr;
  }

  public Long getRaceNr() {
    return raceNr;
  }

  public void setRaceNr(Long raceNr) {
    this.raceNr = raceNr;
  }

  public Long getRaceNrOrder() {
    return raceNrOrder;
  }

  public void setRaceNrOrder(Long raceNrOrder) {
    this.raceNrOrder = raceNrOrder;
  }

  public Long getPunchingSystemUid() {
    return punchingSystemUid;
  }

  public void setPunchingSystemUid(Long punchingSystemUid) {
    this.punchingSystemUid = punchingSystemUid;
  }

  public String getBibNumber() {
    return bibNumber;
  }

  public void setBibNumber(String bibNumber) {
    this.bibNumber = bibNumber;
  }

  public String getRegistrationNo() {
    return registrationNo;
  }

  public void setRegistrationNo(String registrationNo) {
    this.registrationNo = registrationNo;
  }

  public Date getEntryTime() {
    return entryTime;
  }

  public void setEntryTime(Date entryTime) {
    this.entryTime = entryTime;
  }

  public String getExtKey() {
    return extKey;
  }

  public void setExtKey(String extKey) {
    this.extKey = extKey;
  }

  public String getECard() {
    return eCard;
  }

  public void setECard(String eCard) {
    this.eCard = eCard;
  }

  public boolean isRentalCard() {
    return rentalCard;
  }

  public void setRentalCard(boolean rentalCard) {
    this.rentalCard = rentalCard;
  }

  public String getRunner() {
    return runner;
  }

  public void setRunner(String runner) {
    this.runner = runner;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getNationCountryCode() {
    return nationCountryCode;
  }

  public void setNationCountryCode(String nationCountryCode) {
    this.nationCountryCode = nationCountryCode;
  }

  public Long getSex() {
    return sex;
  }

  public void setSex(Long sex) {
    this.sex = sex;
  }

  public Date getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(Date birthdate) {
    this.birthdate = birthdate;
  }

  public Long getYear() {
    return year;
  }

  public void setYear(Long year) {
    this.year = year;
  }

  public String getLegClassShortcut() {
    return legClassShortcut;
  }

  public void setLegClassShortcut(String legClassShortcut) {
    this.legClassShortcut = legClassShortcut;
  }

  public String getLegClassName() {
    return legClassName;
  }

  public void setLegClassName(String legClassName) {
    this.legClassName = legClassName;
  }

  public Long getLegClassUid() {
    return legClassUid;
  }

  public void setLegClassUid(Long classUid) {
    this.legClassUid = classUid;
  }

  public String getCourseShortcut() {
    return courseShortcut;
  }

  public void setCourseShortcut(String courseShortcut) {
    this.courseShortcut = courseShortcut;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Long getArea() {
    return area;
  }

  public void setArea(Long area) {
    this.area = area;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public Long getCountry() {
    return country;
  }

  public void setCountry(Long country) {
    this.country = country;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getEMail() {
    return eMail;
  }

  public void setEMail(String eMail) {
    this.eMail = eMail;
  }

  public String getURL() {
    return URL;
  }

  public void setURL(String uRL) {
    URL = uRL;
  }

  public String getNation() {
    return nation;
  }

  public void setNation(String nation) {
    this.nation = nation;
  }

  public String getClubShortcut() {
    return clubShortcut;
  }

  public void setClubShortcut(String clubShortcut) {
    this.clubShortcut = clubShortcut;
  }

  public String getClubExtKey() {
    return clubExtKey;
  }

  public void setClubExtKey(String clubExtKey) {
    this.clubExtKey = clubExtKey;
  }

  public String getClub() {
    return club;
  }

  public void setClub(String club) {
    this.club = club;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public boolean isStartList() {
    return startList;
  }

  public void setStartList(boolean startList) {
    this.startList = startList;
  }

  public String getStartblock() {
    return startblock;
  }

  public void setStartblock(String startblock) {
    this.startblock = startblock;
  }

  public Long getStartlistSettingOption() {
    return startlistSettingOption;
  }

  public void setStartlistSettingOption(Long startlistSettingOption) {
    this.startlistSettingOption = startlistSettingOption;
  }

  public Long getRelativeStartTime() {
    return relativeStartTime;
  }

  public void setRelativeStartTime(Long relativeStartTime) {
    this.relativeStartTime = relativeStartTime;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Long getRunnerNr() {
    return runnerNr;
  }

  public void setRunnerNr(Long runnerNr) {
    this.runnerNr = runnerNr;
  }

  public Object[] getEntryAdditionalValues() {
    return entryAdditionalValues;
  }

  public void setEntryAdditionalValues(Object[] entryAdditionalValues) {
    this.entryAdditionalValues = entryAdditionalValues;
  }

  public Object[] getRunnerAdditionalValues() {
    return runnerAdditionalValues;
  }

  public void setRunnerAdditionalValues(Object[] runnerAdditionalValues) {
    this.runnerAdditionalValues = runnerAdditionalValues;
  }

}
