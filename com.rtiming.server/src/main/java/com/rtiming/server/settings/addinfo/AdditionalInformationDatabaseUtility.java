package com.rtiming.server.settings.addinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.TypeCastUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;

public final class AdditionalInformationDatabaseUtility {

  private static DDLExecutor ddlExecutor;

  public AdditionalInformationDatabaseUtility() {
  }

  public static void createColumn(Long entityUid, Long typeUid, String shortcut) throws ProcessingException {
    if (entityUid == null || typeUid == null || shortcut == null) {
      throw new IllegalArgumentException("arguments required");
    }
    String table = getTableName(entityUid);
    String type = getType(typeUid);
    String column = getColumnName(shortcut);
    String ddl = "alter table " + table + " add column " + column + " " + type;
    executeDDL(ddl);
  }

  public static void renameColumn(Long entityUid, String newShortcut, String oldShortcut) throws ProcessingException {
    String table = getTableName(entityUid);
    String newColumn = getColumnName(newShortcut);
    String oldColumn = getColumnName(oldShortcut);
    String ddl = "alter table " + table + " alter column " + oldColumn + " rename to " + newColumn;
    executeDDL(ddl);
  }

  public static void dropColumn(Long entityUid, String shortcut) throws ProcessingException {
    String table = getTableName(entityUid);
    String column = getColumnName(shortcut);
    String ddl = "alter table " + table + " drop column " + column;
    executeDDL(ddl);
  }

  protected static String getColumnName(String shortcut) throws ProcessingException {
    if (!StringUtility.hasText(shortcut)) {
      throw new VetoException("Illegal column name " + shortcut);
    }
    shortcut = StringUtility.replace(shortcut, " ", "").toLowerCase();
    return "ai_" + shortcut;
  }

