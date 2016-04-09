package com.rtiming.client.common.report.template;

import java.util.Map;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPageWithTable;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.report.DataSourceUtility;
import com.rtiming.shared.Icons;

public class ReportTemplateColumnsTablePage extends AbstractPageWithTable<ReportTemplateColumnsTablePage.Table> implements IHelpEnabledPage {

  private final Long reportTypeUid;

  public ReportTemplateColumnsTablePage(Long reportTypeUid) {
    super();
    this.reportTypeUid = reportTypeUid;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Columns");
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public DetailIdColumn getDetailIdColumn() {
      return getColumnSet().getColumnByClass(DetailIdColumn.class);
    }

    public ColumnHeaderIdColumn getColumnHeaderIdColumn() {
      return getColumnSet().getColumnByClass(ColumnHeaderIdColumn.class);
    }

    public ColumnTitleColumn getColumnTitleColumn() {
      return getColumnSet().getColumnByClass(ColumnTitleColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.FILE;
    }

    @Order(10.0)
    public class ColumnTitleColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Column");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }

    }

    @Order(20.0)
    public class ColumnHeaderIdColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return DataSourceUtility.COLUMN_HEADER;
      }

      @Override
      protected int getConfiguredWidth() {
        return 350;
      }

    }

    @Order(30.0)
    public class DetailIdColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return DataSourceUtility.DETAIL;
      }

      @Override
      protected int getConfiguredWidth() {
        return 350;
      }
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    IPageWithTable<?> page = ReportTemplateTablePageMapping.getTableForReportType(reportTypeUid);
    Object[][] table = null;
    if (page != null) {
      Map<String, Object> data = DataSourceUtility.createColumnHeadersParametersFromTable(page.getTable());
      table = new Object[data.size()][getTable().getColumnCount()];
      int k = 0;
      for (String key : data.keySet()) {
        table[k][getTable().getColumnTitleColumn().getColumnIndex()] = data.get(key);
        table[k][getTable().getDetailIdColumn().getColumnIndex()] = StringUtility.replace(key, DataSourceUtility.COLUMN_HEADER, DataSourceUtility.DETAIL);
        table[k][getTable().getColumnHeaderIdColumn().getColumnIndex()] = key;
        k++;
      }
    }
    importTableData(table);
  }

}
