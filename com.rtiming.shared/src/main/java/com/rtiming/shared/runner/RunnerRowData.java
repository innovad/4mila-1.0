package com.rtiming.shared.runner;

import java.io.Serializable;
import java.util.Date;

public class RunnerRowData implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long runnerNr;
  private String extKey;
  private String name;
  private String lastName;
  private String firstName;
  private String eCard;
  private Long defaultClazzUid;
  private String club;
  private String nation;
  private String nationCountryCode;
  private Long sexUid;
  private Date evtBirthdate;
  private Long year;
  private String street;
  private String zip;
  private String city;
  private Long areaUid;
  private String region;
  private Long countryUid;
  private String phone;
  private String fax;
  private String mobile;
  private String email;
  private String www;
  private Object[] additionalValues;

  public Long getRunnerNr() {
    return runnerNr;
  }

  public void setRunnerNr(Long runnerNr) {
    this.runnerNr = runnerNr;
  }

  public String getExtKey() {
    return extKey;
  }

  public void setExtKey(String extKey) {
    this.extKey = extKey;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public Long getDefaultClazzUid() {
    return defaultClazzUid;
  }

  public void setDefaultClazz(Long defaultClazzUid) {
    this.defaultClazzUid = defaultClazzUid;
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

  public Long getSexUid() {
    return sexUid;
  }

  public void setSexUid(Long sexUid) {
    this.sexUid = sexUid;
  }

  public Date getEvtBirthdate() {
    return evtBirthdate;
  }

  public void setEvtBirthdate(Date evtBirthdate) {
    this.evtBirthdate = evtBirthdate;
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

  public Long getAreaUid() {
    return areaUid;
  }

  public void setAreaUid(Long areaUid) {
    this.areaUid = areaUid;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public Long getCountryUid() {
    return countryUid;
  }

  public void setCountryUid(Long countryUid) {
    this.countryUid = countryUid;
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

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getWww() {
    return www;
  }

  public void setWww(String www) {
    this.www = www;
  }

  public Object[] getAdditionalValues() {
    return additionalValues;
  }

  public void setAdditionalValues(Object[] additionalValues) {
    this.additionalValues = additionalValues;
  }

}
