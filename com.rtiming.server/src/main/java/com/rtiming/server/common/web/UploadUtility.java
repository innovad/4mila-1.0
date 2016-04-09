package com.rtiming.server.common.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Query;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com._4mila._4mila.jaxws.online.ObjectList;
import com._4mila._4mila.jaxws.online.RowData;
import com._4mila._4mila.jaxws.online.StringList;
import com._4mila._4mila.jaxws.online.TableData;
import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.setup.JPASetup;
import com.rtiming.shared.common.database.jpa.JPAStyle;

public final class UploadUtility {

  private static AbstractDatabaseDefinition databaseDefinition;

  private UploadUtility() {
  }

  private static AbstractDatabaseDefinition getDatabaseDefinition() {
    if (databaseDefinition == null) {
      if (JPAStyle.H2_EMBEDDED.equals(JPASetup.get().getStyle())) {
        databaseDefinition = new H2DatabaseDefinition();
      }
      else if (JPAStyle.POSTGRES.equals(JPASetup.get().getStyle())) {
        databaseDefinition = new PostgresqlDatabaseDefinition();
      }
      else {
        throw new RuntimeException("Unsupported database");
      }
    }
    return databaseDefinition;
  }

  private static final Logger LOG = LoggerFactory.getLogger(UploadUtility.class);

  private static ArrayList<Bind> buildBinds(TableData tableData, ObjectList row, List<ColumnDefinition> columnDefinitions) {
    Map<String, ColumnDefinition> lookup = new HashMap<>();
    for (ColumnDefinition def : columnDefinitions) {
      lookup.put(def.getName(), def);
    }

    ArrayList<Bind> binds = new ArrayList<Bind>();
    int i = 0;
    for (Object obj : row.getObjects()) {
      String colName = tableData.getColumns().getStrings().get(i);
      if (obj instanceof XMLGregorianCalendar) {
        XMLGregorianCalendar xml = (XMLGregorianCalendar) obj;
        obj = xml.toGregorianCalendar(TimeZone.getTimeZone("GMT"), null, null).getTime();
      }
      ColumnDefinition col = lookup.get(colName);
      Bind bind = new Bind(colName.toLowerCase(), obj, col.getType());
      binds.add(bind);
      i++;
    }
    return binds;
  }

  private static List<Bind> buildAllPrimaryKeyBinds(TableData tableData, List<String> primaryKeys) {
    Map<String, ColumnDefinition> lookup = new HashMap<>();
    for (ColumnDefinition def : getColumnDefinitions(tableData.getTableName(), null)) {
      lookup.put(def.getName(), def);
    }

    List<Bind> result = new ArrayList<Bind>();
    int pkCount = 0;
    for (ObjectList row : tableData.getData().getRows()) {
      int i = 0;
      for (Object obj : row.getObjects()) {
        String colName = tableData.getColumns().getStrings().get(i);
        if (primaryKeys.contains(colName)) {
          if (obj instanceof XMLGregorianCalendar) {
            XMLGregorianCalendar xml = (XMLGregorianCalendar) obj;
            obj = xml.toGregorianCalendar().getTime();
          }
          result.add(new Bind(colName.toLowerCase() + pkCount, obj, lookup.get(colName).getType()));
        }
        i++;
      }
      pkCount++;
    }
    return result;
  }

  public static void storeTable(TableData tableData, long clientNr, Long eventNr) throws ProcessingException {

    if (tableData.getData().getRows().size() > 0) {
      // fetch primary keys
      List<String> primaryKeys = getPrimaryKeys(tableData.getTableName());

      // build update statement
      StringBuffer update = buildUpdateStatement(tableData, primaryKeys);

      // build insert statement
      StringBuffer insert = buildInsertStatment(tableData);

      // bind data types
      List<ColumnDefinition> defs = getColumnDefinitions(tableData.getTableName(), null);

      // bind data rows
      for (ObjectList row : tableData.getData().getRows()) {
        List<Bind> binds = UploadUtility.buildBinds(tableData, row, defs);
        // update
        int upd = processDynamicQuery(update.toString(), binds);
        if (upd <= 0) {
          processDynamicQuery(insert.toString(), binds);
        }
      }
    }
  }

