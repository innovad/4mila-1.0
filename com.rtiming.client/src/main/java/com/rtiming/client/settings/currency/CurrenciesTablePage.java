package com.rtiming.client.settings.currency;

import java.math.BigDecimal;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.ISettingsOutlineService;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;

public class CurrenciesTablePage extends AbstractPageWithTable<CurrenciesTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Currencies");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(ISettingsOutlineService.class).getCurrencyTableData());
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    public ShortcutColumn getShortcutColumn() {
      return getColumnSet().getColumnByClass(ShortcutColumn.class);
    }

    @Override
    protected void execDecorateRow(ITableRow row) throws ProcessingException {
      if (CompareUtility.equals(getCurrencyUidColumn().getValue(row), getDefaultCurrencyUidColumn().getValue(row))) {
        FontSpec font = FontSpec.parse("bold");
        row.setFont(font);
      }
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.PAYMENT;
    }

    public DefaultCurrencyUidColumn getDefaultCurrencyUidColumn() {
      return getColumnSet().getColumnByClass(DefaultCurrencyUidColumn.class);
    }

    public ExchangeRateColumn getExchangeRateColumn() {
      return getColumnSet().getColumnByClass(ExchangeRateColumn.class);
    }

    public CurrencyNameColumn getCurrencyNameColumn() {
      return getColumnSet().getColumnByClass(CurrencyNameColumn.class);
    }

    public CurrencyUidColumn getCurrencyUidColumn() {
      return getColumnSet().getColumnByClass(CurrencyUidColumn.class);
    }

    @Order(10.0)
    public class CurrencyUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Currency");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return CurrencyCodeType.class;

      }

    }

    @Order(20.0)
    public class DefaultCurrencyUidColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(30.0)
    public class CurrencyNameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Currency");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }

    }

    @Order(40.0)
    public class ShortcutColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Shortcut");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }
    }

    @Order(50.0)
    public class ExchangeRateColumn extends AbstractBigDecimalColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("ExchangeRate");
      }

      @Override
      protected int getConfiguredMaxFractionDigits() {
        return 6;
      }

      @Override
      protected int getConfiguredMinFractionDigits() {
        return 6;
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
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
        CurrencyForm form = new CurrencyForm();
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
        CurrencyForm form = new CurrencyForm();
        form.setCurrencyUid(getCurrencyUidColumn().getSelectedValue());
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
    public class CurrencySetDefaultCurrencyMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return Texts.get("CurrencySetDefaultCurrency");
      }

      @Override
      protected void execAction() throws ProcessingException {
        // set as default
        BEANS.get(IDefaultProcessService.class).setDefaultCurrencyUid(getCurrencyUidColumn().getSelectedValue());

        // default currency must have exchange rate 1
        CurrencyFormData currency = new CurrencyFormData();
        currency.setCurrencyUid(getCurrencyUidColumn().getSelectedValue());
        currency = BEANS.get(ICurrencyProcessService.class).load(currency);
        currency.getExchangeRate().setValue(BigDecimal.ONE);
        BEANS.get(ICurrencyProcessService.class).store(currency);

        reloadPage();
      }
    }
  }
}
