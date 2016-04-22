package com.rtiming.client.common.report;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.scout.rt.client.ui.basic.table.ColumnSet;
import org.eclipse.scout.rt.client.ui.basic.table.IHeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IStringColumn;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author amo
 */
public class DataSourceUtilityTest {

  @Test
  public void testParameters1() throws Exception {
    ITable table = createMockTable();
    Map<String, Object> map = DataSourceUtility.createColumnHeadersParametersFromTable(table);
    Assert.assertEquals("0 items", 0, map.size());
  }

  @Test
  public void testParameters2() throws Exception {
    IColumn<?> col = createStringMockColumn("id", "text", true);
    ITable table = createMockTable(col);
    Map<String, Object> map = DataSourceUtility.createColumnHeadersParametersFromTable(table);
    Assert.assertEquals("1 items", 1, map.size());
    Entry<String, Object> item = map.entrySet().iterator().next();
    Assert.assertEquals("ColumnHeader.id", item.getKey());
    Assert.assertEquals("text", item.getValue());
  }

  @Test
  public void testParameters3() throws Exception {
    ITable table = createMockTable(createStringMockColumn("id1", "text", true), createStringMockColumn("id2", "text", true));
    Map<String, Object> map = DataSourceUtility.createColumnHeadersParametersFromTable(table);
    Assert.assertEquals("2 items", 2, map.size());
  }

  @Test
  public void testParameters4() throws Exception {
    ITable table = createMockTable(createStringMockColumn("id1", "text", false), createStringMockColumn("id2", "text", true));
    Map<String, Object> map = DataSourceUtility.createColumnHeadersParametersFromTable(table);
    Assert.assertEquals("1 items", 1, map.size());
  }

  @Test
  public void testCollection1() throws Exception {
    IStringColumn col1 = createStringMockColumn("id1", "text", false, "ABC", "ABC");
    ITable table = createMockTable(col1);
    Collection<Map<String, ?>> result = DataSourceUtility.createCollectionFromTable(table, false, false);
    Map<String, ?> item = result.iterator().next();
    Assert.assertEquals("ABC", item.get("id1"));
  }

  @Test
  public void testCollection2() throws Exception {
    IStringColumn col1 = createStringMockColumn("id1", "text", false, "ABC", "ABC");
    ITable table = createMockTable(col1);
    Collection<Map<String, ?>> result = DataSourceUtility.createCollectionFromTable(table, false, true);
    Map<String, ?> item = result.iterator().next();
    Assert.assertEquals("ABC", item.get("Detail.id1"));
  }

  @Test
  public void testCollection3() throws Exception {
    IStringColumn col1 = createStringMockColumn("id1", "text", true, "ABC", "ABC");
    ITable table = createMockTable(col1);
    Collection<Map<String, ?>> result = DataSourceUtility.createCollectionFromTable(table, true, true);
    Map<String, ?> item = result.iterator().next();
    Assert.assertEquals("ABC", item.get("Detail.id1"));
  }

  @Test
  public void testCollection4() throws Exception {
    IStringColumn col1 = createStringMockColumn("id1", "text", true, null, null);
    ITable table = createMockTable(col1);
    Collection<Map<String, ?>> result = DataSourceUtility.createCollectionFromTable(table, true, true);
    Map<String, ?> item = result.iterator().next();
    Assert.assertEquals("", item.get("Detail.id1"));
  }

  @Test
  public void testCollection5() throws Exception {
    IStringColumn col1 = createStringMockColumn("id1", "text", true, null, null);
    ITable table = createMockTable(col1);
    Collection<Map<String, ?>> result = DataSourceUtility.createCollectionFromTable(table, true, true);
    Map<String, ?> item = result.iterator().next();
    Assert.assertEquals("", item.get("Detail.id1"));
  }

  @Test
  public void testCollection6() throws Exception {
    IStringColumn col1 = createStringMockColumn("id1", "text", true, null, "Jonathan");
    ITable table = createMockTable(col1);
    Collection<Map<String, ?>> result = DataSourceUtility.createCollectionFromTable(table, true, true);
    Map<String, ?> item = result.iterator().next();
    Assert.assertEquals("Jonathan", item.get("Detail.id1"));
  }

