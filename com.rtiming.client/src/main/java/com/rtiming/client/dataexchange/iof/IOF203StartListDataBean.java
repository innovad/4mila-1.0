package com.rtiming.client.dataexchange.iof;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;

import com.rtiming.client.entry.AbstractEntriesTablePage;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dataexchange.AbstractXMLDataBean;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.iof203.xml.BirthDate;
import com.rtiming.shared.dataexchange.iof203.xml.CCard;
import com.rtiming.shared.dataexchange.iof203.xml.ClassShortName;
import com.rtiming.shared.dataexchange.iof203.xml.ClassStart;
import com.rtiming.shared.dataexchange.iof203.xml.Clock;
import com.rtiming.shared.dataexchange.iof203.xml.Club;
import com.rtiming.shared.dataexchange.iof203.xml.CountryId;
import com.rtiming.shared.dataexchange.iof203.xml.Date;
import com.rtiming.shared.dataexchange.iof203.xml.Given;
import com.rtiming.shared.dataexchange.iof203.xml.Nationality;
import com.rtiming.shared.dataexchange.iof203.xml.Person;
import com.rtiming.shared.dataexchange.iof203.xml.PersonId;
import com.rtiming.shared.dataexchange.iof203.xml.PersonName;
import com.rtiming.shared.dataexchange.iof203.xml.PersonStart;
import com.rtiming.shared.dataexchange.iof203.xml.Start;
import com.rtiming.shared.dataexchange.iof203.xml.StartList;
import com.rtiming.shared.dataexchange.iof203.xml.StartTime;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.runner.SexCodeType;

public class IOF203StartListDataBean extends AbstractXMLDataBean {

  private static final long serialVersionUID = 1L;
  private final Long eventNr;
  private final ITableRow startListRow;

  public IOF203StartListDataBean(Long primaryKeyNr, ITableRow row, Long eventNr) {
    super(primaryKeyNr);
    this.eventNr = eventNr;
    this.startListRow = row;
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {

  }

  @Override
  public void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {

  }

  @Override
  public Object createXMLObject(Object mainObject) throws ProcessingException {
    AbstractEntriesTablePage.Table table = (AbstractEntriesTablePage.Table) startListRow.getTable();

    StartList startlist = (StartList) mainObject;
    ClassStart classStart = new ClassStart();

    // Class
    Long classUid = table.getClazzShortcutColumn().getValue(startListRow);
    String classShortName = FMilaUtility.getCodeExtKey(ClassCodeType.class, classUid);

    // Check for existing class
    boolean exists = false;
    List<ClassStart> classStarts = startlist.getClassStart();
    if (classStarts.size() > 0) {
      for (ClassStart cs : classStarts) {
        if (cs.getClassIdOrClassShortNameOrEventClass().size() > 0) {
          ClassShortName shortName = (ClassShortName) cs.getClassIdOrClassShortNameOrEventClass().get(0);
          if (StringUtility.equalsIgnoreCase(classShortName, shortName.getvalue())) {
            classStart = cs;
            exists = true;
          }
        }
      }
    }

    if (!exists) {
      // Create new class
      classStart = new ClassStart();

      // Class Short Name
      ClassShortName csn = new ClassShortName();
      csn.setvalue(classShortName);
      classStart.getClassIdOrClassShortNameOrEventClass().add(csn);

      // Add to start list
      classStarts.add(classStart);
    }

    // Single Person
    PersonStart p = new PersonStart();
    Person person = new Person();

    // Sex
    Long sexUid = table.getSexColumn().getValue(startListRow);
    String sex = null;
    if (CompareUtility.equals(sexUid, SexCodeType.ManCode.ID)) {
      sex = "M";
    }
    else if (CompareUtility.equals(sexUid, SexCodeType.WomanCode.ID)) {
      sex = "F";
    }
    person.setSex(sex);

    // Birthdate
    if (table.getBirthdateColumn().getValue(startListRow) != null) {
      BirthDate birthDate = new BirthDate();
      Date date = new Date();
      date.setDateFormat("DD.MM.YYYY");
      date.setvalue(DateUtility.format(table.getBirthdateColumn().getValue(startListRow), "dd.mm.yyyy"));
      birthDate.setDate(date);
      person.setBirthDate(birthDate);
    }

    // Nation
    Nationality nation = new Nationality();
    CountryId countryId = new CountryId();
    countryId.setValue(table.getNationColumn().getValue(startListRow));
    nation.getCountryIdOrCountry().add(countryId);
    person.setNationality(nation);

    // Id
    if (!StringUtility.isNullOrEmpty(table.getExtKeyColumn().getValue(startListRow))) {
      PersonId pid = new PersonId();
      pid.setvalue(table.getExtKeyColumn().getValue(startListRow));
      person.setPersonId(pid);
    }

    // Person
    PersonName personName = new PersonName();
    Given g = new Given();
    g.setSequence("1");
    g.setvalue(table.getFirstNameColumn().getValue(startListRow));
    personName.setFamily(table.getLastNameColumn().getValue(startListRow));
    personName.getGiven().add(g);
    person.setPersonName(personName);

    p.getPersonIdOrPerson().add(person);

    // Start
    Start start = new Start();
    start.setBibNumber(table.getBibNumberColumn().getValue(startListRow));
    start.setStartId("" + table.getRaceNrColumn().getValue(startListRow));
    start.setStartNumber(table.getBibNumberColumn().getValue(startListRow));
    StartTime startTime = new StartTime();
    Clock clock = new Clock();
    clock.setClockFormat("HH:MM:SS");

    clock.setvalue(DateUtility.format(table.getStartTimeColumn().getValue(startListRow), "hh:mm:ss"));
    startTime.setClock(clock);
    start.setStartTime(startTime);

    // E-Card
    if (!StringUtility.isNullOrEmpty(table.getECardColumn().getValue(startListRow))) {
      CCard ccard = IOF203Utility.convertToXmlECard(table.getECardColumn().getValue(startListRow));
      start.getCCardIdOrCCard().add(ccard);
    }

    // Club
    String name = table.getClubColumn().getValue(startListRow);
    String shortcut = table.getClubShortcutColumn().getValue(startListRow);
    String extKey = table.getClubExtKeyColumn().getValue(startListRow);
    Club club = IOF203Utility.convertToXmlClub(name, shortcut, extKey);
    p.getClubIdOrClubOrCountryIdOrCountry().add(club);

    p.getStartOrRaceStart().add(start);

    classStart.getPersonStartOrTeamStart().add(p);

    return startlist;
  }

  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    if (startListRow != null) {
      AbstractEntriesTablePage.Table table = (AbstractEntriesTablePage.Table) startListRow.getTable();
      buf.append("[");
      buf.append(table.getRunnerColumn().getValue(startListRow));
      buf.append("]");
    }
    return buf.toString();
  }

  @Override
  public ArrayList<String> getPreviewRowData() throws ProcessingException {
    AbstractEntriesTablePage.Table table = (AbstractEntriesTablePage.Table) startListRow.getTable();

    ArrayList<String> list = new ArrayList<String>();
    list.add(FMilaUtility.getCodeText(ClassCodeType.class, table.getClazzShortcutColumn().getValue(startListRow)));
    list.add(table.getRunnerColumn().getValue(startListRow));
    return list;
  }

}
