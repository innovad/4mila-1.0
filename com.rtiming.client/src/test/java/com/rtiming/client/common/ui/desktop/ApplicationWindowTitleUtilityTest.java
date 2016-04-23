package com.rtiming.client.common.ui.desktop;

import java.util.Date;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.Texts;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class ApplicationWindowTitleUtilityTest {

  @Test
  public void testNoBackup() throws Exception {
    String title = ApplicationWindowTitleUtility.getTitle("Title", null);
    Assert.assertTrue("No Backup", title.contains(Texts.get("NoBackup")));
  }

  @Test
  public void testBackup() throws Exception {
    String title = ApplicationWindowTitleUtility.getTitle("Title", new Date());
    Assert.assertFalse("No Backup", title.contains(Texts.get("NoBackup")));
  }

}
