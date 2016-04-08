package com.rtiming.shared.common.database;

import java.io.Serializable;

public class TableInfoBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Object[][] data;
  private String[] columns;

  public String[] getColumns() {
    return columns;
  }

  public Object[][] getData() {
    return data;
  }

  public void setColumns(String[] columns) {
    this.columns = columns;
  }

  public void setData(Object[][] data) {
    this.data = data;
  }

}
