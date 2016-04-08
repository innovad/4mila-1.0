package com.rtiming.client.dataexchange.iof3;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;

import com.rtiming.client.entry.AbstractEntriesTablePage;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dataexchange.AbstractXMLDataBean;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.iof3.IOF300Utility;
import com.rtiming.shared.dataexchange.iof3.xml.ClassStart;
import com.rtiming.shared.dataexchange.iof3.xml.ControlCard;
import com.rtiming.shared.dataexchange.iof3.xml.Country;
import com.rtiming.shared.dataexchange.iof3.xml.Organisation;
import com.rtiming.shared.dataexchange.iof3.xml.Person;
import com.rtiming.shared.dataexchange.iof3.xml.PersonRaceStart;
import com.rtiming.shared.dataexchange.iof3.xml.PersonStart;
import com.rtiming.shared.dataexchange.iof3.xml.StartList;
import com.rtiming.shared.event.course.ClassCodeType;

public class IOF300StartListDataBean extends AbstractXMLDataBean {

  private static final long serialVersionUID = 1L;
  private final Long eventNr;
  private final ITableRow startListRow;

  public IOF300StartListDataBean(Long primaryKeyNr, ITableRow row, Long eventNr) {
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
        if (cs.getClazz() != null) {
          com.rtiming.shared.dataexchange.iof3.xml.Class clazz = cs.getClazz();
          if (StringUtility.equalsIgnoreCase(classShortName, clazz.getShortName())) {
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
      classStart.setClazz(IOF300Utility.convertToXmlClass(classShortName));

      // Add to start list
      classStarts.add(classStart);
    }

    // Single Person
    PersonStart personStart = new PersonStart();
    classStart.getPersonStart().add(personStart);

    Person person = IOF300Utility.convertToXmlPerson(
        table.getExtKeyColumn().getValue(startListRow),
        table.getLastNameColumn().getValue(startListRow),
        table.getFirstNameColumn().getValue(startListRow),
        table.getSexColumn().getValue(startListRow),
        table.getYearColumn().getValue(startListRow),
        table.getBirthdateColumn().getValue(startListRow));
    personStart.setPerson(person);

    // Nation
    Country country = new Country();
    country.setCode(table.getNationColumn().getValue(startListRow));
    personStart.getPerson().setNationality(country);

    // E-Card
    PersonRaceStart personRaceStart = new PersonRaceStart();
    personStart.getStart().add(personRaceStart);
    ControlCard controlCard = IOF300Utility.convertToXmlECard(table.getECardColumn().getValue(startListRow));
    personRaceStart.getControlCard().add(controlCard);

    // Start
    personRaceStart.setBibNumber(table.getBibNumberColumn().getValue(startListRow));
    personRaceStart.setStartTime(IOF300Utility.convertToXmlDate(table.getStartTimeColumn().getValue(startListRow)));

    // Club
    String name = table.getClubColumn().getValue(startListRow);
    String shortcut = table.getClubShortcutColumn().getValue(startListRow);
    String extKey = table.getClubExtKeyColumn().getValue(startListRow);
    Organisation club = IOF300Utility.convertToXmlClub(name, shortcut, extKey);
    personStart.setOrganisation(club);

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
