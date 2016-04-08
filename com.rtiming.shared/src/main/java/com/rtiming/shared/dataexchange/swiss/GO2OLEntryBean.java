package com.rtiming.shared.dataexchange.swiss;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.shared.dataexchange.AbstractCSVDataBean;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.CSVElement;
import com.rtiming.shared.dataexchange.IDataExchangeService;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.ImportMessageList;

public class GO2OLEntryBean extends AbstractCSVDataBean {

  private static final long serialVersionUID = 1L;
  private final Long eventNr;

  @CSVElement(value = 1, title = "FOREIGNKEY")
  private String foreignkey;

  @CSVElement(value = 2, isMandatory = true, title = "FAMILYNAME")
  private String familyname;

  @CSVElement(value = 3, title = "FIRSTNAME")
  private String firstname;

  @CSVElement(value = 4, title = "SEX")
  private String sex;

  @CSVElement(value = 5, title = "YEAR4")
  private String year4;

  @CSVElement(value = 6, title = "ADR1")
  private String adr1;

  @CSVElement(value = 7, title = "ADR2")
  private String adr2;

  @CSVElement(value = 8, title = "ZIP")
  private String zip;

  @CSVElement(value = 9, title = "TOWN")
  private String town;

  @CSVElement(value = 10, title = "CANTON")
  private String canton;

  @CSVElement(value = 11, title = "REGION")
  private String region;

  @CSVElement(value = 12, title = "COUNTRY")
  private String country;

  @CSVElement(value = 13, title = "EMAIL")
  private String email;

  @CSVElement(value = 14, title = "MOBILE")
  private String mobile;

  @CSVElement(value = 15, title = "FEDNR")
  private String fednr;

  @CSVElement(value = 16, title = "CLUB")
  private String club;

  @CSVElement(value = 17, title = "CLASSSHORT")
  private String classshort;

  @CSVElement(value = 18, title = "CARDNR")
  private String cardnr;

  @CSVElement(value = 19, title = "STARTBLOCK")
  private String startblock;

  @CSVElement(value = 20, title = "STARTEARLY")
  private String startearly;

  @CSVElement(value = 21, title = "STARTLATE")
  private String startlate;

  @CSVElement(value = 22, title = "CHILD")
  private String child;

  @CSVElement(value = 23, title = "STARTNR")
  private String startnr;

  @CSVElement(value = 24, title = "STARTTIME")
  private String starttime;

  @CSVElement(value = 25, title = "NATIONSTARTEDFOR")
  private String nationstartedfor;

  @CSVElement(value = 26, title = "SENDPAPER")
  private String sendpaper;

  @CSVElement(value = 27, title = "TRAIN")
  private String train;

  @CSVElement(value = 28, title = "NURSERY")
  private String nursery;

  @CSVElement(value = 29, title = "CURRENCY")
  private String currency;

  @CSVElement(value = 30, title = "FEEOWED")
  private String feeowed;

  @CSVElement(value = 31, title = "FEEPAYED")
  private String feepayed;

  @CSVElement(value = 32, title = "GROUPSORT")
  private String groupsort;

  @CSVElement(value = 33, title = "REMARK", maxLength = Integer.MAX_VALUE)
  private String remark;

  @CSVElement(value = 34, title = "DOPSTAT")
  private String dopstat;

  @CSVElement(value = 35, title = "EXCELTERM")
  private String excelterm;

  public GO2OLEntryBean(Long primaryKeyNr, Long eventNr) {
    super(primaryKeyNr);
    this.eventNr = eventNr;
  }

  @Override
  public void setData(List<Object> data) {
    foreignkey = (String) data.get(0);
    familyname = (String) data.get(1);
    firstname = (String) data.get(2);
    sex = (String) data.get(3);
    year4 = (String) data.get(4);
    adr1 = (String) data.get(5);
    adr2 = (String) data.get(6);
    zip = (String) data.get(7);
    town = (String) data.get(8);
    canton = (String) data.get(9);
    region = (String) data.get(10);
    country = (String) data.get(11);
    email = (String) data.get(12);
    mobile = (String) data.get(13);
    fednr = (String) data.get(14);
    club = (String) data.get(15);
    classshort = (String) data.get(16);
    cardnr = (String) data.get(17);
    startblock = (String) data.get(18);
    startearly = (String) data.get(19);
    startlate = (String) data.get(20);
    child = (String) data.get(21);
    startnr = (String) data.get(22);
    starttime = (String) data.get(23);
    nationstartedfor = (String) data.get(24);
    sendpaper = (String) data.get(25);
    train = (String) data.get(26);
    nursery = (String) data.get(27);
    currency = (String) data.get(28);
    feeowed = (String) data.get(29);
    feepayed = (String) data.get(30);
    groupsort = (String) data.get(31);
    remark = (String) data.get(32);
    dopstat = (String) data.get(33);
    excelterm = (String) data.get(34);
  }

