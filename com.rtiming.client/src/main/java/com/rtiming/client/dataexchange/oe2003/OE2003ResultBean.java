package com.rtiming.client.dataexchange.oe2003;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;

import com.rtiming.client.race.RaceControlsTablePage;
import com.rtiming.client.result.ResultsTablePage;
import com.rtiming.client.result.ResultsTablePage.Table;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dataexchange.CSVElement;
import com.rtiming.shared.dataexchange.DataExchangeUtility;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.settings.CodeFormData;

public class OE2003ResultBean extends AbstractOE2003ListBean {

  // Stnr
  // Chip
  // Datenbank Id
  // Nachname
  // Vorname
  // Jg
  // G
  // Block
  // AK
  // Start
  // Ziel
  // Zeit
  // Wertung
  // Club-Nr.
  // Abk
  // Ort
  // Nat
  // Katnr
  // Kurz
  // Lang
  // Num1
  // Num2
  // Num3
  // Text1
  // Text2
  // Text3
  // Adr. Name
  // Straﬂe
  // Zeile2
  // PLZ
  // Ort
  // Tel
  // Fax
  // EMail
  // Id/Verein
  // Gemietet
  // Startgeld
  // Bezahlt
  // Bahnnummer
  // Bahn
  // km
  // Hm
  // Bahn Posten
  // Pl
  // Startstempel
  // Zielstempel
  // Posten1
  // Stempel1
  // Posten2
  // Stempel2
  // Posten3
  // Stempel3
  // Posten4
  // Stempel4
  // Posten5
  // Stempel5
  // Posten6
  // Stempel6
  // Posten7
  // Stempel7
  // Posten8
  // Stempel8
  // Posten9
  // Stempel9
  // Posten10
  // Stempel10
  // (und weitere)...

  private static final long serialVersionUID = 1L;

  @CSVElement(value = 44, title = "OE2003_Placement")
  private String placement;

  @CSVElement(value = 45, title = "OE2003_StartPunch")
  private String startTime;

  @CSVElement(value = 46, title = "OE2003_FinishPunch")
  private String finishTime;

  @CSVElement(value = 47, title = {"OE2003_Control", "OE2003_Punch"})
  private String[] controlAndsplitTimesList;

  private final ITableRow resultRow;
  private final List<ITableRow> splits;
  private final Long eventNr;
  private final EventClassFormData clazz;
  private final CodeFormData clazzCode;
  private final CourseFormData course;
  private final Long numControls;
  private final Long languageUid;

  public OE2003ResultBean(Long primaryKeyNr, ITableRow resultRow, List<ITableRow> splits, OE2003ClassInfo info, Long eventNr) {
    super(primaryKeyNr);
    this.resultRow = resultRow;
    this.eventNr = eventNr;
    this.splits = splits;
    this.clazz = info.getClazz();
    this.course = info.getCourse();
    this.numControls = info.getControlCount();
    this.clazzCode = info.getClazzCode();
    this.languageUid = info.getLanguageUid();
  }

