package com.rtiming.client.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateTimeColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ColorUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.common.ui.columns.AbstractClassNameColumn;
import com.rtiming.client.common.ui.columns.AbstractClassShortcutColumn;
import com.rtiming.client.common.ui.columns.AbstractTimeWithSecondsColumn;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField;
import com.rtiming.client.entry.startblock.StartblockSelectionForm;
import com.rtiming.client.race.RaceControlsTablePage;
import com.rtiming.client.settings.addinfo.AbstractTableWithAdditionalColumns;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.common.database.sql.EntryBean;
import com.rtiming.shared.ecard.download.PunchingSystemCodeType;
import com.rtiming.shared.entry.EntriesSearchFormData;
import com.rtiming.shared.entry.EntryList;
import com.rtiming.shared.entry.EntryRowData;
import com.rtiming.shared.entry.EntryTableDataOptions;
import com.rtiming.shared.entry.IEntryProcessService;
import com.rtiming.shared.entry.IRegistrationsOutlineService;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.city.AreaCodeType;
import com.rtiming.shared.settings.city.CountryCodeType;

public abstract class AbstractEntriesTablePage extends AbstractPageWithTable<AbstractEntriesTablePage.Table> implements IHelpEnabledPage {

  private final EntryList presentationType;
  private Long eventNr;
  private final Long clientNr;
  private final Long registrationNr;
  private final Long classUid;
  private final Long courseNr;
  private final Long clubNr;

  protected AbstractEntriesTablePage(EntryList presentationType, Long eventNr, Long clientNr, Long registrationNr, Long classUid, Long courseNr, Long clubNr) {
    super();
    this.presentationType = presentationType;
    this.eventNr = eventNr;
    this.clientNr = clientNr;
    this.registrationNr = registrationNr;
    this.classUid = classUid;
    this.courseNr = courseNr;
    this.clubNr = clubNr;
  }

  /**
   * Create a Entry List with Presentation Type ALL ({@link EntryList#ALL})
   * 
   * @param eventNr
   * @param clientNr
   * @param registrationNr
   * @param classUid
   * @param courseNr
   * @param clubNr
   */
  public AbstractEntriesTablePage(Long eventNr, Long clientNr, Long registrationNr, Long classUid, Long courseNr, Long clubNr) {
    this(EntryList.ALL, eventNr, clientNr, registrationNr, classUid, courseNr, clubNr);
  }

  public EntryList getPresentationType() {
    return presentationType;
  }

  @FormData
  public Long getEventNr() {
    return eventNr;
  }

  @FormData
  public Long getRegistrationNr() {
    return registrationNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return EntriesSearchForm.class;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("StartList");
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) throws ProcessingException {
    return new RaceControlsTablePage(getTable().getRaceNrColumn().getValue(row));
  }

  @Override
  protected void execInitPage() throws ProcessingException {
    switch (getPresentationType()) {
      case ALL:
        if (classUid == null && clubNr == null && courseNr == null) {
          if (getEventNr() != null) {
            // entry list for event
            getTable().getEventColumn().setDisplayable(false);
            getSearchForm().getEventField().setVisible(false);
            getCellForUpdate().setText(Texts.get("All"));
          }
          else {
            // overall entry list
            getCellForUpdate().setText(Texts.get("All"));
          }
        }
        break;
      case MISSING:
        // missing runner list for event
        getCellForUpdate().setText(Texts.get("Missing"));
        getTable().getEntryTimeColumn().setInitialVisible(true);
        getTable().getStartblockUidColumn().setInitialVisible(false);
        getTable().getStartListColumn().setInitialVisible(false);
        break;
      case MANUAL_RACE_STATUS:
        // races with manual status for event
        getCellForUpdate().setText(Texts.get("ManualControlStatus"));
        getTable().getEntryTimeColumn().setInitialVisible(true);
        getTable().getStartblockUidColumn().setInitialVisible(false);
        getTable().getStartListColumn().setInitialVisible(false);
        break;
      case FINISH_TIMES_STORED:
        // races with manual status for event
        getCellForUpdate().setText(Texts.get("FinishTimesStored"));
        getTable().getEntryTimeColumn().setInitialVisible(true);
        getTable().getStartblockUidColumn().setInitialVisible(false);
        getTable().getStartListColumn().setInitialVisible(false);
        break;
      default:
        break;
    }
    if (getEventNr() == null && registrationNr == null) {
      eventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
      getSearchForm().getEventField().setValue(getEventNr());
    }
    else {
      getSearchForm().getEventField().setValue(getEventNr());
    }

    getTable().resetColumnVisibilities();
  }

