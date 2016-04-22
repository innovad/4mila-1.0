package com.rtiming.server.common.database;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class ServerBackupUtilityTest {

  @Test
  public void testBackup() throws Exception {
    IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);
    service.setBackupDirectory("C:\\TEMP");

    ServerBackupUtility.backup();
  }

  @After
  public void after() throws ProcessingException {
    // disable automatic backup
    BEANS.get(IDefaultProcessService.class).setBackupInterval(null);
  }

}