  @Override
  public List<Object> getData() {
    List<Object> list = super.getData();
    list.add(placement);
    list.add(startTime);
    list.add(finishTime);
    list.add(controlAndsplitTimesList);
    return list;
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {
    ResultsTablePage.Table table = (Table) resultRow.getTable();
    Long rank = table.getRankColumn().getValue(resultRow);

    String clazzStr = "";
    for (int t = 0; t < clazzCode.getMainBox().getLanguage().getRowCount(); t++) {
      if (CompareUtility.equals(clazzCode.getMainBox().getLanguage().getLanguage(t), languageUid)) {
        clazzStr = clazzCode.getMainBox().getLanguage().getTranslation(t);
      }
    }

    // Standard Fields
    setStartNumber(table.getBibNumberColumn().getValue(resultRow));
    setECardNumber(table.getECardColumn().getValue(resultRow));
    setDatenbankId(table.getExtKeyColumn().getValue(resultRow));
    setNName(table.getLastNameColumn().getValue(resultRow));
    setVName(table.getFirstNameColumn().getValue(resultRow));
    setJg(OE2003Utility.exportYear(table.getYearColumn().getValue(resultRow)));
    setGeschlecht(OE2003Utility.exportSex(table.getSexColumn().getValue(resultRow)));
    setBlock(""); // not implemented
    setAK("0"); // not implemented
    setStart(OE2003Utility.exportRelativeTime(table.getLegStartTimeColumn().getValue(resultRow)));
    if (table.getLegStartTimeColumn().getValue(resultRow) != null && table.getLegTimeColumn().getValue(resultRow) != null) {
      setZiel(OE2003Utility.exportRelativeTime(table.getLegStartTimeColumn().getValue(resultRow) + table.getLegTimeColumn().getValue(resultRow)));
    }
    setZeit(OE2003Utility.exportRelativeTime(table.getLegTimeColumn().getValue(resultRow)));
    setWertung(OE2003Utility.exportRaceStatus(table.getRaceStatusColumn().getValue(resultRow)));
    setClubNr(table.getClubExtKeyColumn().getValue(resultRow));
    setAbk(table.getClubShortcutColumn().getValue(resultRow));
    setOrt(table.getClubColumn().getValue(resultRow));
    setNation(table.getNationColumn().getValue(resultRow));
    setKatnr(DataExchangeUtility.exportLong(clazz.getClazz().getValue()));
    setKurz(FMilaUtility.getCodeExtKey(ClassCodeType.class, clazz.getClazz().getValue()));
    setLang(clazzStr);
    setNum1(""); // not implemented
    setNum2(""); // not implemented
    setNum3(""); // not implemented
    setText1(""); // not implemented
    setText2(""); // not implemented
    setText3(""); // not implemented
    setAdrName(OE2003Utility.exportRunnerName(table.getFirstNameColumn().getValue(resultRow), table.getLastNameColumn().getValue(resultRow)));
    setStreet(""); // not implemented
    setZeile2(""); // not implemented
    setPLZ(table.getZipColumn().getValue(resultRow));
    setWohnort(table.getCityColumn().getValue(resultRow));
    setTel(table.getPhoneColumn().getValue(resultRow));
    setFax(table.getFaxColumn().getValue(resultRow));
    setEMail(table.getEMailColumn().getValue(resultRow));
    setClubCode(table.getClubExtKeyColumn().getValue(resultRow));
    setGemietet(OE2003Utility.exportBoolean(table.getRentalCardColumn().getValue(resultRow)));
    setStartgeld(""); // not implemented
    setBezahlt(""); // not implemented

    setCourseNumber(DataExchangeUtility.exportLong(course.getCourseNr()));
    setCourseName(course.getShortcut().getValue());
    setCourseLength(DataExchangeUtility.exportLong(course.getLength().getValue()));
    setCourseClimb(DataExchangeUtility.exportLong(course.getClimb().getValue()));
    setCourseNoOfControls(DataExchangeUtility.exportLong(numControls));

    // Additional Fields for Result List
    placement = DataExchangeUtility.exportLong(rank);
    startTime = OE2003Utility.exportRelativeTime(null);
    finishTime = OE2003Utility.exportRelativeTime(null);

    // Posten1
    // Stempel1
    // (und weitere)...
    ArrayList<String> result = new ArrayList<String>();
    for (ITableRow split : splits) {
      RaceControlsTablePage.Table splitTable = (RaceControlsTablePage.Table) split.getTable();
      if (CompareUtility.equals(splitTable.getControlTypeColumn().getValue(split), ControlTypeCodeType.ControlCode.ID)) {
        result.add(splitTable.getControlColumn().getValue(split));
        result.add(splitTable.getRelativeTimeColumn().getValue(split));
      }
    }
    controlAndsplitTimesList = result.toArray(new String[result.size()]);

  }

  @Override
  public void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {
  }

}
