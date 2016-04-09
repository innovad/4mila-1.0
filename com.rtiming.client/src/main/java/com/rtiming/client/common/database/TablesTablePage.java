package com.rtiming.client.common.database;

import java.util.List;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.shared.common.database.IDatabaseService;

public class TablesTablePage extends AbstractPageWithTable<TablesTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Tables");
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    TableColumnsTablePage childPage = new TableColumnsTablePage(getTable().getTableColumn().getValue(row));
    return childPage;
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<String> tables = BEANS.get(IDatabaseService.class).getTables();
    Object[][] data = new Object[tables.size()][1];
    int i = 0;
    for (String table : tables) {
      data[i][0] = table;
      i++;
    }
    importTableData(data);
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public TableColumn getTableColumn() {
      return getColumnSet().getColumnByClass(TableColumn.class);
    }

    @Order(10.0)
    public class TableColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Table");
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

  }
}
