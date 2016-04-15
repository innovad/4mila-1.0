package com.rtiming.client.dataexchange.iof;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;

import com.rtiming.client.race.RaceControlsTablePage;
import com.rtiming.client.result.ResultsTablePage;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dataexchange.AbstractXMLDataBean;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.iof203.xml.ClassResult;
import com.rtiming.shared.dataexchange.iof203.xml.ClassShortName;
import com.rtiming.shared.dataexchange.iof203.xml.Club;
import com.rtiming.shared.dataexchange.iof203.xml.CompetitorStatus;
import com.rtiming.shared.dataexchange.iof203.xml.Control;
import com.rtiming.shared.dataexchange.iof203.xml.ControlCode;
import com.rtiming.shared.dataexchange.iof203.xml.Country;
import com.rtiming.shared.dataexchange.iof203.xml.CountryId;
import com.rtiming.shared.dataexchange.iof203.xml.Name;
import com.rtiming.shared.dataexchange.iof203.xml.Person;
import com.rtiming.shared.dataexchange.iof203.xml.PersonResult;
import com.rtiming.shared.dataexchange.iof203.xml.Result;
import com.rtiming.shared.dataexchange.iof203.xml.ResultList;
import com.rtiming.shared.dataexchange.iof203.xml.SplitTime;
import com.rtiming.shared.dataexchange.iof203.xml.StartTime;
import com.rtiming.shared.dataexchange.iof203.xml.Time;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.settings.city.CountryCodeType;

public class IOF203ResultListDataBean extends AbstractXMLDataBean {

  private static final long serialVersionUID = 1L;
  private final Long eventNr;
  private final ResultsTablePage results;
  private final RaceControlsTablePage splits;

  public IOF203ResultListDataBean(Long primaryKeyNr, Long eventNr, ResultsTablePage results, RaceControlsTablePage splits) {
    super(primaryKeyNr);
    this.eventNr = eventNr;
    this.results = results;
    this.splits = splits;
  }

  @Override
  public Object createXMLObject(Object main) throws ProcessingException {

    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(eventNr); // TODO cache

    ClassResult classResult = new ClassResult();

    String classShortName = FMilaUtility.getCodeExtKey(ClassCodeType.class, getPrimaryKeyNr());
    ClassShortName csn = new ClassShortName();
    csn.setvalue(classShortName);

    classResult.getClassIdOrClassShortNameOrEventClass().add(csn);

    ResultsTablePage.Table table = results.getTable();
    for (int k = 0; k < table.getRowCount(); k++) {
      PersonResult personResult = new PersonResult();

      // Name
      Person person = IOF203Utility.convertToXmlPerson(table.getLastNameColumn().getValue(k), table.getFirstNameColumn().getValue(k));
      personResult.getPersonIdOrPerson().add(person);

      // Time
      Result result = new Result();
      if (table.getLegTimeColumn().getValue(k) != null) {
        Time time = new Time();
        time.setTimeFormat("HH:MM:SS");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        time.setvalue(sdf.format(FMilaUtility.addMilliSeconds(DateUtility.truncDate(new Date()), table.getLegTimeColumn().getValue(k))));
        result.setTime(time);
      }
      personResult.setResult(result);

      // Club
      Club club = IOF203Utility.convertToXmlClub(table.getClubColumn().getValue(k), table.getClubShortcutColumn().getValue(k), table.getClubExtKeyColumn().getValue(k));
      personResult.getClubIdOrClubOrCountryIdOrCountry().add(club);

      // Nation
      Country country = new Country();
      Name countryName = new Name();
      ICode code = BEANS.get(CountryCodeType.class).getCode(table.getCountryColumn().getValue(k));
      if (code != null) {
        countryName.setvalue(code.getText());
      }
      String value = table.getNationColumn().getValue(k);
      if (value != null) {
        CountryId countryId = new CountryId();
        countryId.setValue(value);
        country.setCountryId(countryId);
      }
      country.getName().add(countryName);
      personResult.getClubIdOrClubOrCountryIdOrCountry().add(country);

      // Position
      result.setResultPosition(StringUtility.emptyIfNull(table.getRankColumn().getValue(k)));

      // Status
      CompetitorStatus status = new CompetitorStatus();
      status.setValue(IOF203Utility.convertToCompetitorStatus(table.getRaceStatusColumn().getValue(k)));
      result.setCompetitorStatus(status);

      // Start Time
      StartTime startTime = new StartTime();
      startTime.setClock(IOF203Utility.convertToXmlClock(FMilaUtility.addMilliSeconds(evtZero, table.getLegStartTimeColumn().getValue(k))));
      startTime.setDate(IOF203Utility.convertToXmlDate(FMilaUtility.addMilliSeconds(evtZero, table.getLegStartTimeColumn().getValue(k))));
      result.setStartTime(startTime);

      for (int s = 0; s < splits.getTable().getRowCount(); s++) {
        if (CompareUtility.equals(splits.getTable().getRaceNrColumn().getValue(s), table.getRaceNrColumn().getValue(k))) {
          if (CompareUtility.equals(splits.getTable().getControlTypeColumn().getValue(s), ControlTypeCodeType.ControlCode.ID)) {
            SplitTime splitTime = new SplitTime();
            splitTime.setSequence(String.valueOf(splits.getTable().getSortCodeColumn().getValue(s)));
            Control control = new Control();
            ControlCode controlCode = new ControlCode();
            controlCode.setvalue(splits.getTable().getControlColumn().getValue(s));
            control.setControlCode(controlCode);
            splitTime.getControlCodeOrControl().add(control);
            splitTime.setTime(IOF203Utility.convertToXmlTime(splits.getTable().getRelativeTimeColumn().getValue(s)));
            result.getSplitTime().add(splitTime);
          }
        }
      }

      classResult.getPersonResultOrTeamResult().add(personResult);
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
