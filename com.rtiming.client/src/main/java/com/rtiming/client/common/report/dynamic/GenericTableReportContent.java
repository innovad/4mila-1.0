package com.rtiming.client.common.report.dynamic;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.scout.rt.client.ui.basic.table.ITable;

/**
 * 
 */
public class GenericTableReportContent {

  private ITable table;
  private Map<String, Object> parameters;

  public ITable getTable() {
    return table;
  }

  public void setTable(ITable table) {
    this.table = table;
  }

  public void addParameter(String key, Object value) {
    if (parameters == null) {
      parameters = new HashMap<String, Object>();
    }
    parameters.put(key, value);
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  @Override
  public String toString() {
    return "ReportContent [table.rowCount=" + table != null ? "" + table.getRowCount() : table + ", parameters=" + parameters + "]";
  }

}
