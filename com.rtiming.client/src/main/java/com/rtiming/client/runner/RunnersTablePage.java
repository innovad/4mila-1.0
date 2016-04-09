package com.rtiming.client.runner;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.settings.addinfo.AbstractTableWithAdditionalColumns;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.runner.RunnerRowData;
import com.rtiming.shared.runner.RunnersSearchFormData;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.city.AreaCodeType;
import com.rtiming.shared.settings.city.CountryCodeType;

public class RunnersTablePage extends AbstractPageWithTable<RunnersTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Runners");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<RunnerRowData> list = BEANS.get(IEventsOutlineService.class).getRunnerTableData((RunnersSearchFormData) filter.getFormData());
    Object[][] table = new Object[list.size()][getTable().getColumnCount()];

    int k = 0;
    for (RunnerRowData row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getRunnerNrColumn().getColumnIndex()] = row.getRunnerNr();
      table[k][getTable().getExtKeyColumn().getColumnIndex()] = row.getExtKey();
      table[k][getTable().getNameColumn().getColumnIndex()] = row.getName();
      table[k][getTable().getLastNameColumn().getColumnIndex()] = row.getLastName();
      table[k][getTable().getFirstNameColumn().getColumnIndex()] = row.getFirstName();
      table[k][getTable().getECardColumn().getColumnIndex()] = row.geteCard();
      table[k][getTable().getDefaultClazzColumn().getColumnIndex()] = row.getDefaultClazzUid();
      table[k][getTable().getClubColumn().getColumnIndex()] = row.getClub();
      table[k][getTable().getNationColumn().getColumnIndex()] = row.getNation();
      table[k][getTable().getSexColumn().getColumnIndex()] = row.getSexUid();
      table[k][getTable().getBirthdateColumn().getColumnIndex()] = row.getEvtBirthdate();
      table[k][getTable().getYearColumn().getColumnIndex()] = row.getYear();
      table[k][getTable().getStreetColumn().getColumnIndex()] = row.getStreet();
      table[k][getTable().getZipColumn().getColumnIndex()] = row.getZip();
      table[k][getTable().getCityColumn().getColumnIndex()] = row.getCity();
      table[k][getTable().getAreaColumn().getColumnIndex()] = row.getAreaUid();
      table[k][getTable().getRegionColumn().getColumnIndex()] = row.getRegion();
      table[k][getTable().getCountryColumn().getColumnIndex()] = row.getCountryUid();
      table[k][getTable().getPhoneColumn().getColumnIndex()] = row.getPhone();
      table[k][getTable().getFaxColumn().getColumnIndex()] = row.getFax();
      table[k][getTable().getMobilePhoneColumn().getColumnIndex()] = row.getMobile();
      table[k][getTable().getEMailColumn().getColumnIndex()] = row.getEmail();
      table[k][getTable().getURLColumn().getColumnIndex()] = row.getWww();

      // additional values
      List<AdditionalInformationValueBean> ais = SharedCache.getAddInfoForEntity(EntityCodeType.RunnerCode.ID, ClientSession.get().getSessionClientNr());
      for (int a = 0; a < ais.size(); a++) {
        table[k][getTable().getColumnCount() - ais.size() + a] = row.getAdditionalValues()[a];
      }
      k++;
    }
    importTableData(table);
  }

  @Order(10.0)
  public class Table extends AbstractTableWithAdditionalColumns {

    @Override
    public Long[] getEntityUids() throws ProcessingException {
      return new Long[]{EntityCodeType.RunnerCode.ID};
    }

    @Override
    protected void execDecorateRow(ITableRow row) throws ProcessingException {
      String countryCode = StringUtility.lowercase(getNationCountryCodeColumn().getValue(row));
      row.getCellForUpdate(getNationColumn().getColumnIndex()).setIconId(countryCode);
    }

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.RUNNER;
    }

    public YearColumn getYearColumn() {
      return getColumnSet().getColumnByClass(YearColumn.class);
    }

    public DefaultClazzColumn getDefaultClazzColumn() {
      return getColumnSet().getColumnByClass(DefaultClazzColumn.class);
    }

    public CityColumn getCityColumn() {
      return getColumnSet().getColumnByClass(CityColumn.class);
    }

    public SexColumn getSexColumn() {
      return getColumnSet().getColumnByClass(SexColumn.class);
    }

    public CountryColumn getCountryColumn() {
      return getColumnSet().getColumnByClass(CountryColumn.class);
    }

    public NationColumn getNationColumn() {
      return getColumnSet().getColumnByClass(NationColumn.class);
    }

    public ZipColumn getZipColumn() {
      return getColumnSet().getColumnByClass(ZipColumn.class);
    }

    public RegionColumn getRegionColumn() {
      return getColumnSet().getColumnByClass(RegionColumn.class);
    }

    public StreetColumn getStreetColumn() {
      return getColumnSet().getColumnByClass(StreetColumn.class);
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

    public NationCountryCodeColumn getNationCountryCodeColumn() {
      return getColumnSet().getColumnByClass(NationCountryCodeColumn.class);
    }

    public LastNameColumn getLastNameColumn() {
      return getColumnSet().getColumnByClass(LastNameColumn.class);
    }

    public FirstNameColumn getFirstNameColumn() {
      return getColumnSet().getColumnByClass(FirstNameColumn.class);
    }

    public AreaColumn getAreaColumn() {
      return getColumnSet().getColumnByClass(AreaColumn.class);
    }

    public BirthdateColumn getBirthdateColumn() {
      return getColumnSet().getColumnByClass(BirthdateColumn.class);
    }

    public ClubColumn getClubColumn() {
      return getColumnSet().getColumnByClass(ClubColumn.class);
    }

    public ECardColumn getECardColumn() {
      return getColumnSet().getColumnByClass(ECardColumn.class);
    }

    public ExtKeyColumn getExtKeyColumn() {
      return getColumnSet().getColumnByClass(ExtKeyColumn.class);
    }

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
    }

    public RunnerNrColumn getRunnerNrColumn() {
      return getColumnSet().getColumnByClass(RunnerNrColumn.class);
    }

    @Order(10.0)
    public class RunnerNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected boolean getConfiguredPrimaryKey() {
        return true;
      }
    }

    @Order(20.0)
    public class ExtKeyColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Number");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(30.0)
    public class NameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return ScoutTexts.get("Name");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(40.0)
    public class LastNameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("LastName");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 160;
      }

    }

    @Order(50.0)
    public class FirstNameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("FirstName");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 160;
      }

    }

    @Order(60.0)
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

    @Order(70.0)
    public class DefaultClazzColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("DefaultClazz");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ClassCodeType.class;
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(80.0)
    public class ClubColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Club");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(90.0)
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

    @Order(100.0)
    public class NationCountryCodeColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(110.0)
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
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(120.0)
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
        return 120;
      }
    }

    @Order(130.0)
    public class YearColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Year");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(140.0)
    public class StreetColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Street");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 140;
      }

    }

    @Order(150.0)
    public class ZipColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Zip");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 65;
      }
    }

    @Order(160.0)
    public class CityColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("City");
      }

      @Override
      protected int getConfiguredWidth() {
        return 180;
      }
    }

    @Order(170.0)
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
        return 100;
      }
    }

    @Order(180.0)
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
        return 70;
      }
    }

    @Order(190.0)
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
      protected int getConfiguredWidth() {
        return 200;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(200.0)
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

    @Order(210.0)
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

    @Order(220.0)
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

    @Order(230.0)
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

    @Order(240.0)
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

    @Order(10.0)
    public class NewMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("NewButton");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() throws ProcessingException {
        RunnerForm form = new RunnerForm();
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(30.0)
    public class EditMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("EditMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        RunnerForm form = new RunnerForm();
        form.setRunnerNr(getRunnerNrColumn().getSelectedValue());
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(35.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(40.0)
    public class DeleteMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Runners"), getNameColumn().getSelectedValues())) {
          for (Long runnerNr : getRunnerNrColumn().getSelectedValues()) {
            RunnerBean bean = new RunnerBean();
            bean.setRunnerNr(runnerNr);
            BEANS.get(IRunnerProcessService.class).delete(bean);
          }
          reloadPage();
        }
      }
    }
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return RunnersSearchForm.class;
  }
}