  public static void deleteTable(TableData tableData, Long clientNr, Long eventNr) throws ProcessingException {

    if (tableData.getData().getRows().size() > 0) {
      List<String> primaryKeys = getPrimaryKeys(tableData.getTableName());

      if (tableData.isForceCleanup()) {
        // build bind all statement
        List<Bind> deleteBinds = buildAllPrimaryKeyBinds(tableData, primaryKeys);

        // build delete statement
        StringBuffer delete = buildDeleteStatement(tableData, primaryKeys);

        // delete
        deleteBinds.add(new Bind("uploadClientNr", clientNr, Types.BIGINT));
        deleteBinds.add(new Bind("eventNr", eventNr, Types.BIGINT));
        processDynamicQuery(delete.toString(), deleteBinds);
      }
    }
  }

  /**
   * @param tableData
   * @param primaryKeys
   * @return
   */
  private static StringBuffer buildDeleteStatement(TableData tableData, List<String> primaryKeys) {
    StringBuffer delete = new StringBuffer();
    delete.append("DELETE FROM");
    delete.append(" ");
    delete.append(tableData.getTableName());
    delete.append(" ");
    delete.append("WHERE CLIENT_NR = :uploadClientNr");
    delete.append(" ");
    // and (key1,key2) not in ((value1,value2),(value3,value4))
    delete.append("AND (");
    boolean first = true;
    for (String primaryKey : primaryKeys) {
      if (!first) {
        delete.append(",");
      }
      delete.append(primaryKey);
      first = false;
    }
    delete.append(") NOT IN (");
    for (int k = 0; k < tableData.getData().getRows().size(); k++) {
      if (k > 0) {
        delete.append(",");
      }
      first = true;
      for (String primaryKey : primaryKeys) {
        if (first) {
          delete.append("(");
        }
        else {
          delete.append(",");
        }
        delete.append(":");
        delete.append(primaryKey.toLowerCase() + k);
        first = false;
      }
      delete.append(")");
    }
    delete.append(")");
    delete.append(" ");
    delete.append(UploadConstraints.getInstance().getConstraint(tableData.getTableName()));
    return delete;
  }

  /**
   * @param tableData
   * @param primaryKeys
   * @return
   * @throws ProcessingException
   */
  private static StringBuffer buildUpdateStatement(TableData tableData, List<String> primaryKeys) throws ProcessingException {
    StringBuffer update = new StringBuffer();
    update.append("UPDATE");
    update.append(" ");
    update.append(tableData.getTableName());
    update.append(" ");
    update.append("SET");
    update.append(" ");

    boolean first = true;
    for (String column : tableData.getColumns().getStrings()) {
      if (!primaryKeys.contains(column)) {
        if (first) {
          first = false;
        }
        else {
          update.append(",");
        }
        update.append(column);
        update.append("=");
        update.append(":");
        update.append(column.toLowerCase());
      }
    }
    if (first && primaryKeys.size() > 0) {
      // there wasn't any data column
      update.append(primaryKeys.get(0));
      update.append("=");
      update.append(primaryKeys.get(0));
    }
    else if (primaryKeys.size() == 0) {
      throw new ProcessingException("Primary Key required on table " + tableData.getTableName());
    }

    // build where
    update.append(" ");
    first = true;
    for (String primaryKeyColumn : primaryKeys) {
      update.append(" ");
      if (first) {
        update.append("WHERE");
        first = false;
      }
      else {
        update.append("AND");
      }
      update.append(" ");
      update.append(primaryKeyColumn);
      update.append("=");
      update.append(":");
      update.append(primaryKeyColumn.toLowerCase());
    }
    return update;
  }

  /**
   * @param tableData
   * @return
   */
  private static StringBuffer buildInsertStatment(TableData tableData) {
    boolean first;
    StringBuffer insert = new StringBuffer();
    insert.append("INSERT INTO");
    insert.append(" ");
    insert.append(tableData.getTableName());
    insert.append(" ");
    insert.append("(");
    insert.append(" ");

    first = true;
    for (String column : tableData.getColumns().getStrings()) {
      if (first) {
        first = false;
      }
      else {
        insert.append(",");
      }
      insert.append(column);
    }
    insert.append(")");
    insert.append(" ");
    insert.append("VALUES");
    insert.append(" ");
    insert.append("(");

    first = true;
    for (String column : tableData.getColumns().getStrings()) {
      if (first) {
        first = false;
      }
      else {
        insert.append(",");
      }
      insert.append(":");
      insert.append(column.toLowerCase());
    }

    insert.append(")");
    return insert;
  }

