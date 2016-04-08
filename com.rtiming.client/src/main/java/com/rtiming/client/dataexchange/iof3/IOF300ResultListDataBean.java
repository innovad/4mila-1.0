package com.rtiming.client.dataexchange.iof3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.client.result.ResultsTablePage;
import com.rtiming.client.result.split.SplitCalculator;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dataexchange.AbstractXMLDataBean;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.iof3.IOF300Utility;
import com.rtiming.shared.dataexchange.iof3.xml.Address;
import com.rtiming.shared.dataexchange.iof3.xml.ClassResult;
import com.rtiming.shared.dataexchange.iof3.xml.ControlCard;
import com.rtiming.shared.dataexchange.iof3.xml.Country;
import com.rtiming.shared.dataexchange.iof3.xml.Organisation;
import com.rtiming.shared.dataexchange.iof3.xml.Person;
import com.rtiming.shared.dataexchange.iof3.xml.PersonRaceResult;
import com.rtiming.shared.dataexchange.iof3.xml.PersonResult;
import com.rtiming.shared.dataexchange.iof3.xml.ResultList;
import com.rtiming.shared.dataexchange.iof3.xml.SplitTime;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.RaceControlRowData;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.TimePrecisionCodeType;

public class IOF300ResultListDataBean extends AbstractXMLDataBean {

  private static final long serialVersionUID = 1L;
  private final Long eventNr;
  private final ResultsTablePage results;
  private final List<RaceControlRowData> splits;

  public IOF300ResultListDataBean(Long primaryKeyNr, Long eventNr, ResultsTablePage results, List<RaceControlRowData> splits) {
    super(primaryKeyNr);
    this.eventNr = eventNr;
    this.results = results;
    this.splits = splits;
  }

  @Override
  public Object createXMLObject(Object main) throws ProcessingException {

    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(eventNr); // TODO cache

    ClassResult classResult = new ClassResult();

    // Class Info
    String classShortName = FMilaUtility.getCodeExtKey(ClassCodeType.class, getPrimaryKeyNr());
    classResult.setClazz(IOF300Utility.convertToXmlClass(classShortName));

    ResultsTablePage.Table table = results.getTable();
    Long winnerRaceNr = null;
    if (table.getRowCount() > 0) {
      winnerRaceNr = table.getRaceNrColumn().getValue(0);
    }
    Map<Long, List<com.rtiming.client.result.split.SplitTime>> splitLookup = SplitCalculator.calculate(winnerRaceNr, TimePrecisionCodeType.Precision1sCode.ID, splits);

    for (int k = 0; k < table.getRowCount(); k++) {
      PersonResult personResult = new PersonResult();

      // Name
      Person person = IOF300Utility.convertToXmlPerson(
          table.getExtKeyColumn().getValue(k),
          table.getLastNameColumn().getValue(k),
          table.getFirstNameColumn().getValue(k),
          table.getSexColumn().getValue(k),
          table.getYearColumn().getValue(k),
          table.getBirthdateColumn().getValue(k));
      personResult.setPerson(person);

      // Address
      Address address = IOF300Utility.convertToXmlAddress(table.getCityColumn().getValue(k), table.getCountryColumn().getValue(k));
      person.getAddress().add(address);

      // Time
      PersonRaceResult result = new PersonRaceResult();
      if (table.getLegTimeColumn().getValue(k) != null) {
        Double time = IOF300Utility.toDoubleTime(table.getLegTimeColumn().getValue(k));
        result.setTime(time);
      }
      personResult.getResult().add(result);

      // Club
      Organisation club = IOF300Utility.convertToXmlClub(table.getClubColumn().getValue(k),
          table.getClubShortcutColumn().getValue(k),
          table.getClubExtKeyColumn().getValue(k));
      personResult.setOrganisation(club);

      // Nation
      Country country = new Country();
      country.setCode(table.getNationColumn().getValue(k));
      personResult.getPerson().setNationality(country);

      // Position
      result.setPosition(NumberUtility.toBigInteger(table.getRankColumn().getValue(k)));

      // Status
      result.setStatus(IOF300Utility.convertToCompetitorStatus(table.getRaceStatusColumn().getValue(k)));

      // Bib No
      result.setBibNumber(table.getBibNumberColumn().getValue(k));

      // Start Time
      Date startTimeDate = FMilaUtility.addMilliSeconds(evtZero, table.getLegStartTimeColumn().getValue(k));
      result.setStartTime(IOF300Utility.convertToXmlDate(startTimeDate));

      // Finish Time
      if (table.getLegStartTimeColumn().getValue(k) != null && table.getLegTimeColumn().getValue(k) != null) {
        Date finishTimeDate = FMilaUtility.addMilliSeconds(evtZero, table.getLegStartTimeColumn().getValue(k) + table.getLegTimeColumn().getValue(k));
        result.setFinishTime(IOF300Utility.convertToXmlDate(finishTimeDate));
      }

      // E-Card
      ControlCard ecard = IOF300Utility.convertToXmlECard(table.getECardColumn().getValue(k));
      if (ecard != null) {
        result.getControlCard().add(ecard);
      }

      // splits - align control order to winner
      List<com.rtiming.client.result.split.SplitTime> splitList = splitLookup.get(results.getTable().getRaceNrColumn().getValue(k));
      if (splitList != null) {
        for (com.rtiming.client.result.split.SplitTime split : splitList) {
          if (CompareUtility.equals(split.getControl().getTypeUid(), ControlTypeCodeType.ControlCode.ID)) {
            SplitTime splitTime = new SplitTime();
            splitTime.setControlCode(split.getControl().getControlNo());
            Double time = IOF300Utility.toDoubleTime(split.getOverallTime().getOverallTimeRaw());
            splitTime.setTime(time);
            splitTime.setStatus(IOF300Utility.convertToXmlControlStatus(split.getControl().getStatusUid()));
            result.getSplitTime().add(splitTime);
          }
        }
      }

      classResult.getPersonResult().add(personResult);
    }

    ((ResultList) main).getClassResult().add(classResult);

    return main;
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {

  }

  @Override
  public void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {
  }

  @Override
  public ArrayList<String> getPreviewRowData() throws ProcessingException {
    ArrayList<String> list = new ArrayList<String>();
    list.add(FMilaUtility.getCodeText(ClassCodeType.class, results.getClassUid()));
    list.add(String.valueOf(results.getTable().getRowCount()));
    return list;
  }

}
