package com.rtiming.server;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class ServerInfoUtilityTest {

  @Test
  public void testBuildInstallationInfoNull() throws Exception {
    ServerInfoUtility.buildInstallationInfo(null);
  }

  @Test
  public void testBuildInstallationInfoEmpty() throws Exception {
    Map<String, String> users = new HashMap<String, String>();
    ServerInfoUtility.buildInstallationInfo(users);
  }

  @Test
  public void testBuildInstallationInfo() throws Exception {
    Map<String, String> users = new HashMap<String, String>();
    users.put("DUMMY1", "DUMMY2");
    String result = ServerInfoUtility.buildInstallationInfo(users);
    Assert.assertTrue("Username must exist", result.contains("DUMMY1"));
    Assert.assertTrue("Password must exist", result.contains("DUMMY2"));
  }

}
