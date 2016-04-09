package com.rtiming.client.result;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.ui.columns.AbstractClassNameColumn;
import com.rtiming.client.common.ui.columns.AbstractClassShortcutColumn;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.settings.addinfo.AbstractTableWithAdditionalColumns;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.city.AreaCodeType;
import com.rtiming.shared.settings.city.CountryCodeType;

public abstract class AbstractResultsTable extends AbstractTableWithAdditionalColumns {

  @Override
  protected Class<? extends IMenu> getConfiguredDefaultMenu() {
    return EditMenu.class;
  }

  @Override
  public Long[] getEntityUids() throws ProcessingException {
    return new Long[]{EntityCodeType.EntryCode.ID, EntityCodeType.RunnerCode.ID};
  }

  @Override
  protected void execDecorateRow(ITableRow row) throws ProcessingException {
    String countryCode = StringUtility.lowercase(getNationCountryCodeColumn().getValue(row));
    row.getCellForUpdate(getNationColumn().getColumnIndex()).setIconId(countryCode);
  }

  public RunnerColumn getRunnerColumn() {
    return getColumnSet().getColumnByClass(RunnerColumn.class);
  }

  public RunnerNrColumn getRunnerNrColumn() {
    return getColumnSet().getColumnByClass(RunnerNrColumn.class);
  }

  public ClassNameColumn getClassNameColumn() {
    return getColumnSet().getColumnByClass(ClassNameColumn.class);
  }

  public ClassShortcutColumn getClassShortcutColumn() {
    return getColumnSet().getColumnByClass(ClassShortcutColumn.class);
  }

  public LastNameColumn getLastNameColumn() {
    return getColumnSet().getColumnByClass(LastNameColumn.class);
  }

  public FirstNameColumn getFirstNameColumn() {
    return getColumnSet().getColumnByClass(FirstNameColumn.class);
  }

  public BibNumberColumn getBibNumberColumn() {
    return getColumnSet().getColumnByClass(BibNumberColumn.class);
  }

  public RaceStatusColumn getRaceStatusColumn() {
    return getColumnSet().getColumnByClass(RaceStatusColumn.class);
  }

  public RankColumn getRankColumn() {
    return getColumnSet().getColumnByClass(RankColumn.class);
  }

  public TimeColumn getTimeColumn() {
    return getColumnSet().getColumnByClass(TimeColumn.class);
  }

  public EntryNrColumn getEntryNrColumn() {
    return getColumnSet().getColumnByClass(EntryNrColumn.class);
  }

  public RentalCardColumn getRentalCardColumn() {
    return getColumnSet().getColumnByClass(RentalCardColumn.class);
  }

  public TimeBehindColumn getTimeBehindColumn() {
    return getColumnSet().getColumnByClass(TimeBehindColumn.class);
  }

  public PercentColumn getPercentColumn() {
    return getColumnSet().getColumnByClass(PercentColumn.class);
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

  @Override
  protected boolean getConfiguredSortEnabled() {
    return false;
  }

  public CountryColumn getCountryColumn() {
    return getColumnSet().getColumnByClass(CountryColumn.class);
  }

  public NationColumn getNationColumn() {
    return getColumnSet().getColumnByClass(NationColumn.class);
  }

  public SexColumn getSexColumn() {
    return getColumnSet().getColumnByClass(SexColumn.class);
  }

  public YearColumn getYearColumn() {
    return getColumnSet().getColumnByClass(YearColumn.class);
  }

  public ExtKeyColumn getExtKeyColumn() {
    return getColumnSet().getColumnByClass(ExtKeyColumn.class);
  }

  public ECardColumn getECardColumn() {
    return getColumnSet().getColumnByClass(ECardColumn.class);
  }

  public NationCountryCodeColumn getNationCountryCodeColumn() {
    return getColumnSet().getColumnByClass(NationCountryCodeColumn.class);
  }

  public BirthdateColumn getBirthdateColumn() {
    return getColumnSet().getColumnByClass(BirthdateColumn.class);
  }

  public CityColumn getCityColumn() {
    return getColumnSet().getColumnByClass(CityColumn.class);
  }

  public ClubColumn getClubColumn() {
    return getColumnSet().getColumnByClass(ClubColumn.class);
  }

  public ClubShortcutColumn getClubShortcutColumn() {
    return getColumnSet().getColumnByClass(ClubShortcutColumn.class);
  }

  public ClubExtKeyColumn getClubExtKeyColumn() {
    return getColumnSet().getColumnByClass(ClubExtKeyColumn.class);
  }

  public RaceNrColumn getRaceNrColumn() {
    return getColumnSet().getColumnByClass(RaceNrColumn.class);
  }

  public LegStartTimeColumn getLegStartTimeColumn() {
    return getColumnSet().getColumnByClass(LegStartTimeColumn.class);
  }

  public LegTimeColumn getLegTimeColumn() {
    return getColumnSet().getColumnByClass(LegTimeColumn.class);
  }

  public AreaColumn getAreaColumn() {
    return getColumnSet().getColumnByClass(AreaColumn.class);
  }

  public ZipColumn getZipColumn() {
    return getColumnSet().getColumnByClass(ZipColumn.class);
  }

  public RegionColumn getRegionColumn() {
    return getColumnSet().getColumnByClass(RegionColumn.class);
  }

  public CourseShortcutColumn getCourseShortcutColumn() {
    return getColumnSet().getColumnByClass(CourseShortcutColumn.class);
  }

  public StreetColumn getStreetColumn() {
    return getColumnSet().getColumnByClass(StreetColumn.class);
  }

  @Order(1.0)
  public class RaceNrColumn extends AbstractLongColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(3.0)
  public class EntryNrColumn extends AbstractLongColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(5.0)
  public class RunnerNrColumn extends AbstractLongColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(7.0)
  public class RankColumn extends AbstractLongColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Rank");
    }

