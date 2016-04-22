package com.rtiming.server.settings.addinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rtiming.server.settings.addinfo.AdditionalInformationDatabaseUtility.DDLExecutor;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtEntry;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;

public class AdditionalInformationDatabaseUtilityTest {

  private TestDDLExecutor testDDLexecutor = new TestDDLExecutor();

  @Test(expected = IllegalArgumentException.class)
  public void testCreateColumn1() throws ProcessingException {
    AdditionalInformationDatabaseUtility.createColumn(null, null, null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateColumn2() throws ProcessingException {
    AdditionalInformationDatabaseUtility.createColumn(-1L, 1L, "abc");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testCreateColumn3() throws ProcessingException {
    AdditionalInformationDatabaseUtility.createColumn(EntityCodeType.ClubCode.ID, 1L, "abc");
  }

  @Test
  public void testCreateColumn4() throws ProcessingException {
    AdditionalInformationDatabaseUtility.createColumn(EntityCodeType.ClubCode.ID, AdditionalInformationTypeCodeType.BooleanCode.ID, "abc");
    assertEquals("alter table rt_club add column ai_abc boolean", testDDLexecutor.getDDL());
  }

  @Test(expected = VetoException.class)
  public void testGetColumnName1() throws Exception {
    AdditionalInformationDatabaseUtility.getColumnName(null);
  }

  @Test(expected = VetoException.class)
  public void testGetColumnName2() throws Exception {
    AdditionalInformationDatabaseUtility.getColumnName(" ");
  }

  @Test
  public void testGetColumnName3() throws Exception {
    String result = AdditionalInformationDatabaseUtility.getColumnName(" a");
    assertEquals("ai_a", result);
  }

  @Test
  public void testGetColumnName4() throws Exception {
    String result = AdditionalInformationDatabaseUtility.getColumnName("123");
    assertEquals("ai_123", result);
  }

  @Test
  public void testGetType1() throws Exception {
    String result = AdditionalInformationDatabaseUtility.getType(AdditionalInformationTypeCodeType.BooleanCode.ID);
    assertEquals("boolean", result);
  }

  @Test
  public void testGetType2() throws Exception {
    String result = AdditionalInformationDatabaseUtility.getType(AdditionalInformationTypeCodeType.DoubleCode.ID);
    assertEquals("double", result);
  }

  @Test
  public void testGetType3() throws Exception {
    String result = AdditionalInformationDatabaseUtility.getType(AdditionalInformationTypeCodeType.IntegerCode.ID);
    assertEquals("integer", result);
  }

  @Test
  public void testGetType4() throws Exception {
    String result = AdditionalInformationDatabaseUtility.getType(AdditionalInformationTypeCodeType.TextCode.ID);
    assertEquals("varchar", result);
  }

  @Test
  public void testGetType5() throws Exception {
    String result = AdditionalInformationDatabaseUtility.getType(AdditionalInformationTypeCodeType.SmartfieldCode.ID);
    assertEquals("integer", result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetType6() throws Exception {
    AdditionalInformationDatabaseUtility.getType(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetType7() throws Exception {
    AdditionalInformationDatabaseUtility.getType(-1L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetTableName1() throws Exception {
    AdditionalInformationDatabaseUtility.getTableName(-1L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetTableName2() throws Exception {
    AdditionalInformationDatabaseUtility.getTableName(null);
  }

  @Test
  public void testGetTableName3() throws Exception {
    String result = AdditionalInformationDatabaseUtility.getTableName(EntityCodeType.EntryCode.ID);
    assertEquals(RtEntry.class.getSimpleName().toString().toLowerCase(), StringUtility.replace(result, "_", ""));
  }

  @Test
  public void testGetTableName4() throws Exception {
    String result = AdditionalInformationDatabaseUtility.getTableName(EntityCodeType.RunnerCode.ID);
    assertEquals(RtRunner.class.getSimpleName().toString().toLowerCase(), StringUtility.replace(result, "_", ""));
  }

  @Test
  public void testGetTableName5() throws Exception {
    String result = AdditionalInformationDatabaseUtility.getTableName(EntityCodeType.ClubCode.ID);
    assertEquals(RtClub.class.getSimpleName().toString().toLowerCase(), StringUtility.replace(result, "_", ""));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetPkColumn1() throws Exception {
    AdditionalInformationDatabaseUtility.getTableName(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetPkColumn2() throws Exception {
    AdditionalInformationDatabaseUtility.getTableName(-1L);
  }

  @Test
  public void testGetPkColumn3() throws Exception {
    String column = AdditionalInformationDatabaseUtility.getPkColumn(EntityCodeType.ClubCode.ID);
    assertNotNull(column);
  }

  @Test
  public void testGetPkColumn4() throws Exception {
    String column = AdditionalInformationDatabaseUtility.getPkColumn(EntityCodeType.EntryCode.ID);
    assertNotNull(column);
  }

  @Test
  public void testGetPkColumn5() throws Exception {
    String column = AdditionalInformationDatabaseUtility.getPkColumn(EntityCodeType.RunnerCode.ID);
    assertNotNull(column);
  }

  @Test
  public void testConvertToPrimaryKeyList1() throws Exception {
    Set<Long> result = AdditionalInformationDatabaseUtility.convertToPrimaryKeyList(null, 0);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testConvertToPrimaryKeyList2() throws Exception {
    Set<Long> result = AdditionalInformationDatabaseUtility.convertToPrimaryKeyList(new ArrayList<Object[]>(), 0);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testConvertToPrimaryKeyList3() throws Exception {
    List<Object[]> resultList = new ArrayList<Object[]>();
    resultList.add(new Object[]{});
    Set<Long> result = AdditionalInformationDatabaseUtility.convertToPrimaryKeyList(resultList, 0);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testConvertToPrimaryKeyList4() throws Exception {
    List<Object[]> resultList = new ArrayList<Object[]>();
    resultList.add(new Object[]{1L});
    Set<Long> result = AdditionalInformationDatabaseUtility.convertToPrimaryKeyList(resultList, 0);
    assertEquals(1, result.size());
  }

  @Before
  public void before() throws ProcessingException {
    AdditionalInformationDatabaseUtility.setDDLExecutor(testDDLexecutor);
  }

  @After
  public void after() throws ProcessingException {
    AdditionalInformationDatabaseUtility.setDDLExecutor(null);
  }

  public class TestDDLExecutor extends DDLExecutor {

    private String ddl;

    @Override
    protected void executeDDL(String ddlQuery) throws ProcessingException {
      this.ddl = ddlQuery;
    }

    public String getDDL() {
      return ddl;
    }
  }

}