  @Override
  public List<Object> getData() {
    List<Object> list = new ArrayList<Object>(35);

    list.add(foreignkey);
    list.add(familyname);
    list.add(firstname);
    list.add(sex);
    list.add(year4);
    list.add(adr1);
    list.add(adr2);
    list.add(zip);
    list.add(town);
    list.add(canton);
    list.add(region);
    list.add(country);
    list.add(email);
    list.add(mobile);
    list.add(fednr);
    list.add(club);
    list.add(classshort);
    list.add(cardnr);
    list.add(startblock);
    list.add(startearly);
    list.add(startlate);
    list.add(child);
    list.add(startnr);
    list.add(starttime);
    list.add(nationstartedfor);
    list.add(sendpaper);
    list.add(train);
    list.add(nursery);
    list.add(currency);
    list.add(feeowed);
    list.add(feepayed);
    list.add(groupsort);
    list.add(remark);
    list.add(dopstat);
    list.add(excelterm);

    return list;
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {
  }

  @Override
  protected void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {
  }

  @Override
  public void storeBeansToDatabase(List<AbstractDataBean> batch, IProgressMonitor monitor) throws ProcessingException {
    ImportMessageList result = BEANS.get(IDataExchangeService.class).storeGO2OLEntry(batch, eventNr);
    monitor.addErrors(result);
  }

  public String getForeignkey() {
    return foreignkey;
  }

  public void setForeignkey(String foreignkey) {
    this.foreignkey = foreignkey;
  }

  public String getFamilyname() {
    return familyname;
  }

  public void setFamilyname(String familyname) {
    this.familyname = familyname;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getYear4() {
    return year4;
  }

  public void setYear4(String year4) {
    this.year4 = year4;
  }

  public String getAdr1() {
    return adr1;
  }

  public void setAdr1(String adr1) {
    this.adr1 = adr1;
  }

  public String getAdr2() {
    return adr2;
  }

  public void setAdr2(String adr2) {
    this.adr2 = adr2;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getTown() {
    return town;
  }

  public void setTown(String town) {
    this.town = town;
  }

  public String getCanton() {
    return canton;
  }

  public void setCanton(String canton) {
    this.canton = canton;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getFednr() {
    return fednr;
  }

  public void setFednr(String fednr) {
    this.fednr = fednr;
  }

  public String getClub() {
    return club;
  }

  public void setClub(String club) {
    this.club = club;
  }

  public String getClassshort() {
    return classshort;
  }

  public void setClassshort(String classshort) {
    this.classshort = classshort;
  }

  public String getCardnr() {
    return cardnr;
  }

  public void setCardnr(String cardnr) {
    this.cardnr = cardnr;
  }

  public String getStartblock() {
    return startblock;
  }

  public void setStartblock(String startblock) {
    this.startblock = startblock;
  }

  public String getStartearly() {
    return startearly;
  }

  public void setStartearly(String startearly) {
    this.startearly = startearly;
  }

  public String getStartlate() {
    return startlate;
  }

  public void setStartlate(String startlate) {
    this.startlate = startlate;
  }

  public String getChild() {
    return child;
  }

  public void setChild(String child) {
    this.child = child;
  }

  public String getStartnr() {
    return startnr;
  }

  public void setStartnr(String startnr) {
    this.startnr = startnr;
  }

  public String getStarttime() {
    return starttime;
  }

  public void setStarttime(String starttime) {
    this.starttime = starttime;
  }

  public String getNationstartedfor() {
    return nationstartedfor;
  }

  public void setNationstartedfor(String nationstartedfor) {
    this.nationstartedfor = nationstartedfor;
  }

  public String getSendpaper() {
    return sendpaper;
  }

  public void setSendpaper(String sendpaper) {
    this.sendpaper = sendpaper;
  }

  public String getTrain() {
    return train;
  }

  public void setTrain(String train) {
    this.train = train;
  }

  public String getNursery() {
    return nursery;
  }

  public void setNursery(String nursery) {
    this.nursery = nursery;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getFeeowed() {
    return feeowed;
  }

  public void setFeeowed(String feeowed) {
    this.feeowed = feeowed;
  }

  public String getFeepayed() {
    return feepayed;
  }

  public void setFeepayed(String feepayed) {
    this.feepayed = feepayed;
  }

  public String getGroupsort() {
    return groupsort;
  }

  public void setGroupsort(String groupsort) {
    this.groupsort = groupsort;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getDopstat() {
    return dopstat;
  }

  public void setDopstat(String dopstat) {
    this.dopstat = dopstat;
  }

  public String getExcelterm() {
    return excelterm;
  }

  public void setExcelterm(String excelterm) {
    this.excelterm = excelterm;
  }

  public Long getEventNr() {
    return eventNr;
  }

  @Override
  public String toString() {
    return StringUtility.emptyIfNull(foreignkey) + " " + StringUtility.emptyIfNull(familyname) + " " + StringUtility.emptyIfNull(firstname);
  }

}