  protected static String getType(Long typeUid) {
    String type = null;
    if (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.BooleanCode.ID)) {
      type = "boolean";
    }
    else if (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.DoubleCode.ID)) {
      type = "double";
    }
    else if (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.IntegerCode.ID)) {
      type = "integer";
    }
    else if (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.SmartfieldCode.ID)) {
      type = "integer";
    }
    else if (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.TextCode.ID)) {
      type = "varchar";
    }
    if (StringUtility.isNullOrEmpty(type)) {
      throw new UnsupportedOperationException("Type " + typeUid + " not supported.");
    }
    return type;
  }

  protected static String getTableName(Long entityUid) {
    String table = null;
    if (CompareUtility.equals(entityUid, EntityCodeType.RunnerCode.ID)) {
      table = "rt_runner";
    }
    else if (CompareUtility.equals(entityUid, EntityCodeType.EntryCode.ID)) {
      table = "rt_entry";
    }
    else if (CompareUtility.equals(entityUid, EntityCodeType.ClubCode.ID)) {
      table = "rt_club";
    }
    if (StringUtility.isNullOrEmpty(table)) {
      throw new UnsupportedOperationException("Entity " + entityUid + " not supported.");
    }
    return table;
  }

  public static void updateValue(Long entityUid, Long joinNr, Long clientNr, AdditionalInformationValueBean value) throws ProcessingException {
    String extKey = FMilaUtility.getCodeExtKey(AdditionalInformationCodeType.class, value.getAdditionalInformationUid());
    String column = AdditionalInformationDatabaseUtility.getColumnName(extKey);
    String table = AdditionalInformationDatabaseUtility.getTableName(entityUid);
    String pkColumn = AdditionalInformationDatabaseUtility.getPkColumn(entityUid);
    String updateQuery = "update " + table + " set " + column + " = :value where " + pkColumn + " = :pk and client_nr = :clientNr";
    Query query = JPA.currentEntityManager().createNativeQuery(updateQuery);
    if (CompareUtility.equals(value.getTypeUid(), AdditionalInformationTypeCodeType.TextCode.ID)) {
      query.setParameter("value", value.getValueString());
    }
    else if (CompareUtility.equals(value.getTypeUid(), AdditionalInformationTypeCodeType.DoubleCode.ID)) {
      query.setParameter("value", value.getValueDouble());
    }
    else {
      query.setParameter("value", value.getValueInteger());
    }
    query.setParameter("pk", joinNr);
    query.setParameter("clientNr", NumberUtility.nvl(clientNr, ServerSession.get().getSessionClientNr()));
    query.executeUpdate();
  }

  public static List<AdditionalInformationValueBean> selectValue(Long entityUid, Collection<Long> joinNrs, Long clientNr, List<AdditionalInformationValueBean> values) throws ProcessingException {
    if (values == null || values.isEmpty()) {
      return values;
    }
    List<?> results = selectValues(entityUid, joinNrs == null, joinNrs, clientNr, values);

    Object[] selectedValues = new Object[]{};
    if (results.size() > 0) {
      selectedValues = (Object[]) results.get(0);
    }

    int k = 0;
    for (Object result : selectedValues) {
      if (k == 0 && joinNrs == null) {
        // first column is pkNr in case no joinNr is selected
        continue;
      }
      AdditionalInformationValueBean value = values.get(k);
      if (CompareUtility.equals(value.getTypeUid(), AdditionalInformationTypeCodeType.TextCode.ID)) {
        value.setValueString(TypeCastUtility.castValue(result, String.class));
      }
      else if (CompareUtility.equals(value.getTypeUid(), AdditionalInformationTypeCodeType.DoubleCode.ID)) {
        value.setValueDouble(TypeCastUtility.castValue(result, Double.class));
      }
      else {
        value.setValueInteger(TypeCastUtility.castValue(result, Long.class));
      }
      k++;
    }

    return values;
  }

  public static List<?> selectValues(Long entityUid, boolean selectPrimaryKey, Collection<Long> joinNrs, Long clientNr, List<AdditionalInformationValueBean> values) throws ProcessingException {
    List<String> columns = new ArrayList<>();
    if (values == null || values.isEmpty()) {
      return columns;
    }
    for (AdditionalInformationValueBean value : values) {
      String extKey = FMilaUtility.getCodeExtKey(AdditionalInformationCodeType.class, value.getAdditionalInformationUid());
      String column = AdditionalInformationDatabaseUtility.getColumnName(extKey);
      columns.add(column);
    }
    String table = AdditionalInformationDatabaseUtility.getTableName(entityUid);
    String pkColumn = AdditionalInformationDatabaseUtility.getPkColumn(entityUid);
    if (selectPrimaryKey) {
      columns.add(0, pkColumn);
    }
    String selectQuery = "select " + CollectionUtility.format(columns) + " from " + table + " where 1=1" + (joinNrs == null ? "" : " and " + pkColumn + " in :pk ") + " and client_nr = :clientNr";

    Query query = JPA.currentEntityManager().createNativeQuery(selectQuery);
    if (joinNrs != null) {
      query.setParameter("pk", joinNrs);
    }
    query.setParameter("clientNr", NumberUtility.nvl(clientNr, ServerSession.get().getSessionClientNr()));
    List<?> results = query.getResultList();
    if (results.size() > 0 && values.size() == 1 && !selectPrimaryKey) {
      // only one column: convert to array
      List<Object[]> convertedResults = new ArrayList<>();
      for (Object o : results) {
        convertedResults.add(new Object[]{o});
      }
      return convertedResults;
    }

    return results;
  }

  public static Map<Long, Object[]> selectTableData(Long entityUid, Set<Long> pkNrs) throws ProcessingException {
    List<AdditionalInformationValueBean> ais = SharedCache.getAddInfoForEntity(entityUid, ServerSession.get().getSessionClientNr());
    List<?> list = selectValues(entityUid, true, pkNrs, ServerSession.get().getSessionClientNr(), ais);
    Map<Long, Object[]> result = new HashMap<>();
    for (Object o : list) {
      Object[] row = (Object[]) o;
      result.put(TypeCastUtility.castValue(row[0], Long.class), Arrays.copyOfRange(row, 1, row.length));
    }
    return result;
  }

  public static Long[] getMatchingPrimaryKeys(Long addInfoUid, Long entityUid, Object from, Object equal, Object to) throws ProcessingException {
    String extKey = FMilaUtility.getCodeExtKey(AdditionalInformationCodeType.class, addInfoUid);
    String column = AdditionalInformationDatabaseUtility.getColumnName(extKey);
    String pk = getPkColumn(entityUid);

    String table = AdditionalInformationDatabaseUtility.getTableName(entityUid);
    StringBuilder sql = new StringBuilder();
    sql.append(" select ");
    sql.append(pk);
    sql.append(" from ");
    sql.append(table);
    sql.append(" where 1=1 ");
    if (from != null) {
      sql.append(" and ");
      sql.append(" :from <= ");
      sql.append(column);
    }
    if (to != null) {
      sql.append(" and ");
      sql.append(column);
      sql.append(" <= :to ");
    }
    if (equal != null) {
      sql.append(" and ");
      sql.append(column);
      if (equal instanceof String) {
        sql.append(" like '%' || :value || '%' ");
      }
      else {
        sql.append(" = :value ");
      }
    }
    if (from == null && to == null && equal == null) {
      sql.append(" and ");
      sql.append(column);
      sql.append(" is not null ");
    }
    sql.append(" and ");
    sql.append(" client_nr = :clientNr ");

    Query query = JPA.currentEntityManager().createNativeQuery(sql.toString());
    if (from != null) {
      query.setParameter("from", from);
    }
    if (to != null) {
      query.setParameter("to", to);
    }
    if (equal != null) {
      query.setParameter("value", equal);
    }
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    List result = query.getResultList();

    List<Long> longs = new ArrayList<>();
    for (Object o : result) {
      longs.add(TypeCastUtility.castValue(o, Long.class));
    }

    if (longs.size() == 0) {
      longs.add(0L);
    }
    return longs.toArray(new Long[longs.size()]);
  }

  protected static String getPkColumn(Long entityUid) {
    String pkColumn = null;
    if (CompareUtility.equals(entityUid, EntityCodeType.RunnerCode.ID)) {
      pkColumn = "runner_nr";
    }
    else if (CompareUtility.equals(entityUid, EntityCodeType.EntryCode.ID)) {
      pkColumn = "entry_nr";
    }
    else if (CompareUtility.equals(entityUid, EntityCodeType.ClubCode.ID)) {
      pkColumn = "club_nr";
    }
    if (StringUtility.isNullOrEmpty(pkColumn)) {
      throw new UnsupportedOperationException("Entity " + entityUid + " not supported.");
    }
    return pkColumn;
  }

  protected static void executeDDL(final String ddl) throws ProcessingException {
    if (ddlExecutor == null) {
      ddlExecutor = new DDLExecutor();
    }
    ddlExecutor.executeDDL(ddl);
  }

  protected static void setDDLExecutor(DDLExecutor ddlExecutor) {
    AdditionalInformationDatabaseUtility.ddlExecutor = ddlExecutor;
  }

  protected static class DDLExecutor {

    protected void executeDDL(final String ddl) throws ProcessingException {
// TODO MIG      
//      // ddl runs in a seperate job because it commits anyway
//      ServerJob job = new ServerJob("ddl", ServerSession.get()) {
//        @Override
//        protected IStatus runTransaction(IProgressMonitor monitor) throws Exception {
//          Connection conn = null;
//          try {
//            conn = JPA.getConnection();
//            Statement stmt = conn.createStatement();
//            stmt.execute(ddl);
//          }
//          catch (SQLException e) {
//            throw new ProcessingException("failed executing ddl statement", e);
//          }
//          return Status.OK_STATUS;
//        }
//      };
//      job.schedule();
//      try {
//        job.join();
//      }
//      catch (InterruptedException e) {
//        throw new ProcessingException("failed executing ddl statement", e);
//      }
//      if (!job.getResult().isOK()) {
//        throw new ProcessingException("failed executing ddl statement", job.getResult().getException());
//      }
    }

  }

  public static Set<Long> convertToPrimaryKeyList(List<Object[]> resultList, int position) {
    if (resultList == null || resultList.isEmpty()) {
      return Collections.emptySet();
    }
    Set<Long> pkNrs = new HashSet<>();
    for (Object[] row : resultList) {
      if (row.length > 0) {
        pkNrs.add(TypeCastUtility.castValue(row[position], Long.class));
      }
    }
    return pkNrs;
  }

}
