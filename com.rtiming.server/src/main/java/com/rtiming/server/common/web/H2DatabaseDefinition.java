package com.rtiming.server.common.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;

import com.rtiming.server.common.database.jpa.JPA;

/**
 * 
 */
public class H2DatabaseDefinition extends AbstractDatabaseDefinition {

  @Override
  public List<String> getPrimaryKeys(String table) {
    String queryString = "SELECT UPPER(COLUMN_LIST) FROM INFORMATION_SCHEMA.CONSTRAINTS C " +
        "WHERE CONSTRAINT_TYPE = 'PRIMARY KEY' " +
        "AND UPPER(C.TABLE_NAME) = :tableName ";
    Query nativeQuery = JPA.currentEntityManager().createNativeQuery(queryString);
    nativeQuery.setParameter("tableName", table.toUpperCase());
    List result = nativeQuery.getResultList();
    String keys = StringUtility.emptyIfNull(result.get(0));
    String[] keyArray = keys.split(",");
    return Arrays.asList(keyArray);
  }

  @Override
  public List<ColumnDefinition> getColumnDefinitions(String tableName, String[] columnFilter) {
    String query = "SELECT UPPER(COLUMN_NAME), DATA_TYPE " +
        "FROM INFORMATION_SCHEMA.COLUMNS " +
        "WHERE UPPER(TABLE_NAME) = :table " +
        (columnFilter != null ? "AND UPPER(COLUMN_NAME) NOT IN :columnFilter " : "");
    Query nativeQuery = JPA.currentEntityManager().createNativeQuery(query);
    nativeQuery.setParameter("table", tableName.toUpperCase());
    if (columnFilter != null) {
      nativeQuery.setParameter("columnFilter", Arrays.asList(columnFilter));
    }
    List result = nativeQuery.getResultList();

    List<ColumnDefinition> columnNames = new ArrayList<>();
    for (Object row : result) {
      Object[] rowArray = (Object[]) row;
      String name = TypeCastUtility.castValue(rowArray[0], String.class);
      int sqlType = TypeCastUtility.castValue(rowArray[1], Integer.class);
      columnNames.add(new ColumnDefinition(name, sqlType));
    }

    return columnNames;
  }

}