  public static TableData createUploadTable(String tableName, String[] columnFilter, Long eventNr, boolean cleanup) throws ProcessingException {

    // upload a table
    TableData tableData = new TableData();
    tableData.setTableName(tableName);
    tableData.setForceCleanup(cleanup);

    if (columnFilter.length == 0) {
      columnFilter = new String[]{"dummy"};
    }
    else {
      for (int k = 0; k < columnFilter.length; k++) {
        columnFilter[k] = columnFilter[k].toUpperCase();
      }
    }

    // column headers and type
    List<ColumnDefinition> columnDefinitions = getColumnDefinitions(tableName, columnFilter);
    Collection<String> columnNames = new ArrayList<>();
    for (ColumnDefinition columnDefinition : columnDefinitions) {
      columnNames.add(columnDefinition.getName());
    }

    StringList columns = new StringList();
    columns.getStrings().addAll(columnNames);
    tableData.setColumns(columns);

    // table data
    // build data select query
    StringBuffer select = new StringBuffer();
    select.append("SELECT");
    boolean first = true;
    for (String column : columnNames) {
      if (first) {
        select.append(" ");
        first = false;
      }
      else {
        select.append(", ");
      }
      select.append(column);
    }
    select.append(" ");
    select.append("FROM");
    select.append(" ");
    select.append(tableName);

    // build where
    select.append(" ");
    select.append("WHERE CLIENT_NR = :uploadClientNr");
    select.append(" ");
    select.append(UploadConstraints.getInstance().getConstraint(tableData.getTableName()));

    // load row data
    Query nativeQuery = JPA.currentEntityManager().createNativeQuery(select.toString());
    setParameterFailsafe("eventNr", eventNr, nativeQuery);
    setParameterFailsafe("uploadClientNr", ServerSession.get().getSessionClientNr(), nativeQuery);
    List data = nativeQuery.getResultList();

    RowData rowData = new RowData();

    for (Object rowObject : data) {
      ObjectList list = new ObjectList();
      if (rowObject instanceof Object[]) {
        Object[] row = (Object[]) rowObject;
        for (Object cell : row) {
          list.getObjects().add(cell);
        }
      }
      else {
        list.getObjects().add(rowObject);
      }
      rowData.getRows().add(list);
    }

    tableData.setData(rowData);

    return tableData;
  }

  private static void setParameterFailsafe(String parameter, Object value, Query nativeQuery) {
    try {
      nativeQuery.setParameter(parameter, value);
    }
    catch (IllegalArgumentException e) {
      // nop
    }
  }

  private static int processDynamicQuery(String queryString, List<Bind> binds) throws ProcessingException {
    int result = 0;
    Connection connection = JPA.getConnection();
    queryString += " ";

    try {
      // find next bind
      int nextPos = 0;
      int maxBindCount = 0;
      Bind nextBind = null;
      List<Bind> bindObjects = new ArrayList<>();
      while (queryString.contains(":") && maxBindCount < 10000) {
        for (Bind bind : binds) {
          Pattern bindPattern = Pattern.compile("(:" + bind.getName() + ")\\W");
          Matcher match = bindPattern.matcher(queryString);
          if (match.find()) {
            int candPos = match.start(0);
            if (candPos > 0 && nextPos == 0) {
              nextPos = candPos;
              nextBind = bind;
            }
            else if (candPos > 0 && candPos < nextPos) {
              nextPos = candPos;
              nextBind = bind;
            }
          }

        }
        if (nextBind != null) {
          queryString = queryString.substring(0, nextPos) + "?" + queryString.substring(1 + nextBind.getName().length() + nextPos);
          bindObjects.add(nextBind);
        }
        nextPos = 0;
        maxBindCount++;
      }

      if (maxBindCount >= 10000) {
        throw new ProcessingException("maximum binds reached");
      }

      PreparedStatement st = connection.prepareStatement(queryString);
      int k = 1;
      for (Bind bind : bindObjects) {
        if (bind.getSqlType() == Types.OTHER) {
          System.out.println(bind);
        }
        st.setObject(k, bind.getValue(), bind.getSqlType());
        k++;
      }
      result = st.executeUpdate();
      LOG.info(queryString);
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  protected static List<String> getPrimaryKeys(String table) {
    return getDatabaseDefinition().getPrimaryKeys(table);
  }

  protected static List<ColumnDefinition> getColumnDefinitions(String tableName, String[] columnFilter) {
    return getDatabaseDefinition().getColumnDefinitions(tableName, columnFilter);
  }

}