    @Override
    protected int getConfiguredWidth() {
      return 50;
    }
  }

  // IMPORTANT This order gap is reserverd for Split Time Columns

  @Order(44.0)
  public class ClassShortcutColumn extends AbstractClassShortcutColumn {

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

  }

  @Order(45.0)
  public class ClassNameColumn extends AbstractClassNameColumn {

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

  }

  @Order(46.0)
  public class CourseShortcutColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Course");
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

  @Order(47.0)
  public class BibNumberColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("BibNumber");
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(48.0)
  public class ExtKeyColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Number") + " (" + TEXTS.get("Runner") + ")";
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(50.0)
  public class RunnerColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Runner");
    }

    @Override
    protected int getConfiguredWidth() {
      return 250;
    }
  }

  @Order(52.0)
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

  @Order(55.0)
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

  @Order(60.0)
  public class ECardColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("ECard");
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(62.0)
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

  @Order(66.0)
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

  @Order(68.0)
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

  @Order(70.0)
  public class ClubColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Club");
    }

    @Override
    protected int getConfiguredWidth() {
      return 200;
    }
  }

  @Order(80.0)
  public class NationColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Nation");
    }

    @Override
    protected int getConfiguredWidth() {
      return 80;
    }
  }

  @Order(90.0)
  public class NationCountryCodeColumn extends AbstractStringColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(100.0)
  public class SexColumn extends AbstractSmartColumn<Long> {

    @Override
    protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
      return SexCodeType.class;
    }

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Sex");
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(110.0)
  public class BirthdateColumn extends AbstractDateColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Birthdate");
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }
  }

  @Order(120.0)
  public class YearColumn extends AbstractLongColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Year");
    }
  }

  @Order(124.0)
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

  @Order(125.0)
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

  @Order(130.0)
  public class CityColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("City");
    }

    @Override
    protected int getConfiguredWidth() {
      return 200;
    }
  }

  @Order(133.0)
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
    protected int getConfiguredWidth() {
      return 150;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

  }

  @Order(135.0)
  public class RegionColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Region");
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

  }

  @Order(140.0)
  public class CountryColumn extends AbstractSmartColumn<Long> {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Country");
    }

    @Override
    protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
      return CountryCodeType.class;

    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

    @Override
    protected int getConfiguredWidth() {
      return 200;
    }
  }

  @Order(142.0)
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

  @Order(144.0)
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

  @Order(146.0)
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

  @Order(148.0)
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

  @Order(149.0)
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

  @Order(153.0)
  public class RaceStatusColumn extends AbstractSmartColumn<Long> {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("RaceStatus");
    }

    @Override
    protected int getConfiguredWidth() {
      return 80;
    }

    @Override
    protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
      return RaceStatusCodeType.class;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

  }

  @Order(154.0)
  public class LegStartTimeColumn extends AbstractLongColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }

  }

  @Order(155.0)
  public class LegTimeColumn extends AbstractLongColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }

  }

  @Order(156.0)
  public class TimeBehindColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("TimeBehind");
    }

    @Override
    protected int getConfiguredHorizontalAlignment() {
      return 1;
    }

    @Override
    protected int getConfiguredWidth() {
      return 120;
    }
  }

  @Order(160.0)
  public class PercentColumn extends AbstractBigDecimalColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Percent");
    }

    @Override
    protected int getConfiguredMaxFractionDigits() {
      return 1;
    }

    @Override
    protected int getConfiguredMinFractionDigits() {
      return 1;
    }

    @Override
    protected int getConfiguredWidth() {
      return 80;
    }
  }

  @Order(170.0)
  public class TimeColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Time");
    }

    @Override
    protected int getConfiguredHorizontalAlignment() {
      return 1;
    }

    @Override
    protected int getConfiguredWidth() {
      return 90;
    }
  }

  @Order(10.0)
  public class EditMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("EditMenu");
    }

    @Override
    protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
      setVisible(getEntryNrColumn().getSelectedValue() != null);
    }

    @Override
    protected void execAction() throws ProcessingException {
      EntryForm form = new EntryForm();
      form.setEntryNr(getEntryNrColumn().getSelectedValue());
      form.startModify();
      form.waitFor();
      if (form.isFormStored()) {
        ClientSession.get().getDesktop().getOutline().getActivePage().reloadPage();
      }
    }
  }

}
