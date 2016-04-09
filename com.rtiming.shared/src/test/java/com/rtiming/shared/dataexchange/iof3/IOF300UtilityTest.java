package com.rtiming.shared.dataexchange.iof3;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.rtiming.shared.common.database.sql.AddressBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dataexchange.cache.CityCacheKey;
import com.rtiming.shared.dataexchange.cache.CityDataCacher;
import com.rtiming.shared.dataexchange.iof3.IOF300Utility.ContactType;
import com.rtiming.shared.dataexchange.iof3.xml.Address;
import com.rtiming.shared.dataexchange.iof3.xml.Contact;
import com.rtiming.shared.dataexchange.iof3.xml.ControlCard;
import com.rtiming.shared.dataexchange.iof3.xml.ControlType;
import com.rtiming.shared.dataexchange.iof3.xml.Country;
import com.rtiming.shared.dataexchange.iof3.xml.Event;
import com.rtiming.shared.dataexchange.iof3.xml.Organisation;
import com.rtiming.shared.dataexchange.iof3.xml.Person;
import com.rtiming.shared.dataexchange.iof3.xml.PersonName;
import com.rtiming.shared.dataexchange.iof3.xml.ResultStatus;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.city.CityFormData;

/**
 * @author amo
 */
public class IOF300UtilityTest {

  @Test
  public void testGetDatatypeFactory() throws Exception {
    DatatypeFactory factory = IOF300Utility.getDatatypeFactory();
    Assert.assertNotNull("Factory exists", factory);
    // test cache
    DatatypeFactory factory2 = IOF300Utility.getDatatypeFactory();
    Assert.assertEquals("Cache", factory, factory2);
  }

  @Test
  public void testToDoubleTime1() throws Exception {
    Double result = IOF300Utility.toDoubleTime(null);
    Assert.assertNull("Result is null", result);
  }

  @Test
  public void testToDoubleTime2() throws Exception {
    Double result = IOF300Utility.toDoubleTime(5000L);
    Assert.assertEquals("Double Time", 5d, result, 0d);
  }

  @Test
  public void testToDoubleTime3() throws Exception {
    Double result = IOF300Utility.toDoubleTime(12345L);
    Assert.assertEquals("Double Time", 12.345d, result, 0d);
  }

  @Test
  public void testConvertToXmlClass1() throws Exception {
    com.rtiming.shared.dataexchange.iof3.xml.Class result = IOF300Utility.convertToXmlClass(null);
    Assert.assertNotNull("Result not empty", result);
    Assert.assertNull(result.getShortName());
    Assert.assertNull(result.getName());
  }

  @Test
  public void testConvertToXmlClass2() throws Exception {
    com.rtiming.shared.dataexchange.iof3.xml.Class result = IOF300Utility.convertToXmlClass("HE");
    Assert.assertNotNull("Result not empty", result);
    Assert.assertEquals("HE", result.getShortName());
    Assert.assertEquals("HE", result.getName());
  }

  @Test
  public void testConvertToXmlDate1() throws Exception {
    XMLGregorianCalendar result = IOF300Utility.convertToXmlDate(null);
    Assert.assertNull("Null result", result);
  }

  @Test
  public void testConvertToXmlDate2() throws Exception {
    Date date = new Date();
    XMLGregorianCalendar result = IOF300Utility.convertToXmlDate(date);
    Assert.assertEquals("Same value", date, result.toGregorianCalendar().getTime());
  }

  @Test
  public void testConvertToXmlPerson1() throws Exception {
    Person result = IOF300Utility.convertToXmlPerson(null, null, null, null, null, null);
    Assert.assertNotNull("Person not null", result);
    Assert.assertNull(result.getName().getFamily());
    Assert.assertNull(result.getName().getGiven());
    Assert.assertNull(result.getSex());
    Assert.assertNull(result.getBirthDate());
    Assert.assertEquals("0 Id", 0, result.getId().size());
  }

  @Test
  public void testConvertToXmlPerson2() throws Exception {
    Date birthdate = DateUtility.parse("20120702", "yyyyMMdd");
    Person result = IOF300Utility.convertToXmlPerson("CP6MOA", "Latscha", "Jonathan", SexCodeType.ManCode.ID, 2012L, birthdate);
    Assert.assertNotNull("Person not null", result);
    Assert.assertEquals("Latscha", result.getName().getFamily());
    Assert.assertEquals("Jonathan", result.getName().getGiven());
    Assert.assertEquals("Men", "M", result.getSex());
    Assert.assertEquals("Birthdate", birthdate, result.getBirthDate().toGregorianCalendar().getTime());
    Assert.assertEquals("ExtKey", "CP6MOA", result.getId().get(0).getValue());
  }

