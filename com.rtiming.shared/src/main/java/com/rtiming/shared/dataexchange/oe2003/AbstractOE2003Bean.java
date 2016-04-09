package com.rtiming.shared.dataexchange.oe2003;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.shared.dataexchange.AbstractCSVDataBean;
import com.rtiming.shared.dataexchange.CSVElement;

public abstract class AbstractOE2003Bean extends AbstractCSVDataBean {

  private static final long serialVersionUID = 1L;

  @CSVElement(value = 1, title = "OE2003_Stno")
  private String startNumber;

  @CSVElement(value = 2, title = "OE2003_Chip")
  private String eCardNumber;

  @CSVElement(value = 3, title = "OE2003_DatabaseId")
  private String databaseExtKey;

  @CSVElement(value = 4, title = "OE2003_Surname", isMandatory = true)
  private String lastName;

  @CSVElement(value = 5, title = "OE2003_Firstname")
  private String firstName;

  @CSVElement(value = 6, title = "OE2003_YB", isYear = true)
  private String yearOfBirth;

  @CSVElement(value = 7, title = "OE2003_S", isSex = true)
  private String sex;

  @CSVElement(value = 8, title = "OE2003_Block")
  private String startblockCode;

  @CSVElement(value = 9, title = "OE2003_nc")
  private String classificationStatus;

  @CSVElement(value = 10, title = "OE2003_Start")
  private String startTime;

  @CSVElement(value = 11, title = "OE2003_Finish")
  private String finishTime;

  @CSVElement(value = 12, title = "OE2003_Time")
  private String time;

  @CSVElement(value = 13, title = "OE2003_Classifier")
  private String raceStatus;

  @CSVElement(value = 14, title = "OE2003_ClubNo")
  private String clubNumber;

  @CSVElement(value = 15, title = "OE2003_ClName")
  private String clubShortcut;

  @CSVElement(value = 16, title = "OE2003_City")
  private String clubName;

  @CSVElement(value = 17, title = "OE2003_Nat")
  private String nation;

  @CSVElement(value = 18, title = "OE2003_ClNo")
  private String classCode;

  @CSVElement(value = 19, title = "OE2003_Short", isMandatory = true)
  private String classShortcut;

  @CSVElement(value = 20, title = "OE2003_Long")
  private String className;

  @CSVElement(value = 21, title = "OE2003_Num1")
  private String numField1;

  @CSVElement(value = 22, title = "OE2003_Num2")
  private String numField2;

  @CSVElement(value = 23, title = "OE2003_Num3")
  private String numField3;

  @CSVElement(value = 24, title = "OE2003_Text1")
  private String stringField1;

  @CSVElement(value = 25, title = "OE2003_Text2")
  private String stringField2;

  @CSVElement(value = 26, title = "OE2003_Text3")
  private String stringField3;

  @CSVElement(value = 27, title = "OE2003_AdrName")
  private String addressName;

  @CSVElement(value = 28, title = "OE2003_Street")
  private String street;

  @CSVElement(value = 29, title = "OE2003_Line2")
  private String street2ndLine;

  @CSVElement(value = 30, title = "OE2003_Zip")
  private String zipCode;

  @CSVElement(value = 31, title = "OE2003_City")
  private String city;

  @CSVElement(value = 32, title = "OE2003_Phone")
  private String phone;

  @CSVElement(value = 33, title = "OE2003_Fax")
  private String faxNumber;

  @CSVElement(value = 34, title = "OE2003_EMail")
  private String email;

  @CSVElement(value = 35, title = "OE2003_IdClub")
  private String clubCode;

  @CSVElement(value = 36, title = "OE2003_Rented")
  private String eCardRented;

  @CSVElement(value = 37, title = "OE2003_StartFee")
  private String startFee;

  @CSVElement(value = 38, title = "OE2003_Paid")
  private String paidYesOrNo;

