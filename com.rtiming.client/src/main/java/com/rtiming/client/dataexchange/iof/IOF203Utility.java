package com.rtiming.client.dataexchange.iof;

import java.text.SimpleDateFormat;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dataexchange.iof203.xml.CCard;
import com.rtiming.shared.dataexchange.iof203.xml.CCardId;
import com.rtiming.shared.dataexchange.iof203.xml.Clock;
import com.rtiming.shared.dataexchange.iof203.xml.Club;
import com.rtiming.shared.dataexchange.iof203.xml.ClubId;
import com.rtiming.shared.dataexchange.iof203.xml.Date;
import com.rtiming.shared.dataexchange.iof203.xml.Event;
import com.rtiming.shared.dataexchange.iof203.xml.EventId;
import com.rtiming.shared.dataexchange.iof203.xml.FinishDate;
import com.rtiming.shared.dataexchange.iof203.xml.Given;
import com.rtiming.shared.dataexchange.iof203.xml.IOFVersion;
import com.rtiming.shared.dataexchange.iof203.xml.ModifyDate;
import com.rtiming.shared.dataexchange.iof203.xml.Name;
import com.rtiming.shared.dataexchange.iof203.xml.Organiser;
import com.rtiming.shared.dataexchange.iof203.xml.Person;
import com.rtiming.shared.dataexchange.iof203.xml.PersonName;
import com.rtiming.shared.dataexchange.iof203.xml.PunchingUnitType;
import com.rtiming.shared.dataexchange.iof203.xml.ShortName;
import com.rtiming.shared.dataexchange.iof203.xml.StartDate;
import com.rtiming.shared.dataexchange.iof203.xml.Time;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.race.RaceStatusCodeType;

public final class IOF203Utility {

  private IOF203Utility() {
  }

  public static CCard convertToXmlECard(String eCardNo) {
    CCard ccard = new CCard();
    CCardId ccardid = new CCardId();
    ccardid.setvalue(eCardNo);
    ccard.setCCardId(ccardid);
    PunchingUnitType punchType = new PunchingUnitType();
    punchType.setValue("SI");
    ccard.setPunchingUnitType(punchType);
    return ccard;
  }

  public static RtEcard convertFromXmlECard(CCard card) throws ProcessingException {
    boolean isSICard = true;
    RtEcard ecard = null;
    PunchingUnitType type = card.getPunchingUnitType();
    if (type != null && !StringUtility.isNullOrEmpty(type.getValue())) {
      if (!"SI".equalsIgnoreCase(type.getValue())) {
        isSICard = false;
      }
    }
    if (isSICard && !StringUtility.isNullOrEmpty(card.getCCardId().getvalue())) {
      ecard = BEANS.get(IECardProcessService.class).findECard(card.getCCardId().getvalue());
    }
    return ecard;
  }

  public static Person convertToXmlPerson(String lastName, String firstName) {
    Person person = new Person();
    PersonName personName = new PersonName();
    personName.setFamily(lastName);
    Given given = new Given();
    given.setvalue(firstName);
    given.setSequence("1");
    personName.getGiven().add(given);
    person.setPersonName(personName);
    return person;
  }

  public static RunnerBean convertFromXmlPerson(Person person, RunnerBean runner) {
    if (person != null) {
      PersonName personName = person.getPersonName();
      if (personName != null) {
        runner.setLastName(personName.getFamily());
        StringBuilder firstName = new StringBuilder();
        for (Given given : personName.getGiven()) {
          if (firstName.length() > 0) {
            firstName.append(" ");
          }
          firstName.append(given.getvalue());
        }
        runner.setFirstName(firstName.toString());
      }
    }
    return runner;
  }

  public static Club convertToXmlClub(String name, String shortcut, String extKey) {
    Club club = new Club();
    Name clubName = new Name();
    clubName.setvalue(name);
    club.setName(clubName);
    if (!StringUtility.isNullOrEmpty(shortcut)) {
      ShortName shortName = new ShortName();
      shortName.setvalue(shortcut);
      club.setShortName(shortName);
    }
    if (!StringUtility.isNullOrEmpty(extKey)) {
      ClubId clubId = new ClubId();
      clubId.setvalue(extKey);
      club.setClubId(clubId);
    }
    return club;
  }