  @Test
  public void testConvertToXmlPersonBirthdate1() throws Exception {
    Date birthdate = DateUtility.parse("20120702", "yyyyMMdd");
    Person result = IOF300Utility.convertToXmlPerson(null, "Latscha", "Jonathan", SexCodeType.ManCode.ID, null, birthdate);
    Assert.assertEquals("Birthdate", birthdate, result.getBirthDate().toGregorianCalendar().getTime());
  }

  @Test
  public void testConvertToXmlPersonBirthdate2() throws Exception {
    Date birthdate = DateUtility.parse("2012", "yyyy");
    Person result = IOF300Utility.convertToXmlPerson(null, "Latscha", "Jonathan", SexCodeType.ManCode.ID, 2012L, null);
    Assert.assertEquals("Birthdate", birthdate, result.getBirthDate().toGregorianCalendar().getTime());
  }

  @Test
  public void testConvertToCompetitorStatus1() throws Exception {
    testStatus(ResultStatus.DID_NOT_FINISH, RaceStatusCodeType.DidNotFinishCode.ID);
  }

  @Test
  public void testConvertToCompetitorStatus2() throws Exception {
    testStatus(ResultStatus.DID_NOT_START, RaceStatusCodeType.DidNotStartCode.ID);
  }

  @Test
  public void testConvertToCompetitorStatus3() throws Exception {
    testStatus(ResultStatus.DISQUALIFIED, RaceStatusCodeType.DisqualifiedCode.ID);
  }

  @Test
  public void testConvertToCompetitorStatus4() throws Exception {
    testStatus(ResultStatus.MISSING_PUNCH, RaceStatusCodeType.MissingPunchCode.ID);
  }

  @Test
  public void testConvertToCompetitorStatus5() throws Exception {
    testStatus(ResultStatus.MISSING_PUNCH, RaceStatusCodeType.NoStartTimeCode.ID);
  }

  @Test
  public void testConvertToCompetitorStatus6() throws Exception {
    testStatus(ResultStatus.OK, RaceStatusCodeType.OkCode.ID);
  }

  @Test
  public void testConvertToCompetitorStatus7() throws Exception {
    testStatus(ResultStatus.ACTIVE, 0L);
  }

  @Test
  public void testConvertToCompetitorStatus8() throws Exception {
    testStatus(ResultStatus.INACTIVE, null);
  }

  private void testStatus(ResultStatus expected, Long actual) {
    Assert.assertEquals(expected, IOF300Utility.convertToCompetitorStatus(actual));
  }

  @Test
  public void testConvertToXmlClub1() throws Exception {
    Organisation result = IOF300Utility.convertToXmlClub("Ostschweiz", "OLV", null);
    Assert.assertNotNull("Result not null", result);
    Assert.assertEquals("Name", "Ostschweiz", result.getName());
    Assert.assertEquals("Short Name", "OLV", result.getShortName());
    Assert.assertEquals("Type", "Club", result.getType());
    Assert.assertNull("Id null", result.getId());
  }

  @Test
  public void testConvertToXmlClub2() throws Exception {
    Organisation result = IOF300Utility.convertToXmlClub(null, null, null);
    Assert.assertNotNull("Result not null", result);
    Assert.assertNull("Name is null", result.getName());
    Assert.assertNull("Short Name is null", result.getShortName());
    Assert.assertEquals("Type", "Club", result.getType());
    Assert.assertNull("Id null", result.getId());
  }

  @Test
  public void testConvertToXmlClub3() throws Exception {
    Organisation result = IOF300Utility.convertToXmlClub("Ostschweiz", "OLV", "OLVO");
    Assert.assertNotNull("Result not null", result);
    Assert.assertEquals("Name", "Ostschweiz", result.getName());
    Assert.assertEquals("Short Name", "OLV", result.getShortName());
    Assert.assertEquals("Type", "Club", result.getType());
    Assert.assertNotNull("Id not null", result.getId());
    Assert.assertEquals("Id", "OLVO", result.getId().getValue());
  }

  @Test
  public void testConvertToXmlSex1() throws Exception {
    String result = IOF300Utility.convertToXmlSex(null);
    Assert.assertNull("Empty", result);
  }

  @Test
  public void testConvertToXmlSex2() throws Exception {
    String result = IOF300Utility.convertToXmlSex(0L);
    Assert.assertNull("Empty", result);
  }

