package com.rtiming.shared.dataexchange.iof3;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.database.sql.AddressBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dataexchange.DataExchangeUtility;
import com.rtiming.shared.dataexchange.cache.CityCacheKey;
import com.rtiming.shared.dataexchange.cache.CityDataCacher;
import com.rtiming.shared.dataexchange.iof3.xml.Address;
import com.rtiming.shared.dataexchange.iof3.xml.Contact;
import com.rtiming.shared.dataexchange.iof3.xml.ControlCard;
import com.rtiming.shared.dataexchange.iof3.xml.ControlType;
import com.rtiming.shared.dataexchange.iof3.xml.Country;
import com.rtiming.shared.dataexchange.iof3.xml.Event;
import com.rtiming.shared.dataexchange.iof3.xml.Id;
import com.rtiming.shared.dataexchange.iof3.xml.Organisation;
import com.rtiming.shared.dataexchange.iof3.xml.Person;
import com.rtiming.shared.dataexchange.iof3.xml.PersonName;
import com.rtiming.shared.dataexchange.iof3.xml.ResultStatus;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.CountryCodeType;

/**
 * 
 */
public class IOF300Utility {

  private static DatatypeFactory datatypeFactory;

  private IOF300Utility() throws ProcessingException {
  }

  /**
   * <xsd:enumeration value="PhoneNumber"/>
   * <xsd:enumeration value="MobilePhoneNumber"/>
   * <xsd:enumeration value="FaxNumber"/>
   * <xsd:enumeration value="EmailAddress"/>
   * <xsd:enumeration value="WebAddress"/>
   * <xsd:enumeration value="Other"/>
   **/
  public enum ContactType {
    PhoneNumber, MobilePhoneNumber, FaxNumber, EmailAddress, WebAddress, Other
  }

  public static Double toDoubleTime(Long milliseconds) {
    if (milliseconds == null) {
      return null;
    }
    return NumberUtility.toDouble(milliseconds) / 1000d;
  }

  public static DatatypeFactory getDatatypeFactory() throws ProcessingException {
    if (datatypeFactory == null) {
      datatypeFactory = createFactory();
    }
    return datatypeFactory;
  }

  public static com.rtiming.shared.dataexchange.iof3.xml.Class convertToXmlClass(String shortName) {
    com.rtiming.shared.dataexchange.iof3.xml.Class clazz = new com.rtiming.shared.dataexchange.iof3.xml.Class();
    clazz.setShortName(shortName);
    clazz.setName(shortName);
    return clazz;
  }

  public static Person convertToXmlPerson(String extKey, String lastName, String firstName, Long sexUid, Long yearOfBirth, Date birthdate) throws ProcessingException {
    Person person = new Person();
    PersonName personName = new PersonName();
    personName.setFamily(lastName);
    personName.setGiven(firstName);
    person.setName(personName);
    person.setSex(convertToXmlSex(sexUid));
    if (birthdate != null) {
      person.setBirthDate(convertToXmlDate(birthdate));
    }
    else if (yearOfBirth != null) {
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTimeInMillis(0);
      cal.set(GregorianCalendar.YEAR, NumberUtility.longToInt(yearOfBirth));
      person.setBirthDate(convertToXmlDate(DateUtility.truncDateToYear(cal.getTime())));
    }
    if (!StringUtility.isNullOrEmpty(extKey)) {
      person.getId().add(convertToXmlExternalKey(extKey));
    }
    return person;
  }

  public static void addContact(Person person, ContactType type, String value) {
    if (StringUtility.hasText(value)) {
      Contact contact = new Contact();
      contact.setType(type.toString());
      contact.setValue(value);
      person.getContact().add(contact);
    }
  }

  public static Address convertToXmlAddress(String city, Long countryUid) throws ProcessingException {
    Address address = new Address();
    Country country = new Country();
    country.setValue(FMilaUtility.getCodeText(CountryCodeType.class, countryUid));
    country.setCode(FMilaUtility.getCodeExtKey(CountryCodeType.class, countryUid));
    address.setCountry(country);
    address.setCity(city);
    return address;
  }

  public static String convertToXmlSex(Long sexUid) {
    if (sexUid == null) {
      return null;
    }
    else if (sexUid == SexCodeType.WomanCode.ID) {
      return "F";
    }
    else if (sexUid == SexCodeType.ManCode.ID) {
      return "M";
    }
    return null;
  }

