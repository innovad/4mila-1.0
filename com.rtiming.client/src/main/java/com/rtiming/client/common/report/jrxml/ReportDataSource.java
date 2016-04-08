package com.rtiming.client.common.report.jrxml;

import java.util.ArrayList;
import java.util.Map;

/**
 * 
 */
public class ReportDataSource extends ArrayList<Map<String, ?>> {

  private static final long serialVersionUID = 1L;

  @Override
  public boolean add(Map<String, ?> e) {
    if (e != null) {
      return super.add(e);
    }
    return false;
  }

}
