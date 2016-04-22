package com.rtiming.server.common.security;

import java.security.Permissions;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class AccessControlServiceTest {

  @Test
  public void testService() throws Exception {
    AccessControlService service = new AccessControlService();
    Permissions p = service.execLoadPermissions("");
    Assert.assertTrue(p.elements().hasMoreElements());
  }

}