  @Test
  public void testConvertToXmlSex3() throws Exception {
    String result = IOF300Utility.convertToXmlSex(SexCodeType.WomanCode.ID);
    Assert.assertEquals("Woman", "F", result);
  }

  @Test
  public void testConvertToXmlControlStatus1() throws Exception {
    String result = IOF300Utility.convertToXmlControlStatus(null);
    Assert.assertNull(result);
  }

  @Test
  public void testConvertToXmlControlStatus2() throws Exception {
    String result = IOF300Utility.convertToXmlControlStatus(ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("OK", result);
  }

  @Test
  public void testConvertToXmlControlStatus3() throws Exception {
    String result = IOF300Utility.convertToXmlControlStatus(ControlStatusCodeType.MissingCode.ID);
    Assert.assertEquals("Missing", result);
  }

  @Test
  public void testConvertToXmlControlStatus4() throws Exception {
    String result = IOF300Utility.convertToXmlControlStatus(ControlStatusCodeType.AdditionalCode.ID);
    Assert.assertEquals("Additional", result);
  }

  @Test
  public void testConvertToXmlControlStatus5() throws Exception {
    String result = IOF300Utility.convertToXmlControlStatus(ControlStatusCodeType.WrongCode.ID);
    Assert.assertEquals("Additional", result);
  }

  @Test
  public void testConvertToXmlControlStatus6() throws Exception {
    String result = IOF300Utility.convertToXmlControlStatus(0L);
    Assert.assertNull(result);
  }

  @Test
  public void testConvertToXmlAddress1() throws Exception {
    Address result = IOF300Utility.convertToXmlAddress(null, null);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getCountry());
  }

