package com.rtiming.shared;

import org.junit.Assert;
import org.junit.Test;

public class SharedInfoUtilityTest {

  @Test
  public void testBuildInstallationInfo() throws Exception {
    String result = SharedInfoUtility.buildInstallationInfo("DUMMY1");
    Assert.assertTrue("Caller exists", result.contains("DUMMY1"));
  }

}
