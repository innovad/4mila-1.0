package com.rtiming.client.common.database;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.HeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.IHeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.shared.common.database.IDatabaseService;
import com.rtiming.shared.common.database.TableInfoBean;

public class TableColumnsTablePage extends AbstractPageWithTable<TableColumnsTablePage.Table> implements IHelpEnabledPage {

  private final String tableId;

  public TableColumnsTablePage(String tableId) {
    this.tableId = tableId;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Columns");
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected boolean getConfiguredAutoResizeColumns() {
      return true;
    }

    @Override
    protected void injectColumnsInternal(OrderedCollection<IColumn<?>> columnList) {
      for (int i = 0; i < 30; i++) {
        columnList.addLast(new DataColumn());
      }
    }

  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    TableInfoBean bean = BEANS.get(IDatabaseService.class).getColumns(tableId);

    int i = 0;
    for (String columnTitle : bean.getColumns()) {
      IColumn<?> col = getTable().getColumns().get(i);

      // Title
      IHeaderCell cell = col.getHeaderCell();
      HeaderCell hcell = (HeaderCell) cell;
      hcell.setText(columnTitle);
      i++;

      // Display
      col.setDisplayable(true);
      col.setVisible(true);
    }

    importTableData(bean.getData());
  }

  class DataColumn extends AbstractStringColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }

  }

}