  @Test
  public void testCollectionBoolean1() throws Exception {
    IBooleanColumn col1 = createBooleanMockColumn("id1", "text", true, Boolean.TRUE);
    ITable table = createMockTable(col1);
    Collection<Map<String, ?>> result = DataSourceUtility.createCollectionFromTable(table, true, true);
    Map<String, ?> item = result.iterator().next();
    Assert.assertEquals("X", item.get("Detail.id1"));
  }

  @Test
  public void testCollectionBoolean2() throws Exception {
    IBooleanColumn col1 = createBooleanMockColumn("id1", "text", true, Boolean.FALSE);
    ITable table = createMockTable(col1);
    Collection<Map<String, ?>> result = DataSourceUtility.createCollectionFromTable(table, true, true);
    Map<String, ?> item = result.iterator().next();
    Assert.assertEquals("", item.get("Detail.id1"));
  }

  @Test
  public void testCollectionBoolean3() throws Exception {
    IBooleanColumn col1 = createBooleanMockColumn("id1", "text", true, null);
    ITable table = createMockTable(col1);
    Collection<Map<String, ?>> result = DataSourceUtility.createCollectionFromTable(table, true, true);
    Map<String, ?> item = result.iterator().next();
    Assert.assertEquals("", item.get("Detail.id1"));
  }

  private IStringColumn createStringMockColumn(String id, String text, boolean displayable) {
    IStringColumn col = Mockito.mock(IStringColumn.class);
    Mockito.when(col.isDisplayable()).thenReturn(displayable);
    Mockito.when(col.getColumnId()).thenReturn(id);
    IHeaderCell headerCell = Mockito.mock(IHeaderCell.class);
    Mockito.when(headerCell.getText()).thenReturn(text);
    Mockito.when(col.getHeaderCell()).thenReturn(headerCell);
    return col;
  }

  private IBooleanColumn createBooleanMockColumn(String id, String text, boolean displayable) {
    IBooleanColumn col = Mockito.mock(IBooleanColumn.class);
    Mockito.when(col.isDisplayable()).thenReturn(displayable);
    Mockito.when(col.getColumnId()).thenReturn(id);
    IHeaderCell headerCell = Mockito.mock(IHeaderCell.class);
    Mockito.when(headerCell.getText()).thenReturn(text);
    Mockito.when(col.getHeaderCell()).thenReturn(headerCell);
    return col;
  }

  private IStringColumn createStringMockColumn(String id, String text, boolean displayable, String displayValue, String value) {
    IStringColumn column = createStringMockColumn(id, text, displayable);
    Mockito.when(column.getDisplayText(Mockito.any(ITableRow.class))).thenReturn(displayValue);
    Mockito.when(column.getValue(Mockito.any(ITableRow.class))).thenReturn(value);
    return column;
  }

  private IBooleanColumn createBooleanMockColumn(String id, String text, boolean displayable, Boolean value) {
    IBooleanColumn column = createBooleanMockColumn(id, text, displayable);
    Mockito.when(column.getDisplayText(Mockito.any(ITableRow.class))).thenReturn(null);
    Mockito.when(column.getValue(Mockito.any(ITableRow.class))).thenReturn(value);
    return column;
  }

  private ITable createMockTable(IColumn<?>... cols) {
    ITable table = Mockito.mock(ITable.class);
    Mockito.when(table.getColumns()).thenReturn(Arrays.asList(cols));
    ColumnSet columnSet = Mockito.mock(ColumnSet.class);
    Mockito.when(columnSet.getVisibleColumns()).thenReturn(Arrays.asList(cols));
    Mockito.when(table.getColumnSet()).thenReturn(columnSet);
    Mockito.when(table.getRowCount()).thenReturn(1);
    Mockito.when(table.getRow(Mockito.anyInt())).thenReturn(Mockito.mock(ITableRow.class));
    return table;
  }

}
