package com.rtiming.server.common.web;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.StringUtility;

public abstract class AbstractDatabaseDefinition {

  public abstract List<String> getPrimaryKeys(String table);

  public abstract List<ColumnDefinition> getColumnDefinitions(String tableName, String[] columnFilter);

  protected static List<String> convertToStringList(List result) {
    List<String> columnNames = new ArrayList<>();
    for (Object row : result) {
      columnNames.add(StringUtility.emptyIfNull(row));
    }
    return columnNames;
  }

}
