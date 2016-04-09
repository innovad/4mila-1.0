package com.rtiming.shared.dataexchange.swiss;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.club.IClubProcessService;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.database.sql.ClubBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.dataexchange.AbstractCSVDataBean;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.CSVElement;
import com.rtiming.shared.dataexchange.DataExchangeUtility;
import com.rtiming.shared.dataexchange.IDataExchangeService;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.ImportMessageList;
import com.rtiming.shared.ecard.ECardFormData;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.city.AreaCodeType;
import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.CountryCodeType;
import com.rtiming.shared.settings.city.ICityProcessService;

public class SwissOrienteeringRunnerBean extends AbstractCSVDataBean {

  //    1 SOLV-Nr
  //    2 SI_Karte
  //    3 Name
  //    4 Vorname
  //    5 Jahrgang
  //    6 Geschlecht
  //    7 Adresstyp
  //    8 Adressz1
  //    9 Adressz2
  //   10 PLZ
  //   11 Ort
  //   12 Kanton
  //   13 Region
  //   14 Land
  //   15 Nation
  //   16 Verein
  //   17 Vereins-Nr
  //   18 NofComp
  //   19 LastComp
  //   20 Kategorie
  //   21 Email
  //   22 Flags
  //   23 MissedCards
  //   24 Dop.Stat

  private static final long serialVersionUID = 1L;

  @CSVElement(value = 1, title = "SOLV Nr", maxLength = 6)
  private String swissOrienteeringDatabaseCode;

  @CSVElement(value = 2, title = "SICard", maxLength = 8)
  private String eCardNumber;

  @CSVElement(value = 3, title = "Nachname", isMandatory = true, maxLength = 250)
  private String lastName;

  @CSVElement(value = 4, title = "Vorname", maxLength = 250)
  private String firstName;

  @CSVElement(value = 5, title = "Jahrgang", isYear = true)
  private String yearOfBirth;

  // M/F
  @CSVElement(value = 6, title = "Geschlecht", isSex = true)
  private String sex;

  @CSVElement(value = 7, title = "")
  private String emptyField = "";

  @CSVElement(value = 8, title = "Strasse", maxLength = 250)
  private String street;

  @CSVElement(value = 9, title = "")
  private String emptyField2 = "";

  @CSVElement(value = 10, title = "PLZ")
  private String zipCode;

  @CSVElement(value = 11, title = "Ort")
  private String cityStr;

  @CSVElement(value = 12, title = "Kanton")
  private String canton;

  @CSVElement(value = 13, title = "Region")
  private String region;

  @CSVElement(value = 14, title = "Land", isMandatory = true)
  private String country;

  @CSVElement(value = 15, title = "Nation")
  private String nationStr;

  @CSVElement(value = 16, title = "Verein", maxLength = 250)
  private String clubName;

  @CSVElement(value = 17, title = "ID Verein")
  private String clubCode;

  @CSVElement(value = 18, title = "NofComp")
  private String numberOfCompetitions;

  @CSVElement(value = 19, title = "LastComp")
  private String lastComp;

  @CSVElement(value = 20, title = "Kategorie")
  private String clazzCode;

  @CSVElement(value = 21, title = "Email")
  private String email;

  @CSVElement(value = 22, title = "Flags")
  private String flags;

  @CSVElement(value = 23, title = "MissedCards")
  private String missedCards;

  @CSVElement(value = 24, title = "DopStat")
  private String dopStat;

  public SwissOrienteeringRunnerBean(Long primaryKeyNr) {
    super(primaryKeyNr);
  }

  @Override
  public void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {
  }

