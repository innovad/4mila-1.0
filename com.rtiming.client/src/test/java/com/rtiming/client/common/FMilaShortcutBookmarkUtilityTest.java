package com.rtiming.client.common;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ecard.download.DownloadedECardsAllTablePage;
import com.rtiming.client.entry.EntriesTablePage;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class FMilaShortcutBookmarkUtilityTest {

  @Test
  public void testActivateDownloadedECardsTablePage() throws Exception {
    FMilaShortcutBookmarkUtility.activateDownloadedECardsTablePage();
    IPage currentPage = IDesktop.CURRENT.get().getOutline().getActivePage();
    assertNotNull("page activated", currentPage);
    assertTrue("correct page", currentPage instanceof DownloadedECardsAllTablePage);
  }

  @Test
  public void testActivateEntriesTablePage() throws Exception {
    FMilaShortcutBookmarkUtility.activateEntriesTablePage();
    IPage currentPage = IDesktop.CURRENT.get().getOutline().getActivePage();
    assertNotNull("page activated", currentPage);
    assertTrue("correct page", currentPage instanceof EntriesTablePage);
  }

}
