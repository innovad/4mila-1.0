package com.rtiming.server.common.web;

import static org.junit.Assert.assertFalse;

import java.util.List;

import javax.persistence.Table;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com._4mila._4mila.jaxws.online.ObjectList;
import com._4mila._4mila.jaxws.online.RowData;
import com._4mila._4mila.jaxws.online.StringList;
import com._4mila._4mila.jaxws.online.TableData;
import com.rtiming.server.ServerSession;
import com.rtiming.shared.dao.RtEvent;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class UploadUtilityTest {

  @Test
  public void testCreateUploadTable() throws Exception {
    UploadUtility.createUploadTable(RtEvent.class.getAnnotation(Table.class).name(), new String[]{}, 0L, false);
  }

  @Test
  public void testDeleteUploadTable() throws Exception {
    TableData tableData = new TableData();
    tableData.setTableName(RtEvent.class.getAnnotation(Table.class).name());
    RowData value = new RowData();
    ObjectList list = new ObjectList();
    list.getObjects().add(0L);
    list.getObjects().add(0L);
    list.getObjects().add("gif");
    value.getRows().add(list);
    tableData.setData(value);
    tableData.setForceCleanup(true);
    StringList stringList = new StringList();
    stringList.getStrings().add("EVENT_NR");
    stringList.getStrings().add("CLIENT_NR");
    stringList.getStrings().add("FORMAT");
    tableData.setColumns(stringList);
    UploadUtility.deleteTable(tableData, Long.MAX_VALUE, 0L);
  }

  @Test
  public void testGetColumnDefinitions1() throws Exception {
    List<ColumnDefinition> result = UploadUtility.getColumnDefinitions(RtEvent.class.getAnnotation(Table.class).name(), null);
    assertFalse(result.isEmpty());
  }

  @Test
  public void testGetPrimaryKeys() throws Exception {
    List<String> result = UploadUtility.getPrimaryKeys(RtEvent.class.getAnnotation(Table.class).name());
    assertFalse(result.isEmpty());
  }

}
