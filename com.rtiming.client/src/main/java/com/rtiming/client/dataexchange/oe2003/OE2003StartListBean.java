package com.rtiming.client.dataexchange.oe2003;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;

import com.rtiming.client.entry.AbstractEntriesTablePage;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dataexchange.DataExchangeUtility;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;

public class OE2003StartListBean extends AbstractOE2003ListBean {

  private static final long serialVersionUID = 1L;
  private final Long eventNr;
  private final ITableRow entryListRow;
  private final OE2003ClassInfo info;

  public OE2003StartListBean(Long primaryKeyNr, ITableRow entryListRow, OE2003ClassInfo info, Long eventNr) {
    super(primaryKeyNr);
    this.eventNr = eventNr;
    this.entryListRow = entryListRow;
    this.info = info;
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {
    AbstractEntriesTablePage.Table table = (AbstractEntriesTablePage.Table) entryListRow.getTable();

    String clazzStr = "";
    for (int t = 0; t < info.getClazzCode().getMainBox().getLanguage().getRowCount(); t++) {
      if (CompareUtility.equals(info.getClazzCode().getMainBox().getLanguage().getLanguage(t), info.getLanguageUid())) {
        clazzStr = info.getClazzCode().getMainBox().getLanguage().getTranslation(t);
      }
    }

    // Standard Fields
    setStartNumber(table.getBibNumberColumn().getValue(entryListRow));
    setECardNumber(table.getECardColumn().getValue(entryListRow));
    setDatenbankId(table.getExtKeyColumn().getValue(entryListRow));
    setNName(table.getLastNameColumn().getValue(entryListRow));
    setVName(table.getFirstNameColumn().getValue(entryListRow));
    setJg(OE2003Utility.exportYear(table.getYearColumn().getValue(entryListRow)));
    setGeschlecht(OE2003Utility.exportSex(table.getSexColumn().getValue(entryListRow)));
    setBlock(""); // not implemented
    setAK("0"); // not implemented
    setStart(OE2003Utility.exportRelativeTime(table.getRelativeStartTimeColumn().getValue(entryListRow)));
    setZiel(""); // not implemented for start list
    setZeit(""); // not implemented for start list
    setWertung(OE2003Utility.exportRaceStatus(RaceStatusCodeType.DidNotStartCode.ID));
    setClubNr(table.getClubExtKeyColumn().getValue(entryListRow));
    setAbk(table.getClubShortcutColumn().getValue(entryListRow));
    setOrt(table.getClubColumn().getValue(entryListRow));
    setNation(table.getNationColumn().getValue(entryListRow));
    setKatnr(DataExchangeUtility.exportLong(info.getClazz().getClazz().getValue()));
    setKurz(FMilaUtility.getCodeExtKey(ClassCodeType.class, info.getClazz().getClazz().getValue()));
    setLang(clazzStr);
    setNum1(""); // not implemented
    setNum2(""); // not implemented
    setNum3(""); // not implemented
    setText1(""); // not implemented
    setText2(""); // not implemented
    setText3(""); // not implemented
    setAdrName(OE2003Utility.exportRunnerName(table.getFirstNameColumn().getValue(entryListRow), table.getLastNameColumn().getValue(entryListRow)));
    setStreet(""); // not implemented
    setZeile2(""); // not implemented
    setPLZ(table.getZipColumn().getValue(entryListRow));
    setWohnort(table.getCityColumn().getValue(entryListRow));
    setTel(table.getPhoneColumn().getValue(entryListRow));
    setFax(table.getFaxColumn().getValue(entryListRow));
    setEMail(table.getEMailColumn().getValue(entryListRow));
    setClubCode(table.getClubExtKeyColumn().getValue(entryListRow));
    setGemietet(OE2003Utility.exportBoolean(table.getRentalCardColumn().getValue(entryListRow)));
    setStartgeld(""); // not implemented
    setBezahlt(""); // not implemented

    setCourseNumber(DataExchangeUtility.exportLong(info.getCourse().getCourseNr()));
    setCourseName(info.getCourse().getShortcut().getValue());
    setCourseLength(DataExchangeUtility.exportLong(info.getCourse().getLength().getValue()));
    setCourseClimb(DataExchangeUtility.exportLong(info.getCourse().getClimb().getValue()));
    setCourseNoOfControls(DataExchangeUtility.exportLong(info.getControlCount()));

  }

  @Override
  protected void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {

  }

}