  @Test
  public void testConvertToXmlAddress2() throws Exception {
    Address result = IOF300Utility.convertToXmlAddress("CITY", 999L);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getCountry());
    Assert.assertEquals("City", "CITY", result.getCity());
    Assert.assertEquals("Country", "999", result.getCountry().getValue());
  }

  @Test
  public void testConvertToXmlECard1() throws Exception {
    ControlCard result = IOF300Utility.convertToXmlECard("12345");
    Assert.assertNotNull(result);
    Assert.assertEquals("12345", result.getValue());
  }

  @Test
  public void testConvertToXmlECard2() throws Exception {
    ControlCard result = IOF300Utility.convertToXmlECard(null);
    Assert.assertNull(result);
  }

  @Test
  public void testConvertToXmlEvent1() throws Exception {
    Event result = IOF300Utility.convertToXmlEvent(null);
    Assert.assertNotNull(result);
    Assert.assertNull("0 id", result.getId());
  }

  @Test
  public void testConvertToXmlEvent2() throws Exception {
    Event result = IOF300Utility.convertToXmlEvent(11L);
    Assert.assertNotNull(result);
    Assert.assertNotNull("Id", result.getId());
    Assert.assertEquals("Id", "11", result.getId().getValue());
  }

  @Test
  public void testGetIofVersion() throws Exception {
    String result = IOF300Utility.getIOFVersion();
    Assert.assertEquals("3.0.0", result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertFromXmlPerson1() throws Exception {
    IOF300Utility.convertFromXmlPerson(null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertFromXmlPerson2() throws Exception {
    IOF300Utility.convertFromXmlPerson(null, Mockito.mock(RunnerBean.class), null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertFromXmlPerson3() throws Exception {
    IOF300Utility.convertFromXmlPerson(Mockito.mock(Person.class), null, null, null);
  }

  @Test
  public void testConvertFromXmlPerson4() throws Exception {
    Person person = new Person();
    RunnerBean runner = new RunnerBean();
    IOF300Utility.convertFromXmlPerson(person, runner, new CityDataCacher(), "SUI");
  }

  @Test
  public void testConvertFromXmlPerson5() throws Exception {
    Person person = new Person();
    PersonName personName = new PersonName();
    personName.setFamily("Latscha");
    personName.setGiven("Jonathan");
    person.setName(personName);
    RunnerBean runner = new RunnerBean();
    IOF300Utility.convertFromXmlPerson(person, runner, new CityDataCacher(), "SUI");
    assertEquals("Last name", person.getName().getFamily(), runner.getLastName());
    assertEquals("First name", person.getName().getGiven(), runner.getFirstName());
  }

  @Test
  public void testConvertFromXmlPerson6() throws Exception {
    Person person = new Person();
    Date testDate = DateUtility.parse("20120702", "yyyyMMdd");
    person.setBirthDate(IOF300Utility.convertToXmlDate(testDate));
    RunnerBean runner = new RunnerBean();
    IOF300Utility.convertFromXmlPerson(person, runner, new CityDataCacher(), "SUI");
    assertEquals("Birth Date", testDate, runner.getEvtBirth());
  }

  @Test
  public void testConvertFromXmlPerson7() throws Exception {
    Person person = new Person();
    person.getContact().add(createContact(ContactType.EmailAddress.toString(), "info@4mila.ch"));
    RunnerBean runner = new RunnerBean();
    IOF300Utility.convertFromXmlPerson(person, runner, new CityDataCacher(), "SUI");
    assertEquals("E-Mail set", "info@4mila.ch", runner.getAddress().getEmail());
  }

  @Test
  public void testConvertFromXmlPerson8() throws Exception {
    Person person = new Person();
    person.getContact().add(createContact(ContactType.EmailAddress.toString(), "info@4mila.ch"));
    person.getContact().add(createContact(ContactType.EmailAddress.toString(), "additional"));
    RunnerBean runner = new RunnerBean();
    IOF300Utility.convertFromXmlPerson(person, runner, new CityDataCacher(), "SUI");
    assertEquals("E-Mail set", "info@4mila.ch", runner.getAddress().getEmail());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertFromXmlAddress1() throws Exception {
    IOF300Utility.convertFromXmlAddress(null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertFromXmlAddress2() throws Exception {
    IOF300Utility.convertFromXmlAddress(Mockito.mock(Address.class), null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertFromXmlAddress3() throws Exception {
    IOF300Utility.convertFromXmlAddress(null, Mockito.mock(AddressBean.class), null, null);
  }

  @Test
  public void testConvertFromXmlAddress4() throws Exception {
    Address address = new Address();
    address.setStreet("STREET");
    AddressBean existing = new AddressBean();
    existing.setEmail("jf@latscha.com");
    IOF300Utility.convertFromXmlAddress(address, existing, getCityDataCacherMock(null), "SUI");
    Assert.assertEquals("Value Set", address.getStreet(), existing.getStreet());
    Assert.assertEquals("Value Not Changed", "jf@latscha.com", existing.getEmail());
    Assert.assertEquals("City", 999, existing.getCityNr().longValue());
  }

  @Test
  public void testConvertFromXmlAddress5() throws Exception {
    Address address = new Address();
    address.setCity("City1");
    address.setZipCode("9999");
    AddressBean existing = new AddressBean();
    IOF300Utility.convertFromXmlAddress(address, existing, getCityDataCacherMock(888L), "SUI");
    Assert.assertEquals("City", 888, existing.getCityNr().longValue());
  }

  @Test
  public void testConvertFromXmlAddress6() throws Exception {
    Address address = new Address();
    address.setCity("City1");
    Country country = new Country();
    address.setCountry(country);
    AddressBean existing = new AddressBean();
    IOF300Utility.convertFromXmlAddress(address, existing, getCityDataCacherMock(888L), "SUI");
    Assert.assertEquals("City", 888, existing.getCityNr().longValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImportCity1() throws Exception {
    IOF300Utility.importCity(null, null, null, null, null);
  }

  @Test
  public void testImportCity2() throws Exception {
    Long cityNr = IOF300Utility.importCity(null, null, null, null, getCityDataCacherMock(null));
    Assert.assertEquals("CityNr", 999, cityNr.longValue());
  }

  @Test
  public void testConvertFromXmlControlType1() throws Exception {
    ControlType type = ControlType.CONTROL;
    long resultUid = IOF300Utility.convertFromXmlControlType(type);
    Assert.assertEquals("Converted Control Type", resultUid, ControlTypeCodeType.ControlCode.ID);
  }

  @Test
  public void testConvertFromXmlControlType2() throws Exception {
    ControlType type = ControlType.START;
    long resultUid = IOF300Utility.convertFromXmlControlType(type);
    Assert.assertEquals("Converted Control Type", resultUid, ControlTypeCodeType.StartCode.ID);
  }

  @Test
  public void testConvertFromXmlControlType3() throws Exception {
    ControlType type = ControlType.FINISH;
    long resultUid = IOF300Utility.convertFromXmlControlType(type);
    Assert.assertEquals("Converted Control Type", resultUid, ControlTypeCodeType.FinishCode.ID);
  }

  @Test
  public void testConvertFromXmlControlType4() throws Exception {
    ControlType type = null;
    Long resultUid = IOF300Utility.convertFromXmlControlType(type);
    Assert.assertNull("Converted Control Type", resultUid);
  }

  protected CityDataCacher getCityDataCacherMock(Long cachedCityNr) throws ProcessingException {
    CityDataCacher mock = Mockito.mock(CityDataCacher.class);
    CityFormData formData = new CityFormData();
    formData.setCityNr(cachedCityNr);
    Mockito.when(mock.get(Mockito.any(CityCacheKey.class))).thenReturn(formData);
    CityFormData formData2 = new CityFormData();
    formData2.setCityNr(999L);
    Mockito.when(mock.put(Mockito.any(CityCacheKey.class), Mockito.any(CityFormData.class))).thenReturn(formData2);
    return mock;
  }

  @Test
  public void testConvertFromXmlContact1() throws Exception {
    IOF300Utility.convertFromXmlContact(null, null);
  }

  @Test
  public void testConvertFromXmlContact2() throws Exception {
    IOF300Utility.convertFromXmlContact(new ArrayList<Contact>(), null);
  }

  @Test
  public void testConvertFromXmlContact3() throws Exception {
    IOF300Utility.convertFromXmlContact(null, new AddressBean());
  }

  @Test
  public void testConvertFromXmlContact4() throws Exception {
    List<Contact> in = new ArrayList<Contact>();
    Contact contact = new Contact();
    in.add(contact);
    AddressBean existing = new AddressBean();
    IOF300Utility.convertFromXmlContact(in, existing);
    assertContactValues(existing, null, null, null, null, null);
  }

  @Test
  public void testConvertFromXmlContact5() throws Exception {
    List<Contact> in = new ArrayList<Contact>();
    in.add(createContact(ContactType.EmailAddress.toString(), "info@4mila.com"));
    AddressBean existing = new AddressBean();
    IOF300Utility.convertFromXmlContact(in, existing);
    assertContactValues(existing, "info@4mila.com", null, null, null, null);
  }

  @Test
  public void testConvertFromXmlContact6() throws Exception {
    List<Contact> in = new ArrayList<Contact>();
    in.add(createContact(ContactType.EmailAddress.toString(), "info@4mila.com"));
    in.add(createContact(ContactType.FaxNumber.toString(), "Fax"));
    in.add(createContact(ContactType.MobilePhoneNumber.toString(), "Mobile"));
    in.add(createContact(ContactType.WebAddress.toString(), "Web"));
    in.add(createContact(ContactType.PhoneNumber.toString(), "Phone"));
    AddressBean existing = new AddressBean();
    IOF300Utility.convertFromXmlContact(in, existing);
    assertContactValues(existing, "info@4mila.com", "Fax", "Mobile", "Phone", "Web");
  }

  @Test
  public void testConvertFromXmlContact7() throws Exception {
    List<Contact> in = new ArrayList<Contact>();
    in.add(createContact(ContactType.EmailAddress.toString().toUpperCase(), "info@4mila.com"));
    AddressBean existing = new AddressBean();
    IOF300Utility.convertFromXmlContact(in, existing);
    assertContactValues(existing, "info@4mila.com", null, null, null, null);
  }

  @Test
  public void testConvertFromXmlContact8() throws Exception {
    List<Contact> in = new ArrayList<Contact>();
    in.add(createContact(ContactType.EmailAddress.toString(), "info@4mila.com"));
    in.add(createContact(ContactType.EmailAddress.toString(), "additional"));
    in.add(createContact(ContactType.FaxNumber.toString(), "Fax"));
    in.add(createContact(ContactType.FaxNumber.toString(), "additional"));
    in.add(createContact(ContactType.MobilePhoneNumber.toString(), "Mobile"));
    in.add(createContact(ContactType.MobilePhoneNumber.toString(), "additional"));
    in.add(createContact(ContactType.WebAddress.toString(), "Web"));
    in.add(createContact(ContactType.WebAddress.toString(), "additional"));
    in.add(createContact(ContactType.PhoneNumber.toString(), "Phone"));
    in.add(createContact(ContactType.PhoneNumber.toString(), "additional"));
    AddressBean existing = new AddressBean();
    IOF300Utility.convertFromXmlContact(in, existing);
    assertContactValues(existing, "info@4mila.com", "Fax", "Mobile", "Phone", "Web");
  }

  private Contact createContact(String type, String value) {
    Contact contact = new Contact();
    contact.setType(type);
    contact.setValue(value);
    return contact;
  }

  private void assertContactValues(AddressBean existing, String email, String fax, String mobile, String phone, String www) {
    assertEquals(existing.getEmail(), email);
    assertEquals(existing.getFax(), fax);
    assertEquals(existing.getMobile(), mobile);
    assertEquals(existing.getPhone(), phone);
    assertEquals(existing.getWww(), www);
  }

}