  @Override
  public void storeBeansToDatabase(List<AbstractDataBean> batch, IProgressMonitor monitor) throws ProcessingException {
    ImportMessageList result = BEANS.get(IDataExchangeService.class).storeSwissOrienteeringRunner(batch);
    monitor.addErrors(result);
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {
    // load data
    RunnerBean runner = new RunnerBean();
    runner.setRunnerNr(getPrimaryKeyNr());
    runner = BEANS.get(IRunnerProcessService.class).load(runner);

    ECardFormData ecard = new ECardFormData();
    if (runner.getECardNr() != null) {
      RtEcardKey key = new RtEcardKey();
      key.setId(runner.getECardNr());
      // TODO must get client session nr here
      ecard = BeanUtility.eCardBean2FormData(BEANS.get(IECardProcessService.class).load(key));
    }

    CityFormData city = new CityFormData();
    city.setCityNr(runner.getAddress().getCityNr());
    city = BEANS.get(ICityProcessService.class).load(city);

    ClubBean club = new ClubBean();
    club.setClubNr(runner.getClubNr());
    if (club.getClubNr() != null) {
      club = BEANS.get(IClubProcessService.class).load(club);
    }

    Long antiDopingSigned = 0L;
    for (AdditionalInformationValueBean value : runner.getAddInfo().getValues()) {
      if (CompareUtility.equals(value.getAdditionalInformationUid(), AdditionalInformationCodeType.SwissOrienteeringAntiDopingCode.ID.longValue())) {
        antiDopingSigned = value.getValueInteger();
      }
    }

    // write fields
    swissOrienteeringDatabaseCode = runner.getExtKey();
    eCardNumber = ecard.getNumber().getValue();
    lastName = runner.getLastName();
    firstName = runner.getFirstName();
    yearOfBirth = DataExchangeUtility.exportLong(runner.getYear());
    if (CompareUtility.equals(runner.getSexUid(), SexCodeType.ManCode.ID)) {
      sex = "M";
    }
    else if (CompareUtility.equals(runner.getSexUid(), SexCodeType.WomanCode.ID)) {
      sex = "F";
    }
    else {
      sex = "";
    }
    street = runner.getAddress().getStreet();
    zipCode = city.getZip().getValue();
    cityStr = city.getCity().getValue();
    canton = city.getRegion().getValue();
    region = FMilaUtility.getCodeText(AreaCodeType.class, city.getArea().getValue());
    country = FMilaUtility.getCodeText(CountryCodeType.class, city.getCountry().getValue());
    nationStr = FMilaUtility.getCodeText(CountryCodeType.class, runner.getNationUid());
    email = runner.getAddress().getEmail();
    clazzCode = FMilaUtility.getCodeExtKey(ClassCodeType.class, runner.getDefaultClassUid());
    clubName = club.getName();
    clubCode = club.getShortcut();
    dopStat = DataExchangeUtility.exportLong(NumberUtility.nvl(antiDopingSigned, 0L));

  }

  @Override
  public String toString() {
    return StringUtility.nvl(swissOrienteeringDatabaseCode, "") + " " + StringUtility.nvl(lastName, "") + " " + StringUtility.nvl(firstName, "");
  }

  @Override
  public void setData(List<Object> list) {
    swissOrienteeringDatabaseCode = (String) list.get(0);
    eCardNumber = (String) list.get(1);
    lastName = (String) list.get(2);
    firstName = (String) list.get(3);
    yearOfBirth = (String) list.get(4);
    sex = (String) list.get(5);
    emptyField = (String) list.get(6);
    street = (String) list.get(7);
    emptyField2 = (String) list.get(8);
    zipCode = (String) list.get(9);
    cityStr = (String) list.get(10);
    canton = (String) list.get(11);
    region = (String) list.get(12);
    country = (String) list.get(13);
    nationStr = (String) list.get(14);
    clubName = (String) list.get(15);
    clubCode = (String) list.get(16);
    numberOfCompetitions = (String) list.get(17);
    lastComp = (String) list.get(18);
    clazzCode = (String) list.get(19);
    email = (String) list.get(20);
    flags = (String) list.get(21);
    missedCards = (String) list.get(22);
    dopStat = (String) list.get(23);
  }

  @Override
  public List<Object> getData() {
    List<Object> list = new ArrayList<Object>(25);
    list.add(swissOrienteeringDatabaseCode);
    list.add(eCardNumber);
    list.add(lastName);
    list.add(firstName);
    list.add(yearOfBirth);
    list.add(sex);
    list.add(emptyField);
    list.add(street);
    list.add(emptyField2);
    list.add(zipCode);
    list.add(cityStr);
    list.add(canton);
    list.add(region);
    list.add(country);
    list.add(nationStr);
    list.add(clubName);
    list.add(clubCode);
    list.add(numberOfCompetitions);
    list.add(lastComp);
    list.add(clazzCode);
    list.add(email);
    list.add(flags);
    list.add(missedCards);
    list.add(dopStat);
    return list;
  }

  public String getSwissOrienteeringDatabaseCode() {
    return swissOrienteeringDatabaseCode;
  }

  public String geteCardNumber() {
    return eCardNumber;
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

  public String getEmptyField() {
    return emptyField;
  }

  public String getStreet() {
    return street;
  }

  public String getEmptyField2() {
    return emptyField2;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getCityStr() {
    return cityStr;
  }

  public String getCanton() {
    return canton;
  }

  public String getRegion() {
    return region;
  }

  public String getCountry() {
    return country;
  }

  public String getNationStr() {
    return nationStr;
  }

  public String getClubName() {
    return clubName;
  }

  public String getClubCode() {
    return clubCode;
  }

  public String getNumberOfCompetitions() {
    return numberOfCompetitions;
  }

  public String getLastComp() {
    return lastComp;
  }

  public String getClazzCode() {
    return clazzCode;
  }

  public String getEmail() {
    return email;
  }

  public String getFlags() {
    return flags;
  }

  public String getMissedCards() {
    return missedCards;
  }

  public String getDopStat() {
    return dopStat;
  }

}
