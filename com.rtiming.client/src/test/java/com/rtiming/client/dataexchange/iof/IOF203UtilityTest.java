package com.rtiming.client.dataexchange.iof;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dataexchange.iof203.xml.CCard;
import com.rtiming.shared.dataexchange.iof203.xml.CCardId;
import com.rtiming.shared.dataexchange.iof203.xml.Clock;
import com.rtiming.shared.dataexchange.iof203.xml.Club;
import com.rtiming.shared.dataexchange.iof203.xml.Date;
import com.rtiming.shared.dataexchange.iof203.xml.Event;
import com.rtiming.shared.dataexchange.iof203.xml.Given;
import com.rtiming.shared.dataexchange.iof203.xml.IOFVersion;
import com.rtiming.shared.dataexchange.iof203.xml.ModifyDate;
import com.rtiming.shared.dataexchange.iof203.xml.Person;
import com.rtiming.shared.dataexchange.iof203.xml.PersonName;
import com.rtiming.shared.dataexchange.iof203.xml.PunchingUnitType;
import com.rtiming.shared.dataexchange.iof203.xml.Time;
import com.rtiming.shared.race.RaceStatusCodeType;

public class IOF203UtilityTest {

  @Test
  public void testConvertFromXmlECard() throws ProcessingException {
    CCard card = new CCard();
    card.setCCardId(new CCardId());
    card.getCCardId().setvalue("12345");
    PunchingUnitType punchingSystem = new PunchingUnitType();
    punchingSystem.setValue("EMIT");
    card.setPunchingUnitType(punchingSystem);
    RtEcard formData = IOF203Utility.convertFromXmlECard(card);

    Assert.assertNull("Not SI Card", formData);
  }

  @Test
  public void testConvertFromXmlPerson1() {
    Person person = new Person();
    RunnerBean runner = new RunnerBean();
    RunnerBean result = IOF203Utility.convertFromXmlPerson(person, runner);
    Assert.assertNull("Last Name", result.getLastName());
    Assert.assertNull("First Name", result.getFirstName());
  }

  @Test
  public void testConvertFromXmlPerson2() {
    Person person = new Person();
    PersonName personName = new PersonName();
    personName.setFamily("Latscha");
    Given given1 = new Given();
    given1.setvalue("Jonathan");
    personName.getGiven().add(given1);
    Given given2 = new Given();
    given2.setvalue("Felix");
    personName.getGiven().add(given2);
    person.setPersonName(personName);
    RunnerBean runner = new RunnerBean();
    RunnerBean result = IOF203Utility.convertFromXmlPerson(person, runner);
    Assert.assertEquals("Last Name", "Latscha", result.getLastName());
    Assert.assertEquals("First Name", "Jonathan Felix", result.getFirstName());
  }

  @Test
  public void testConvertFromXmlPersonNull() {
    RunnerBean result = IOF203Utility.convertFromXmlPerson(null, null);
    Assert.assertNull("Runner null", result);
  }

  @Test
  public void testConvertToCompetitorStatus1() {
    String result = IOF203Utility.convertToCompetitorStatus(RaceStatusCodeType.DidNotFinishCode.ID);
    Assert.assertEquals("DidNotFinish", result);
  }

  @Test
  public void testConvertToCompetitorStatusNull() {
    String result = IOF203Utility.convertToCompetitorStatus(null);
    Assert.assertEquals("Inactive", result);
  }

  @Test
  public void testConvertToXmlClock() {
    Clock clock = IOF203Utility.convertToXmlClock(DateUtility.parse("01:02:03", "HH:mm:ss"));
    Assert.assertEquals("Clock", "01:02:03", clock.getvalue());
  }

  @Test
  public void testConvertToXmlClockNull() {
    Clock clock = IOF203Utility.convertToXmlClock(null);
    Assert.assertNull("Clock null", clock.getvalue());
  }

  @Test
  public void testConvertToXmlClub() {
    Club club = IOF203Utility.convertToXmlClub("Name", "Shortcut", "extKey");
    Assert.assertEquals("Name", "Name", club.getName().getvalue());
    Assert.assertEquals("Shortcut", "Shortcut", club.getShortName().getvalue());
    Assert.assertEquals("extKey", "extKey", club.getClubId().getvalue());
  }

  @Test
  public void testConvertToXmlClubNull() {
    Club club = IOF203Utility.convertToXmlClub(null, null, null);
    Assert.assertEquals("Name", null, club.getName().getvalue());
    Assert.assertEquals("Shortcut", null, club.getShortName());
    Assert.assertEquals("extKey", null, club.getClubId());
  }

  @Test
  public void testConvertToXmlDate() {
    Date date = IOF203Utility.convertToXmlDate(DateUtility.parse("2012-07-02", "yyyy-MM-dd"));
    Assert.assertEquals("Date", "2012-07-02", date.getvalue());
  }

  @Test
  public void testConvertToXmlDateNull() {
    Date date = IOF203Utility.convertToXmlDate(null);
    Assert.assertNull("Date null", date.getvalue());
  }

  @Test
  public void testConvertToXmlECard() {
    CCard card = IOF203Utility.convertToXmlECard("12345");
    Assert.assertNotNull(card);
    Assert.assertEquals("Card", "12345", card.getCCardId().getvalue());
  }

  @Test
  public void testConvertToXmlECardNull() {
    CCard card = IOF203Utility.convertToXmlECard(null);
    Assert.assertNotNull(card);
    Assert.assertEquals("Card", null, card.getCCardId().getvalue());
  }

  @Test
  public void testConvertToXmlPerson() {
    Person person = IOF203Utility.convertToXmlPerson("LastName", "FirstName");
    Assert.assertNotNull(person);
    Assert.assertEquals("LastName", "LastName", person.getPersonName().getFamily());
    Assert.assertEquals("FirstName", "FirstName", person.getPersonName().getGiven().get(0).getvalue());
  }

  @Test
  public void testConvertToXmlPersonNull() {
    Person person = IOF203Utility.convertToXmlPerson(null, null);
    Assert.assertNotNull(person);
    Assert.assertEquals("LastName", null, person.getPersonName().getFamily());
    Assert.assertEquals("FirstName", null, person.getPersonName().getGiven().get(0).getvalue());
  }

  @Test
  public void testConvertToXmlTime() {
    Time time = IOF203Utility.convertToXmlTime("12345");
    Assert.assertNotNull(time);
    Assert.assertEquals("Time", "12345", time.getvalue());
  }

  @Test
  public void testConvertToXmlTimeNull() {
    Time time = IOF203Utility.convertToXmlTime(null);
    Assert.assertNotNull(time);
    Assert.assertEquals("Time", null, time.getvalue());
  }

  @Test
  public void testGetEvent() throws ProcessingException {
    Event event = IOF203Utility.getEvent(null);
    Assert.assertNotNull(event);
  }

  @Test
  public void testGetIOFVersion() {
    IOFVersion version = IOF203Utility.getIOFVersion203();
    Assert.assertEquals("2.0.3", version.getVersion());
  }

  @Test
  public void testGetModifyDate() {
    ModifyDate date = IOF203Utility.getModifyDate();
    Assert.assertNotNull(date);
  }

}