  public static Date convertToXmlDate(java.util.Date value) {
    Date date = new Date();
    date.setDateFormat("YYYY-MM-DD");
    if (value != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      date.setvalue(sdf.format(value));
    }
    return date;
  }

  public static Clock convertToXmlClock(java.util.Date value) {
    Clock time = new Clock();
    time.setClockFormat("HH:MM:SS");
    if (value != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
      time.setvalue(sdf.format(value));
    }
    return time;
  }

  public static Time convertToXmlTime(String value) {
    Time time = new Time();
    time.setTimeFormat("MM:SS");
    time.setvalue(value);
    return time;
  }

  public static IOFVersion getIOFVersion203() {
    IOFVersion version = new IOFVersion();
    version.setVersion("2.0.3");
    return version;
  }

  public static ModifyDate getModifyDate() {
    ModifyDate modifyDate = new ModifyDate();
    java.util.Date now = new java.util.Date();
    modifyDate.setClock(IOF203Utility.convertToXmlClock(now));
    modifyDate.setDate(IOF203Utility.convertToXmlDate(now));
    return modifyDate;
  }

  public static Event getEvent(Long eventNr) throws ProcessingException {
    Event event = new Event();

    EventBean eventData = new EventBean();
    if (eventNr != null) {
      eventData.setEventNr(eventNr);
      eventData = BEANS.get(IEventProcessService.class).load(eventData);
    }

    Name name = new Name();
    name.setvalue(eventData.getName());
    event.setName(name);

    FinishDate finishDate = new FinishDate();
    finishDate.setDate(IOF203Utility.convertToXmlDate(eventData.getEvtZero()));
    event.setFinishDate(finishDate);

    StartDate startDate = new StartDate();
    startDate.setDate(IOF203Utility.convertToXmlDate(eventData.getEvtZero()));
    startDate.setClock(IOF203Utility.convertToXmlClock(eventData.getEvtZero()));
    event.setStartDate(startDate);

    event.setEventForm("IndSingleDay");

    Organiser organiser = new Organiser();
    Club club = new Club();
    Name clubName = new Name();
    clubName.setvalue(""); // TODO add a club on event
    club.setName(clubName);
    organiser.getClubIdOrClub().add(club);
    event.setOrganiser(organiser);

    // Event Id
    EventId eventId = new EventId();
    if (eventNr != null) {
      eventId.setvalue(eventData.getEventNr().toString());
      event.setEventId(eventId);
    }

    return event;
  }

  public static String convertToCompetitorStatus(Long raceStatusUid) {

    /* The status of the competitor or team at the time of the result
    generation:
    - Inactive   : Has not yet started
    - DidNotStart  : Did Not Start (in this race)
    - Active   : Currently on course
    - Finished   : Finished but not validated
    - OK     : Finished and validated
    - MisPunch   : Missing Punch
    - DidNotFinish : Did Not Finish
    - Disqualified : Disqualified
    - NotCompeting : Not Competing (running outside the competition)
    - SportWithdr  : Sporting Withdrawal (e.g. helping injured)
    - OverTime   : Overtime, i.e. did not finish within max time
    - Moved    : Moved to another class
    - MovedUp    : Moved to a "better" class, in case of entry
             restrictions
    - Cancelled  : The competitor has cancelled his/hers entry */

    if (raceStatusUid == null) {
      return "Inactive";
    }
    else if (raceStatusUid == RaceStatusCodeType.OkCode.ID) {
      return "OK";
    }
    else if (raceStatusUid == RaceStatusCodeType.DidNotFinishCode.ID) {
      return "DidNotFinish";
    }
    else if (raceStatusUid == RaceStatusCodeType.DidNotStartCode.ID) {
      return "DidNotStart";
    }
    else if (raceStatusUid == RaceStatusCodeType.DisqualifiedCode.ID) {
      return "Disqualified";
    }
    else if (raceStatusUid == RaceStatusCodeType.NoStartTimeCode.ID) {
      return "MisPunch";
    }
    else if (raceStatusUid == RaceStatusCodeType.MissingPunchCode.ID) {
      return "MisPunch";
    }

    return new String();
  }

}