  public static Organisation convertToXmlClub(String name, String shortName, String extKey) {
    Organisation club = new Organisation();
    club.setName(name);
    club.setShortName(shortName);
    club.setType("Club");
    if (!StringUtility.isNullOrEmpty(extKey)) {
      club.setId(convertToXmlExternalKey(extKey));
    }
    return club;
  }

  public static Id convertToXmlExternalKey(String extKey) {
    Id id = new Id();
    id.setType("Ext");
    id.setValue(extKey);
    return id;
  }

  public static XMLGregorianCalendar convertToXmlDate(Date date) throws ProcessingException {
    if (date == null) {
      return null;
    }
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    return getDatatypeFactory().newXMLGregorianCalendar(cal);
  }

  public static ResultStatus convertToCompetitorStatus(Long raceStatusUid) {
    if (raceStatusUid == null) {
      return ResultStatus.INACTIVE;
    }
    else if (raceStatusUid == RaceStatusCodeType.OkCode.ID) {
      return ResultStatus.OK;
    }
    else if (raceStatusUid == RaceStatusCodeType.DidNotFinishCode.ID) {
      return ResultStatus.DID_NOT_FINISH;
    }
    else if (raceStatusUid == RaceStatusCodeType.DidNotStartCode.ID) {
      return ResultStatus.DID_NOT_START;
    }
    else if (raceStatusUid == RaceStatusCodeType.DisqualifiedCode.ID) {
      return ResultStatus.DISQUALIFIED;
    }
    else if (raceStatusUid == RaceStatusCodeType.NoStartTimeCode.ID) {
      return ResultStatus.MISSING_PUNCH;
    }
    else if (raceStatusUid == RaceStatusCodeType.MissingPunchCode.ID) {
      return ResultStatus.MISSING_PUNCH;
    }

    return ResultStatus.ACTIVE;
  }

  public static String convertToXmlControlStatus(Long controlStatusUid) {
    if (controlStatusUid == null) {
      return null;
    }
    else if (controlStatusUid == ControlStatusCodeType.OkCode.ID) {
      return "OK";
    }
    else if (controlStatusUid == ControlStatusCodeType.MissingCode.ID) {
      return "Missing";
    }
    else if (controlStatusUid == ControlStatusCodeType.AdditionalCode.ID) {
      return "Additional";
    }
    else if (controlStatusUid == ControlStatusCodeType.WrongCode.ID) {
      return "Additional";
    }
    return null;
  }

  public static ControlCard convertToXmlECard(String eCardNo) {
    if (StringUtility.isNullOrEmpty(eCardNo)) {
      return null;
    }
    ControlCard ecard = new ControlCard();
    ecard.setValue(eCardNo);
    ecard.setPunchingSystem("SI");
    return ecard;
  }

  public static Event convertToXmlEvent(Long eventNr) {
    Event event = new Event();
    if (eventNr != null) {
      Id id = new Id();
      id.setType("4mila");
      id.setValue(StringUtility.emptyIfNull(eventNr));
      event.setId(id);
    }
    return event;
  }

  public static String getIOFVersion() {
    return "3.0.0";
  }

  public static String getCreator() {
    // return Texts.get("ApplicationName") + " " + Activator.getDefault().getBundle().getVersion().toString();
    return null; // TODO MIG
  }

  public static XMLGregorianCalendar getCurrentModifyDateTime() throws ProcessingException {
    return convertToXmlDate(new Date());
  }

  public static void convertFromXmlPerson(Person in, RunnerBean existing, CityDataCacher cityCache, String defaultNationCode) throws ProcessingException {
    if (in == null || existing == null) {
      throw new IllegalArgumentException("Arguments must not be null");
    }
    if (!StringUtility.isNullOrEmpty(in.getSex())) {
      existing.setSexUid(DataExchangeUtility.parseSex(in.getSex()));
    }
    if (in.getName() != null) {
      existing.setFirstName(in.getName().getGiven());
      existing.setLastName(in.getName().getFamily());
    }
    if (in.getBirthDate() != null) {
      existing.setEvtBirth(in.getBirthDate().toGregorianCalendar().getTime());
      existing.setYear((long) in.getBirthDate().toGregorianCalendar().get(Calendar.YEAR));
    }
    if (in.getContact() != null && in.getContact().size() > 0) {
      convertFromXmlContact(in.getContact(), existing.getAddress());
    }
    existing.setActive(true);
    if (in.getAddress().size() > 0) {
      convertFromXmlAddress(in.getAddress().get(0), existing.getAddress(), cityCache, defaultNationCode);
    }
  }

