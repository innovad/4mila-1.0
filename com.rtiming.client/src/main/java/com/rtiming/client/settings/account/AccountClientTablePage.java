package com.rtiming.client.settings.account;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.event.EventsTablePage;
import com.rtiming.shared.settings.ISettingsOutlineService;

public class AccountClientTablePage extends AbstractPageWithTable<AccountClientTablePage.Table> {

  private final Long accountNr;

  public AccountClientTablePage(Long accountNr) {
    this.accountNr = accountNr;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Local");
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    return new EventsTablePage(getTable().getNumberColumn().getValue(row));
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(ISettingsOutlineService.class).getAccountClientTableData(accountNr));
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public TypeColumn getTypeColumn() {
      return getColumnSet().getColumnByClass(TypeColumn.class);
    }

    public NumberColumn getNumberColumn() {
      return getColumnSet().getColumnByClass(NumberColumn.class);
    }

    @Order(10.0)
    public class NumberColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Number");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 2;
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(20.0)
    public class TypeColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Type");
      }

      @Override
      protected int getConfiguredHorizontalAlignment() {
        return 1;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }
  }
}
