package com.rtiming.client.common.report.jrxml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class SubreportDataSource extends HashMap<String, Object> {

  private static final long serialVersionUID = 1L;

  public void putRunnersData(Collection<Map<String, ?>> runnerRows) {
    this.put("tables", runnerRows);
  }

  public void putHeaderData(Map<String, ?> parameters) {
    this.put("parameters", parameters);
  }

}
