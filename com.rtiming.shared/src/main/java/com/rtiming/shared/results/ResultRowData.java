package com.rtiming.shared.results;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 */
public class ResultRowData implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long raceNr;
  private Long entryNr;
  private Long runnerNr;
  private Long rank;
  private Long classShortcut;
  private String className;
  private String courseShortcut;
  private String bibNumber;
  private String extKey;
  private String runner;
  private String lastName;
  private String firstName;
  private String eCard;
  private boolean rentalCard;
  private String clubShortcut;
  private String clubExtKey;
  private String club;
  private String nation;
  private String nationCountryCode;
  private Long sex;
  private Date birthdate;
  private Long year;
  private String street;
  private String zip;
  private String city;
  private Long area;
  private String region;
  private String country;
  private String phone;
  private String fax;
  private String mobilePhone;
  private String eMail;
  private String URL;
  private Long raceStatus;
  private Long legStartTime;
  private Long legTime;
  private String timeBehind;
  private Double percent;
  private String time;
  private Object[] entryAdditionalValues;
  private Object[] runnerAdditionalValues;

  public Long getRaceNr() {
    return raceNr;
  }

  public void setRaceNr(Long raceNr) {
    this.raceNr = raceNr;
  }

  public Long getEntryNr() {
    return entryNr;
  }

  public void setEntryNr(Long entryNr) {
    this.entryNr = entryNr;
  }

  public Long getRunnerNr() {
    return runnerNr;
  }

  public void setRunnerNr(Long runnerNr) {
    this.runnerNr = runnerNr;
  }

  public Long getRank() {
    return rank;
  }

  public void setRank(Long rank) {
    this.rank = rank;
  }

  public Long getClassShortcut() {
    return classShortcut;
  }

  public void setClassShortcut(Long classShortcut) {
    this.classShortcut = classShortcut;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getCourseShortcut() {
    return courseShortcut;
  }

  public void setCourseShortcut(String courseShortcut) {
    this.courseShortcut = courseShortcut;
  }

  public String getBibNumber() {
    return bibNumber;
  }

  public void setBibNumber(String bibNumber) {
    this.bibNumber = bibNumber;
  }

  public String getExtKey() {
    return extKey;
  }

  public void setExtKey(String extKey) {
    this.extKey = extKey;
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

  public String geteCard() {
    return eCard;
  }

  public void seteCard(String eCard) {
    this.eCard = eCard;
  }

  public boolean isRentalCard() {
    return rentalCard;
  }

  public void setRentalCard(boolean rentalCard) {
    this.rentalCard = rentalCard;
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

  public String getNation() {
    return nation;
  }

  public void setNation(String nation) {
    this.nation = nation;
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

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
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

  public String geteMail() {
    return eMail;
  }

  public void seteMail(String eMail) {
    this.eMail = eMail;
  }

  public String getURL() {
    return URL;
  }

  public void setURL(String uRL) {
    URL = uRL;
  }

  public Long getRaceStatus() {
    return raceStatus;
  }

  public void setRaceStatus(Long raceStatus) {
    this.raceStatus = raceStatus;
  }

  public Long getLegStartTime() {
    return legStartTime;
  }

  public void setLegStartTime(Long legStartTime) {
    this.legStartTime = legStartTime;
  }

  public Long getLegTime() {
    return legTime;
  }

  public void setLegTime(Long legTime) {
    this.legTime = legTime;
  }

  public String getTimeBehind() {
    return timeBehind;
  }

  public void setTimeBehind(String timeBehind) {
    this.timeBehind = timeBehind;
  }

  public Double getPercent() {
    return percent;
  }

  public void setPercent(Double percent) {
    this.percent = percent;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
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
