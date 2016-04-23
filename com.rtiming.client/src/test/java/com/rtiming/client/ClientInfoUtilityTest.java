package com.rtiming.client;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class ClientInfoUtilityTest {

  @Test
  public void testBuildInstallationInfoNull() throws Exception {
    ClientInfoUtility.buildInstallationInfo(null);
  }

  @Test
  public void testBuildInstallationInfoEmpty() throws Exception {
    Map<String, String> users = new HashMap<String, String>();
    ClientInfoUtility.buildInstallationInfo(users);
  }

  @Test
  public void testBuildInstallationInfo() throws Exception {
    Map<String, String> users = new HashMap<String, String>();
    users.put("DUMMY1", "DUMMY2");
    String result = ClientInfoUtility.buildInstallationInfo(users);
    Assert.assertTrue("Username must exist", result.contains("DUMMY1"));
    Assert.assertTrue("Password must exist", result.contains("DUMMY2"));
  }

}