  public AbstractOE2003Bean(Long primaryKeyNr) {
    super(primaryKeyNr);
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {
  }

  public void setStartNumber(String startNumber) {
    this.startNumber = startNumber;
  }

  public String getStartNumber() {
    return startNumber;
  }

  public String getECardNumber() {
    return eCardNumber;
  }

  public void setECardNumber(String siCardString) {
    this.eCardNumber = siCardString;
  }

  public String getDatenbankId() {
    return databaseExtKey;
  }

  public void setDatenbankId(String datenbankId) {
    databaseExtKey = datenbankId;
  }

  public String getNName() {
    return lastName;
  }

  public void setNName(String nName) {
    lastName = nName;
  }

  public String getVName() {
    return firstName;
  }

  public void setVName(String vName) {
    firstName = vName;
  }

  public String getJg() {
    return yearOfBirth;
  }

  public void setJg(String jg) {
    yearOfBirth = jg;
  }

  public String getGeschlecht() {
    return sex;
  }

  public void setGeschlecht(String geschlecht) {
    sex = geschlecht;
  }

  public String getBlock() {
    return startblockCode;
  }

  public void setBlock(String block) {
    startblockCode = block;
  }

  public String getAK() {
    return classificationStatus;
  }

  public void setAK(String aK) {
    classificationStatus = aK;
  }

  public String getStart() {
    return startTime;
  }

  public void setStart(String start) {
    startTime = start;
  }

  public String getZiel() {
    return finishTime;
  }

  public void setZiel(String ziel) {
    finishTime = ziel;
  }

  public String getZeit() {
    return time;
  }

  public void setZeit(String zeit) {
    time = zeit;
  }

  public String getWertung() {
    return raceStatus;
  }

  public void setWertung(String wertung) {
    raceStatus = wertung;
  }

  public String getClubNr() {
    return clubNumber;
  }

  public void setClubNr(String clubNr) {
    clubNumber = clubNr;
  }

  public String getAbk() {
    return clubShortcut;
  }

  public void setAbk(String abk) {
    clubShortcut = abk;
  }

  public String getOrt() {
    return clubName;
  }

  public void setOrt(String ort) {
    clubName = ort;
  }

  public String getKatnr() {
    return classCode;
  }

  public void setKatnr(String katnr) {
    classCode = katnr;
  }

  public String getKurz() {
    return classShortcut;
  }

  public void setKurz(String kurz) {
    classShortcut = kurz;
  }

  public String getLang() {
    return className;
  }

  public void setLang(String lang) {
    className = lang;
  }

  public String getNum1() {
    return numField1;
  }

  public void setNum1(String num1) {
    numField1 = num1;
  }

  public String getNum2() {
    return numField2;
  }

  public void setNum2(String num2) {
    numField2 = num2;
  }

  public String getNum3() {
    return numField3;
  }

  public void setNum3(String num3) {
    numField3 = num3;
  }

  public String getText1() {
    return stringField1;
  }

  public void setText1(String text1) {
    stringField1 = text1;
  }

  public String getText2() {
    return stringField2;
  }

  public void setText2(String text2) {
    stringField2 = text2;
  }

  public String getText3() {
    return stringField3;
  }

  public void setText3(String text3) {
    stringField3 = text3;
  }

  public String getAdrName() {
    return addressName;
  }

  public void setAdrName(String adrName) {
    addressName = adrName;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String string) {
    street = string;
  }

  public String getZeile2() {
    return street2ndLine;
  }

  public void setZeile2(String zeile2) {
    street2ndLine = zeile2;
  }

  public String getPLZ() {
    return zipCode;
  }

  public void setPLZ(String pLZ) {
    zipCode = pLZ;
  }

  public String getWohnort() {
    return city;
  }

  public void setWohnort(String wohnort) {
    city = wohnort;
  }

  public String getTel() {
    return phone;
  }

  public void setTel(String tel) {
    phone = tel;
  }

  public String getFax() {
    return faxNumber;
  }

  public void setFax(String fax) {
    faxNumber = fax;
  }

  public String getEMail() {
    return email;
  }

  public void setEMail(String eMail) {
    email = eMail;
  }

  public String getClubCode() {
    return clubCode;
  }

  public void setClubCode(String code) {
    clubCode = code;
  }

  public String getGemietet() {
    return eCardRented;
  }

  public void setGemietet(String gemietet) {
    eCardRented = gemietet;
  }

  public String getStartgeld() {
    return startFee;
  }

  public void setStartgeld(String startgeld) {
    startFee = startgeld;
  }

  public String getBezahlt() {
    return paidYesOrNo;
  }

  public void setBezahlt(String bezahlt) {
    paidYesOrNo = bezahlt;
  }

  @Override
  public void setData(List<Object> data) {
    startNumber = (String) data.get(0);
    eCardNumber = (String) data.get(1);
    databaseExtKey = (String) data.get(2);
    lastName = (String) data.get(3);
    firstName = (String) data.get(4);
    yearOfBirth = (String) data.get(5);
    sex = (String) data.get(6);
    startblockCode = (String) data.get(7);
    classificationStatus = (String) data.get(8);
    startTime = (String) data.get(9);
    finishTime = (String) data.get(10);
    time = (String) data.get(11);
    raceStatus = (String) data.get(12);
    clubNumber = (String) data.get(13);
    clubShortcut = (String) data.get(14);
    clubName = (String) data.get(15);
    nation = (String) data.get(16);
    classCode = (String) data.get(17);
    classShortcut = (String) data.get(18);
    className = (String) data.get(19);
    numField1 = (String) data.get(20);
    numField2 = (String) data.get(21);
    numField3 = (String) data.get(22);
    stringField1 = (String) data.get(23);
    stringField2 = (String) data.get(24);
    stringField3 = (String) data.get(25);
    addressName = (String) data.get(26);
    street = (String) data.get(27);
    street2ndLine = (String) data.get(28);
    zipCode = (String) data.get(29);
    city = (String) data.get(30);
    phone = (String) data.get(31);
    faxNumber = (String) data.get(32);
    email = (String) data.get(33);
    clubCode = (String) data.get(34);
    eCardRented = (String) data.get(35);
    startFee = (String) data.get(36);
    paidYesOrNo = (String) data.get(37);
  }

  @Override
  public List<Object> getData() {
    List<Object> list = new ArrayList<Object>(80);

    list.add(startNumber);
    list.add(eCardNumber);
    list.add(databaseExtKey);
    list.add(lastName);
    list.add(firstName);
    list.add(yearOfBirth);
    list.add(sex);
    list.add(startblockCode);
    list.add(classificationStatus);
    list.add(startTime);
    list.add(finishTime);
    list.add(time);
    list.add(raceStatus);
    list.add(clubNumber);
    list.add(clubShortcut);
    list.add(clubName);
    list.add(nation);
    list.add(classCode);
    list.add(classShortcut);
    list.add(className);
    list.add(numField1);
    list.add(numField2);
    list.add(numField3);
    list.add(stringField1);
    list.add(stringField2);
    list.add(stringField3);
    list.add(addressName);
    list.add(street);
    list.add(street2ndLine);
    list.add(zipCode);
    list.add(city);
    list.add(phone);
    list.add(faxNumber);
    list.add(email);
    list.add(clubCode);
    list.add(eCardRented);
    list.add(startFee);
    list.add(paidYesOrNo);

    return list;
  }

  public String geteCardNumber() {
    return eCardNumber;
  }

  public String getDatabaseExtKey() {
    return databaseExtKey;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getYearOfBirth() {
    return yearOfBirth;
  }

  public String getSex() {
    return sex;
  }

  public String getStartblockCode() {
    return startblockCode;
  }

  public String getClassificationStatus() {
    return classificationStatus;
  }

  public String getStartTime() {
    return startTime;
  }

  public String getFinishTime() {
    return finishTime;
  }

  public String getTime() {
    return time;
  }

  public String getRaceStatus() {
    return raceStatus;
  }

  public String getClubNumber() {
    return clubNumber;
  }

  public String getClubShortcut() {
    return clubShortcut;
  }

  public String getClubName() {
    return clubName;
  }

  public String getNation() {
    return nation;
  }

  public void setNation(String nation) {
    this.nation = nation;
  }

  public String getClassCode() {
    return classCode;
  }

  public String getClassShortcut() {
    return classShortcut;
  }

  public String getClassName() {
    return className;
  }

  public String getNumField1() {
    return numField1;
  }

  public String getNumField2() {
    return numField2;
  }

  public String getNumField3() {
    return numField3;
  }

  public String getStringField1() {
    return stringField1;
  }

  public String getStringField2() {
    return stringField2;
  }

  public String getStringField3() {
    return stringField3;
  }

  public String getAddressName() {
    return addressName;
  }

  public String getStreet2ndLine() {
    return street2ndLine;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getCity() {
    return city;
  }

  public String getPhone() {
    return phone;
  }

  public String getFaxNumber() {
    return faxNumber;
  }

  public String getEmail() {
    return email;
  }

  public String geteCardRented() {
    return eCardRented;
  }

  public String getStartFee() {
    return startFee;
  }

  public String getPaidYesOrNo() {
    return paidYesOrNo;
  }

  @Override
  public String toString() {
    return StringUtility.emptyIfNull(firstName) + " " + StringUtility.emptyIfNull(lastName);
  }

}
