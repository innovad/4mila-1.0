package com.rtiming.client.common.report.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPageWithTable;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

public class ReportTemplateColumnLookupCall extends LocalLookupCall {

  private Long reportTypeUid;
  private static final long serialVersionUID = 1L;
  private static IPageWithTable<?> page;

  @Override
  protected List<LookupRow> execCreateLookupRows() throws ProcessingException {
    page = ReportTemplateTablePageMapping.getTableForReportType(reportTypeUid);
    List<IColumn<?>> columns = page.getTable().getColumns();
    ArrayList<LookupRow> rows = new ArrayList<LookupRow>();
    for (IColumn<?> column : columns) {
      LookupRow row = new LookupRow(column.getColumnId(), column.getHeaderCell().getText());
      rows.add(row);
    }
    return rows;
  }

  public void setReportTypeUid(Long reportTypeUid) {
    this.reportTypeUid = reportTypeUid;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((reportTypeUid == null) ? 0 : reportTypeUid.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof ReportTemplateColumnLookupCall)) {
      return false;
    }
    ReportTemplateColumnLookupCall other = (ReportTemplateColumnLookupCall) obj;
    if (reportTypeUid == null) {
      if (other.reportTypeUid != null) {
        return false;
      }
    }
    else if (!reportTypeUid.equals(other.reportTypeUid)) {
      return false;
    }
    return true;
  }

}