  public static void convertFromXmlContact(List<Contact> in, AddressBean existing) {
    if (in == null || existing == null) {
      return;
    }
    List<ContactType> updated = new ArrayList<>();
    for (Contact contact : in) {
      if (CompareUtility.equals(contact.getType(), ContactType.PhoneNumber.toString()) && !updated.contains(ContactType.PhoneNumber)) {
        existing.setPhone(contact.getValue());
        updated.add(ContactType.PhoneNumber);
      }
      else if (StringUtility.equalsIgnoreCase(contact.getType(), ContactType.MobilePhoneNumber.toString()) && !updated.contains(ContactType.MobilePhoneNumber)) {
        existing.setMobile(contact.getValue());
        updated.add(ContactType.MobilePhoneNumber);
      }
      else if (StringUtility.equalsIgnoreCase(contact.getType(), ContactType.FaxNumber.toString()) && !updated.contains(ContactType.FaxNumber)) {
        existing.setFax(contact.getValue());
        updated.add(ContactType.FaxNumber);
      }
      else if (StringUtility.equalsIgnoreCase(contact.getType(), ContactType.EmailAddress.toString()) && !updated.contains(ContactType.EmailAddress)) {
        existing.setEmail(contact.getValue());
        updated.add(ContactType.EmailAddress);
      }
      else if (StringUtility.equalsIgnoreCase(contact.getType(), ContactType.WebAddress.toString()) && !updated.contains(ContactType.WebAddress)) {
        existing.setWww(contact.getValue());
        updated.add(ContactType.WebAddress);
      }
    }
  }

  public static void convertFromXmlAddress(Address in, AddressBean existing, CityDataCacher cityCache, String defaultNationCode) throws ProcessingException {
    if (in == null || existing == null) {
      throw new IllegalArgumentException("Arguments must not be null");
    }
    existing.setStreet(in.getStreet());
    Country country = in.getCountry();
    existing.setCityNr(importCity(in.getCity(), in.getZipCode(), country != null ? country.getCode() : null, defaultNationCode, cityCache));
  }

  protected static Long importCity(String city, String zipCode, String countryCode, String defaultNationCode, CityDataCacher cityCache) throws ProcessingException {
    if (cityCache == null) {
      throw new IllegalArgumentException("City cache must not be null");
    }

    String nationCode = null;
    if (!StringUtility.isNullOrEmpty(countryCode)) {
      nationCode = countryCode; // 3-digit nation code, not country code
    }
    else {
      nationCode = defaultNationCode;
    }

    // try to find city
    CityCacheKey cityTerm = new CityCacheKey(city, zipCode, nationCode);
    CityFormData cityFormData = cityCache.get(cityTerm);
    if (cityFormData.getCityNr() == null) {
      cityFormData = cityCache.put(cityTerm, cityFormData);
    }
    Long cityNr = cityFormData.getCityNr();
    return cityNr;
  }

  public static Long convertFromXmlControlType(ControlType type) {
    /* <xsd:enumeration value="Control"/>
    <xsd:enumeration value="Start"/>
    <xsd:enumeration value="Finish"/>
    <xsd:enumeration value="CrossingPoint"/>
    <xsd:enumeration value="EndOfMarkedRoute"/> */

    Long typeUid = null;
    if (ControlType.START.equals(type)) {
      typeUid = ControlTypeCodeType.StartCode.ID;
    }
    else if (ControlType.FINISH.equals(type)) {
      typeUid = ControlTypeCodeType.FinishCode.ID;
    }
    else if (ControlType.CONTROL.equals(type)) {
      typeUid = ControlTypeCodeType.ControlCode.ID;
    }
    return typeUid;
  }

  private static DatatypeFactory createFactory() throws ProcessingException {
    try {
      return DatatypeFactory.newInstance();
    }
    catch (DatatypeConfigurationException e) {
      throw new ProcessingException("Could not create XML DatatypeFactory", e);
    }
  }

}
