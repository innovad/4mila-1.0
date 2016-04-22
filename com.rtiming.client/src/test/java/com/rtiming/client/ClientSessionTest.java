package com.rtiming.client;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.IOutline;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.common.ui.desktop.Desktop;
import com.rtiming.client.entry.RegistrationOutline;
import com.rtiming.client.event.EventsOutline;
import com.rtiming.client.result.ResultsOutline;
import com.rtiming.client.settings.SettingsOutline;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class ClientSessionTest {
  private ClientSession m_clientSession;

  @Before
  public void before() throws Exception {
    m_clientSession = ClientSession.get();
  }

  @Test
  public void testVisibleOutlinesAdminUser() throws Exception {
    IDesktop desktop = m_clientSession.getDesktop();
    Assert.assertNotNull(desktop);
    Assert.assertTrue(desktop instanceof Desktop);
    assertVisible(desktop, EventsOutline.class, true);
    assertVisible(desktop, RegistrationOutline.class, true);
    assertVisible(desktop, ResultsOutline.class, true);
    assertVisible(desktop, SettingsOutline.class, true);
  }

  private void assertVisible(IDesktop desktop, Class<? extends IOutline> outlineClass, boolean visible) {
    IOutline outline = null;
    for (IOutline o : desktop.getAvailableOutlines()) {
      if (outlineClass.isInstance(o)) {
        outline = o;
        break;
      }
    }
    Assert.assertNotNull(outline);
    Assert.assertTrue(outline.isVisible());
  }
}