  public EntriesSearchForm getSearchForm() {
    return (EntriesSearchForm) getSearchFormInternal();
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    EntryTableDataOptions options = new EntryTableDataOptions();
    options.setPresentationType(getPresentationType());
    options.setClientNr(clientNr);
    options.setRegistrationNr(getRegistrationNr());
    options.setClassUid(classUid);
    options.setCourseNr(courseNr);
    options.setClubNr(clubNr);

    List<EntryRowData> list = BEANS.get(IRegistrationsOutlineService.class).getEntryTableData(options, (EntriesSearchFormData) getSearchFilter().getFormData());

    Object[][] table = new Object[list.size()][getTable().getColumnCount()];
    int k = 0;
    for (EntryRowData row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getEntryNrColumn().getColumnIndex()] = row.getEntryNr();
      table[k][getTable().getRaceNrColumn().getColumnIndex()] = row.getRaceNr();
      table[k][getTable().getRaceNrOrderColumn().getColumnIndex()] = row.getRaceNrOrder();
      table[k][getTable().getPunchingSystemUidColumn().getColumnIndex()] = row.getPunchingSystemUid();
      table[k][getTable().getBibNumberColumn().getColumnIndex()] = row.getBibNumber();
      table[k][getTable().getRegistrationNoColumn().getColumnIndex()] = row.getRegistrationNo();
      table[k][getTable().getEntryTimeColumn().getColumnIndex()] = row.getEntryTime();
      table[k][getTable().getExtKeyColumn().getColumnIndex()] = row.getExtKey();
      table[k][getTable().getECardColumn().getColumnIndex()] = row.getECard();
      table[k][getTable().getRentalCardColumn().getColumnIndex()] = row.isStartList();
      table[k][getTable().getRunnerColumn().getColumnIndex()] = row.getRunner();
      table[k][getTable().getLastNameColumn().getColumnIndex()] = row.getLastName();
      table[k][getTable().getFirstNameColumn().getColumnIndex()] = row.getFirstName();
      table[k][getTable().getNationCountryCodeColumn().getColumnIndex()] = row.getNationCountryCode();
      table[k][getTable().getSexColumn().getColumnIndex()] = row.getSex();
      table[k][getTable().getBirthdateColumn().getColumnIndex()] = row.getBirthdate();
      table[k][getTable().getYearColumn().getColumnIndex()] = row.getYear();
      table[k][getTable().getClassShortcutColumn().getColumnIndex()] = row.getLegClassShortcut();
      table[k][getTable().getClassNameColumn().getColumnIndex()] = row.getLegClassName();
      table[k][getTable().getCourseShortcutColumn().getColumnIndex()] = row.getCourseShortcut();
      table[k][getTable().getStreetColumn().getColumnIndex()] = row.getStreet();
      table[k][getTable().getZipColumn().getColumnIndex()] = row.getZip();
      table[k][getTable().getCityColumn().getColumnIndex()] = row.getCity();
      table[k][getTable().getAreaColumn().getColumnIndex()] = row.getArea();
      table[k][getTable().getRegionColumn().getColumnIndex()] = row.getRegion();
      table[k][getTable().getCountryColumn().getColumnIndex()] = row.getCountry();
      table[k][getTable().getPhoneColumn().getColumnIndex()] = row.getPhone();
      table[k][getTable().getFaxColumn().getColumnIndex()] = row.getFax();
      table[k][getTable().getMobilePhoneColumn().getColumnIndex()] = row.getMobilePhone();
      table[k][getTable().getEMailColumn().getColumnIndex()] = row.getEMail();
      table[k][getTable().getURLColumn().getColumnIndex()] = row.getURL();
      table[k][getTable().getNationColumn().getColumnIndex()] = row.getNation();
      table[k][getTable().getClubShortcutColumn().getColumnIndex()] = row.getClubShortcut();
      table[k][getTable().getClubExtKeyColumn().getColumnIndex()] = row.getClubExtKey();
      table[k][getTable().getClubColumn().getColumnIndex()] = row.getClub();
      table[k][getTable().getEventColumn().getColumnIndex()] = row.getEvent();
      table[k][getTable().getStartListColumn().getColumnIndex()] = row.isStartList();
      table[k][getTable().getStartblockColumn().getColumnIndex()] = row.getStartblock();
      table[k][getTable().getStartlistSettingOptionColumn().getColumnIndex()] = row.getStartlistSettingOption();
      table[k][getTable().getRelativeStartTimeColumn().getColumnIndex()] = row.getRelativeStartTime();
      table[k][getTable().getStartTimeColumn().getColumnIndex()] = row.getStartTime();

      // additional values
      List<AdditionalInformationValueBean> entryAis = SharedCache.getAddInfoForEntity(EntityCodeType.EntryCode.ID, ClientSession.get().getSessionClientNr());
      List<AdditionalInformationValueBean> runnerAis = SharedCache.getAddInfoForEntity(EntityCodeType.RunnerCode.ID, ClientSession.get().getSessionClientNr());
      for (int a = 0; a < entryAis.size(); a++) {
        table[k][getTable().getColumnCount() - entryAis.size() - runnerAis.size() + a] = row.getEntryAdditionalValues()[a];
      }
      for (int a = 0; a < runnerAis.size(); a++) {
        table[k][getTable().getColumnCount() - runnerAis.size() + a] = row.getRunnerAdditionalValues()[a];
      }
      k++;
    }
    importTableData(table);
  }

  @Order(10.0)
  public class Table extends AbstractTableWithAdditionalColumns {

    @Override
    public Long[] getEntityUids() throws ProcessingException {
      return new Long[]{EntityCodeType.EntryCode.ID, EntityCodeType.RunnerCode.ID};
    }

    @Override
    public Long getEventNr() throws ProcessingException {
      return eventNr;
    }

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      if (EntryList.ALL.equals(getPresentationType())) {
        return EditEntryMenu.class;
      }
      else {
        return EditRaceMenu.class;
      }
    }

    @Override
    protected void execDecorateRow(ITableRow row) throws ProcessingException {
      if (getGroupSortcodeColumn().getValue(row) != 0) {
        row.setFont(FontSpec.parse("italic"));
        row.setForegroundColor(FMilaUtility.COLOR_LIGHT_GREY);
      }
      String countryCode = StringUtility.lowercase(getNationCountryCodeColumn().getValue(row));
      row.getCellForUpdate(getNationColumn().getColumnIndex()).setIconId(countryCode);
    }

    @Override
    protected void execDecorateCell(Cell view, ITableRow row, IColumn<?> col) throws ProcessingException {
      if (col instanceof ECardColumn) {
        if (CompareUtility.equals(getPunchingSystemUidColumn().getValue(row), PunchingSystemCodeType.PunchingSystemNoneCode.ID)) {
          // nop
        }
        else if (StringUtility.isNullOrEmpty(getECardColumn().getValue(row)) && getGroupSortcodeColumn().getValue(row) == 0) {
          // ecard required
          row.setForegroundColor(ColorUtility.RED);
          view.setIconId(Icons.ERROR);
          view.setTooltipText(TEXTS.get("ElectronicPunchingSystemNoECardWarning"));
          view.setBackgroundColor(ColorUtility.RED);
        }
      }

      if (col instanceof StartTimeColumn) {
        if (getStartListColumn().getValue(row) && getStartTimeColumn().getValue(row) == null) {
          // starttime required
          row.setForegroundColor(ColorUtility.RED);
          view.setTooltipText(TEXTS.get("ClassOrCourseWithStartlistSettingButNoStartTimeWarning"));
          view.setIconId(Icons.ERROR);
          view.setBackgroundColor(ColorUtility.RED);
        }
      }
    }

    public RunnerColumn getRunnerColumn() {
      return getColumnSet().getColumnByClass(RunnerColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.ENTRY;
    }

    public LastNameColumn getLastNameColumn() {
      return getColumnSet().getColumnByClass(LastNameColumn.class);
    }

    public FirstNameColumn getFirstNameColumn() {
      return getColumnSet().getColumnByClass(FirstNameColumn.class);
    }

    public RentalCardColumn getRentalCardColumn() {
      return getColumnSet().getColumnByClass(RentalCardColumn.class);
    }

    public ClubShortcutColumn getClubShortcutColumn() {
      return getColumnSet().getColumnByClass(ClubShortcutColumn.class);
    }

    public ClubExtKeyColumn getClubExtKeyColumn() {
      return getColumnSet().getColumnByClass(ClubExtKeyColumn.class);
    }

    public EventColumn getEventColumn() {
      return getColumnSet().getColumnByClass(EventColumn.class);
    }

    public StartTimeColumn getStartTimeColumn() {
      return getColumnSet().getColumnByClass(StartTimeColumn.class);
    }

    public ClassNameColumn getClassNameColumn() {
      return getColumnSet().getColumnByClass(ClassNameColumn.class);
    }

    public ClassShortcutColumn getClassShortcutColumn() {
      return getColumnSet().getColumnByClass(ClassShortcutColumn.class);
    }

    public CourseShortcutColumn getCourseShortcutColumn() {
      return getColumnSet().getColumnByClass(CourseShortcutColumn.class);
    }

    public RaceNrOrderColumn getRaceNrOrderColumn() {
      return getColumnSet().getColumnByClass(RaceNrOrderColumn.class);
    }

    public StartblockColumn getStartblockColumn() {
      return getColumnSet().getColumnByClass(StartblockColumn.class);
    }

    public ZipColumn getZipColumn() {
      return getColumnSet().getColumnByClass(ZipColumn.class);
    }

    public PhoneColumn getPhoneColumn() {
      return getColumnSet().getColumnByClass(PhoneColumn.class);
    }

    public FaxColumn getFaxColumn() {
      return getColumnSet().getColumnByClass(FaxColumn.class);
    }

    public MobilePhoneColumn getMobilePhoneColumn() {
      return getColumnSet().getColumnByClass(MobilePhoneColumn.class);
    }

    public EMailColumn getEMailColumn() {
      return getColumnSet().getColumnByClass(EMailColumn.class);
    }

    public URLColumn getURLColumn() {
      return getColumnSet().getColumnByClass(URLColumn.class);
    }

    public ECardColumn getECardColumn() {
      return getColumnSet().getColumnByClass(ECardColumn.class);
    }

    public YearColumn getYearColumn() {
      return getColumnSet().getColumnByClass(YearColumn.class);
    }

    public CountryColumn getCountryColumn() {
      return getColumnSet().getColumnByClass(CountryColumn.class);
    }

    public NationColumn getNationColumn() {
      return getColumnSet().getColumnByClass(NationColumn.class);
    }

    public RaceNrColumn getRaceNrColumn() {
      return getColumnSet().getColumnByClass(RaceNrColumn.class);
    }

    public RaceNrOrderColumn getGroupSortcodeColumn() {
      return getColumnSet().getColumnByClass(RaceNrOrderColumn.class);
    }

    public StartListColumn getStartListColumn() {
      return getColumnSet().getColumnByClass(StartListColumn.class);
    }

    public PunchingSystemUidColumn getPunchingSystemUidColumn() {
      return getColumnSet().getColumnByClass(PunchingSystemUidColumn.class);
    }

    public StartblockColumn getStartblockUidColumn() {
      return getColumnSet().getColumnByClass(StartblockColumn.class);
    }

    public ExtKeyColumn getExtKeyColumn() {
      return getColumnSet().getColumnByClass(ExtKeyColumn.class);
    }

    public SexColumn getSexColumn() {
      return getColumnSet().getColumnByClass(SexColumn.class);
    }

    public BirthdateColumn getBirthdateColumn() {
      return getColumnSet().getColumnByClass(BirthdateColumn.class);
    }

    public RegionColumn getRegionColumn() {
      return getColumnSet().getColumnByClass(RegionColumn.class);
    }

    public NationCountryCodeColumn getNationCountryCodeColumn() {
      return getColumnSet().getColumnByClass(NationCountryCodeColumn.class);
    }

    public EntryTimeColumn getEntryTimeColumn() {
      return getColumnSet().getColumnByClass(EntryTimeColumn.class);
    }

    public RelativeStartTimeColumn getRelativeStartTimeColumn() {
      return getColumnSet().getColumnByClass(RelativeStartTimeColumn.class);
    }

    public ClassShortcutColumn getClazzShortcutColumn() {
      return getColumnSet().getColumnByClass(ClassShortcutColumn.class);
    }

    public ClassNameColumn getClazzNameColumn() {
      return getColumnSet().getColumnByClass(ClassNameColumn.class);
    }

    public StreetColumn getStreetColumn() {
      return getColumnSet().getColumnByClass(StreetColumn.class);
    }

    public StartlistSettingOptionColumn getStartlistSettingOptionColumn() {
      return getColumnSet().getColumnByClass(StartlistSettingOptionColumn.class);
    }

    public RegistrationNoColumn getRegistrationNoColumn() {
      return getColumnSet().getColumnByClass(RegistrationNoColumn.class);
    }

    public AreaColumn getAreaColumn() {
      return getColumnSet().getColumnByClass(AreaColumn.class);
    }

    public BibNumberColumn getBibNumberColumn() {
      return getColumnSet().getColumnByClass(BibNumberColumn.class);
    }

    public CityColumn getCityColumn() {
      return getColumnSet().getColumnByClass(CityColumn.class);
    }

    public ClubColumn getClubColumn() {
      return getColumnSet().getColumnByClass(ClubColumn.class);
    }

    public EntryNrColumn getEntryNrColumn() {
      return getColumnSet().getColumnByClass(EntryNrColumn.class);
    }

    @Order(10.0)
    public class EntryNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 2;
      }
    }

    @Order(20.0)
    public class RaceNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(30.0)
    public class RaceNrOrderColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 3;
      }

    }

    @Order(40.0)
    public class PunchingSystemUidColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(50.0)
    public class BibNumberColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("BibNumber");
      }

      @Override
      protected int getConfiguredWidth() {
        return 90;
      }
    }

    @Order(60.0)
    public class RegistrationNoColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Registration");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(70.0)
    public class EntryTimeColumn extends AbstractDateTimeColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("EntryTime");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(80.0)
    public class ExtKeyColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Number");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(120.0)
    public class ECardColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("ECard");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(130.0)
    public class RentalCardColumn extends AbstractBooleanColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("RentalCard");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(90.0)
    public class RunnerColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Runner");
      }

      @Override
      protected boolean getConfiguredSummary() {
        return true;
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 4;
      }

    }

    @Order(100.0)
    public class LastNameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("LastName");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(110.0)
    public class FirstNameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("FirstName");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(180.0)
    public class NationCountryCodeColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(190.0)
    public class SexColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Sex");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return SexCodeType.class;

      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 55;
      }
    }

    @Order(200.0)
    public class BirthdateColumn extends AbstractDateColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Birthdate");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(210.0)
    public class YearColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Year");
      }

      @Override
      protected int getConfiguredWidth() {
        return 65;
      }
    }

    @Order(220.0)
    public class ClassShortcutColumn extends AbstractClassShortcutColumn {

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(230.0)
    public class ClassNameColumn extends AbstractClassNameColumn {

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(235.0)
    public class CourseShortcutColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Course");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(240.0)
    public class StreetColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Street");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(250.0)
    public class ZipColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Zip");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(260.0)
    public class CityColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("City");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(270.0)
    public class AreaColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Area");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return AreaCodeType.class;

      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(280.0)
    public class RegionColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Region");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(290.0)
    public class CountryColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return CountryCodeType.class;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Country");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(300.0)
    public class PhoneColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Phone");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(310.0)
    public class FaxColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Fax");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(320.0)
    public class MobilePhoneColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("MobilePhone");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(330.0)
    public class EMailColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("EMail");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(340.0)
    public class URLColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("www");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(170.0)
    public class NationColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Nation");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(140.0)
    public class ClubShortcutColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Shortcut") + " (" + TEXTS.get("Club") + ")";
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(150.0)
    public class ClubExtKeyColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Number") + " (" + TEXTS.get("Club") + ")";
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(160.0)
    public class ClubColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Club");
      }

      @Override
      protected int getConfiguredWidth() {
        return 160;
      }
    }

    @Order(350.0)
    public class EventColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Event");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(360.0)
    public class StartListColumn extends AbstractBooleanColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("StartList");
      }
    }

    @Order(370.0)
    public class StartblockColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Startblock");
      }

      @Override
      protected int getConfiguredWidth() {
        return 75;
      }
    }

    @Order(380.0)
    public class StartlistSettingOptionColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return StartlistSettingOptionCodeType.class;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("StartlistOption");
      }

      @Override
      protected int getConfiguredWidth() {
        return 160;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(390.0)
    public class RelativeStartTimeColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(400.0)
    public class StartTimeColumn extends AbstractTimeWithSecondsColumn {

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 70;
      }

    }

    @Order(10.0)
    public class NewEntryMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("NewButton");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(EntryList.ALL.equals(getPresentationType()));
      }

      @Override
      protected void execAction() throws ProcessingException {
        EntryForm form = new EntryForm();
        form.getRegistrationField().setValue(getRegistrationNr());
        form.startNew();
        if (getEventNr() != null) {
          form.getEventsField().getTable().getEventNrColumn().fill(getEventNr());
        }
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(20.0)
    public class EditEntryMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("EditMenu");
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(EntryList.ALL.equals(getPresentationType()));
      }

      @Override
      protected void execAction() throws ProcessingException {
        EntryForm form = new EntryForm();
        form.setEntryNr(getEntryNrColumn().getSelectedValue());
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(30.0)
    public class EditRaceMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("EditMenu");
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(EntryList.MISSING.equals(getPresentationType()) || EntryList.FINISH_TIMES_STORED.equals(getPresentationType()) || EntryList.MANUAL_RACE_STATUS.equals(getPresentationType()));
      }

      @Override
      protected void execAction() throws ProcessingException {
        EntryForm entry = new EntryForm();
        entry.setEntryNr(getTable().getEntryNrColumn().getSelectedValue());
        entry.startModify();
        for (ITableRow row : entry.getRacesField().getTable().getRows()) {
          Long currentRaceNr = entry.getRacesField().getTable().getRaceNrColumn().getValue(row);
          Long raceNr = getTable().getRaceNrColumn().getSelectedValue();
          if (CompareUtility.equals(currentRaceNr, raceNr)) {
            entry.getRacesField().getTable().selectRow(row);
            entry.getRacesField().getTable().runMenu(RacesField.Table.EditMenu.class);
          }
        }
        entry.waitFor();
        if (entry.isFormStored()) {
          reloadPage();
        }
      }

    }

    @Order(40.0)
    public class SetStartblockMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("SetStartblockMenu");
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(EntryList.ALL.equals(getPresentationType()));
      }

      @Override
      protected void execAction() throws ProcessingException {
        StartblockSelectionForm form = new StartblockSelectionForm();
        form.setEventNr(getEventNr());
        form.setEntryNrs(getEntryNrColumn().getSelectedValues().toArray(new Long[0]));
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(45.0)
    public class InflateRaceControlsMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(AbstractEntriesTablePage.this instanceof EntriesTablePage);
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("InflateRaceControls");
      }

      @Override
      protected void execAction() throws ProcessingException {
        BEANS.get(IRaceService.class).inflateRaceControls(getRaceNrColumn().getSelectedValues().toArray(new Long[0]));
        reloadPage();
      }
    }

    @Order(50.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

    }

    @Order(60.0)
    public class DeleteEntryMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        List<Long> entryNrList = getEntryNrColumn().getSelectedValues();
        List<String> texts = new ArrayList<>();
        for (ITableRow row : getTable().getRows()) {
          if (entryNrList.contains(getTable().getEntryNrColumn().getValue(row))) {
            texts.add(getTable().getRunnerColumn().getValue(row));
          }
        }
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Entries"), texts.toArray(new String[texts.size()]))) {
          for (Long entryNr : getEntryNrColumn().getSelectedValues()) {
            EntryBean entry = new EntryBean();
            entry.setEntryNr(entryNr);
            entry = BEANS.get(IEntryProcessService.class).load(entry);
            BEANS.get(IEntryProcessService.class).delete(entry);
          }
          reloadPage();
        }
      }
    }
  }
}
