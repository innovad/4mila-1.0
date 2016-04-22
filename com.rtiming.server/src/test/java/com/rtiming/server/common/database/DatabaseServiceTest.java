package com.rtiming.server.common.database;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.common.database.IDatabaseService;
import com.rtiming.shared.common.database.TableInfoBean;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class DatabaseServiceTest {

  @Test
  public void testLastBackup() throws Exception {
    BEANS.get(IDatabaseService.class).getLastBackup();
  }

  @Test
  public void testGetTables() throws Exception {
    DatabaseService svc = new DatabaseService();
    List<String> tables = svc.getTables();
    assertTrue(tables.size() > 40);
  }

  @Test
  public void testGetColumns() throws Exception {
    DatabaseService svc = new DatabaseService();
    List<String> tables = svc.getTables();
    for (String table : tables) {
      TableInfoBean bean = svc.getColumns(table);
      assertTrue(bean.getColumns().length > 0);
    }
  }

  @Test
  public void testGetDataDirectory() throws Exception {
    DatabaseService svc = new DatabaseService();
    String result = svc.getDataDirectory();
    assertNotNull(result);
  }

}
