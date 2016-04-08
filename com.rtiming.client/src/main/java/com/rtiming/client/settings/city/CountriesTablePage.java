package com.rtiming.client.settings.city;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.CountryIcons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.ISettingsOutlineService;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;

public class CountriesTablePage extends AbstractPageWithTable<CountriesTablePage.Table> implements IHelpEnabledPage {

  private final ArrayList<String> flagShortcuts = new ArrayList<String>();

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Countries");
  }

  @Override
  protected void execInitPage() throws ProcessingException {
    Field[] flags = CountryIcons.class.getDeclaredFields();
    for (Field field : flags) {
      flagShortcuts.add(StringUtility.lowercase(field.getName()));
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(ISettingsOutlineService.class).getCountryTableData());
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    public NationColumn getNationColumn() {
      return getColumnSet().getColumnByClass(NationColumn.class);
    }

    public DefaultCountryUidColumn getDefaultCountryUidColumn() {
      return getColumnSet().getColumnByClass(DefaultCountryUidColumn.class);
    }

    @Override
    protected void execDecorateRow(ITableRow row) throws ProcessingException {
      if (CompareUtility.equals(getCountryUidColumn().getValue(row), getDefaultCountryUidColumn().getValue(row))) {
        FontSpec font = FontSpec.parse("bold");
        row.setFont(font);
      }
      row.setIconId(StringUtility.lowercase(getTable().getCountryCodeColumn().getValue(row)));
    }

    public CountryCodeColumn getCountryCodeColumn() {
      return getColumnSet().getColumnByClass(CountryCodeColumn.class);
    }

    public CountryColumn getCountryColumn() {
      return getColumnSet().getColumnByClass(CountryColumn.class);
    }

    public CountryUidColumn getCountryUidColumn() {
      return getColumnSet().getColumnByClass(CountryUidColumn.class);
    }

    @Order(10.0)
    public class CountryUidColumn extends AbstractLongColumn {

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
    public class DefaultCountryUidColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(30.0)
    public class CountryColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Country");
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
    public class CountryCodeColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("CountryCode");
      }

      @Override
      protected int getConfiguredWidth() {
        return 50;
      }
    }

    @Order(50.0)
    public class NationColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Nation");
      }

      @Override
      protected int getConfiguredWidth() {
        return 50;
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
        CountryForm form = new CountryForm();
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
        CountryForm form = new CountryForm();
        form.setCountryUid(getCountryUidColumn().getSelectedValue());
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
    public class CountrySetDefaultCountryMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return Texts.get("CountrySetDefaultCountry");
      }

      @Override
      protected void execAction() throws ProcessingException {
        BEANS.get(IDefaultProcessService.class).setDefaultCountryUid(getCountryUidColumn().getSelectedValue());
        reloadPage();
      }
    }

    @Order(50.0)
    public class Separator2Menu extends AbstractSeparatorMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

    }

    @Order(60.0)
    public class DeleteMenu extends AbstractMenu {

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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Countries"), getCountryColumn().getSelectedValues())) {
          for (Long countryUid : getTable().getCountryUidColumn().getSelectedValues()) {
            CountryFormData country = new CountryFormData();
            country.setCountryUid(countryUid);
            BEANS.get(ICountryProcessService.class).delete(country);
          }
          reloadPage();
        }
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(true);
        for (ITableRow row : getSelectedRows()) {
          if (flagShortcuts.contains(row.getIconId())) {
            setEnabled(false);
          }
        }
      }
    }
  }
}
