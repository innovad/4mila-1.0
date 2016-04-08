package com.rtiming.client.settings.city;

import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.settings.ISettingsOutlineService;
import com.rtiming.shared.settings.city.AreaCodeType;
import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.CitySearchFormData;
import com.rtiming.shared.settings.city.CountryCodeType;
import com.rtiming.shared.settings.city.ICityProcessService;

public class CitiesTablePage extends AbstractPageWithTable<CitiesTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Cities");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(ISettingsOutlineService.class).getCityTableData((CitySearchFormData) getSearchFilter().getFormData()));
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected void execDecorateCell(Cell view, ITableRow row, IColumn<?> col) throws ProcessingException {
      if (col instanceof CountryColumn) {
        view.setIconId(StringUtility.lowercase(getTable().getCountryCodeColumn().getValue(row)));
      }
    }

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    public ZipColumn getZipColumn() {
      return getColumnSet().getColumnByClass(ZipColumn.class);
    }

    public RegionColumn getRegionColumn() {
      return getColumnSet().getColumnByClass(RegionColumn.class);
    }

    public CountryColumn getCountryColumn() {
      return getColumnSet().getColumnByClass(CountryColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.CITY;
    }

    public CountColumn getCountColumn() {
      return getColumnSet().getColumnByClass(CountColumn.class);
    }

    public CountryCodeColumn getCountryCodeColumn() {
      return getColumnSet().getColumnByClass(CountryCodeColumn.class);
    }

    public AreaColumn getAreaColumn() {
      return getColumnSet().getColumnByClass(AreaColumn.class);
    }

    public CityColumn getCityColumn() {
      return getColumnSet().getColumnByClass(CityColumn.class);
    }

    public CityNrColumn getCityNrColumn() {
      return getColumnSet().getColumnByClass(CityNrColumn.class);
    }

    @Order(10.0)
    public class CityNrColumn extends AbstractLongColumn {

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
    public class ZipColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Zip");
      }
    }

    @Order(30.0)
    public class CityColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("City");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(40.0)
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
    }

    @Order(50.0)
    public class RegionColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Region");
      }
    }

    @Order(60.0)
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
        return 250;
      }
    }

    @Order(70.0)
    public class CountryCodeColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("CountryCode");
      }

      @Override
      protected int getConfiguredWidth() {
        return 65;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(80.0)
    public class CountColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Count");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
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
        CityForm form = new CityForm();
        form.setCityNr(getCityNrColumn().getSelectedValue());
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(20.0)
    public class EditMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("EditMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        CityForm form = new CityForm();
        form.setCityNr(getCityNrColumn().getSelectedValue());
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(30.0)
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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Cities"), getCityColumn().getSelectedValues())) {
          for (Long cityNr : getCityNrColumn().getSelectedValues()) {
            CityFormData city = new CityFormData();
            city.setCityNr(cityNr);
            BEANS.get(ICityProcessService.class).delete(city);
          }
          reloadPage();
        }
      }
    }
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return CitiesSearchForm.class;
  }

}
