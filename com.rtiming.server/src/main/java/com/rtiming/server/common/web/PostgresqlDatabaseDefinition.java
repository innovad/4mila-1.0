package com.rtiming.server.common.web;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.eclipse.scout.commons.TypeCastUtility;

import com.rtiming.server.common.database.jpa.JPA;

/**
 * 
 */
public class PostgresqlDatabaseDefinition extends AbstractDatabaseDefinition {

  @Override
  public List<String> getPrimaryKeys(String table) {
    String queryString = "SELECT UPPER(COLUMN_NAME) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS C " +
        "INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE CU ON C.CONSTRAINT_NAME = CU.CONSTRAINT_NAME " +
        "WHERE CONSTRAINT_TYPE = 'PRIMARY KEY' " +
        "AND UPPER(C.TABLE_NAME) = :tableName ";
    Query nativeQuery = JPA.currentEntityManager().createNativeQuery(queryString);
    nativeQuery.setParameter("tableName", table.toUpperCase());
    List result = nativeQuery.getResultList();
    List<String> primaryKeys = convertToStringList(result);
    return primaryKeys;
  }

  @Override
  public List<ColumnDefinition> getColumnDefinitions(String tableName, String[] columnFilter) {
    String query = "SELECT UPPER(COLUMN_NAME), UDT_NAME " +
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
      String type = TypeCastUtility.castValue(rowArray[1], String.class);
      int sqlType = Types.OTHER;
      if (type.startsWith("varchar")) {
        sqlType = Types.VARCHAR;
      }
      else if (type.startsWith("int")) {
        sqlType = Types.INTEGER;
      }
      else if (type.startsWith("time")) {
        sqlType = Types.TIMESTAMP;
      }
      else if (type.startsWith("date")) {
        sqlType = Types.TIMESTAMP;
      }
      else if (type.startsWith("bool")) {
        sqlType = Types.BOOLEAN;
      }
      else if (type.startsWith("float")) {
        sqlType = Types.DOUBLE;
      }
      columnNames.add(new ColumnDefinition(name, sqlType));
    }

    return columnNames;
  }

}
