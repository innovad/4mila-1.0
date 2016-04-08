package com.rtiming.client.common;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.bookmark.AbstractPageState;
import org.eclipse.scout.rt.shared.services.common.bookmark.Bookmark;
import org.eclipse.scout.rt.shared.services.common.bookmark.NodePageState;
import org.eclipse.scout.rt.shared.services.common.bookmark.TablePageState;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ecard.download.DownloadedECardsAllTablePage;
import com.rtiming.client.ecard.download.DownloadedECardsNodePage;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm;
import com.rtiming.client.ecard.download.job.StatusUpdaterJob;
import com.rtiming.client.entry.EntriesNodePage;
import com.rtiming.client.entry.EntriesSearchForm;
import com.rtiming.client.entry.EntriesTablePage;
import com.rtiming.client.entry.RegistrationOutline;
import com.rtiming.client.result.ResultsOutline;

public class FMilaShortcutBookmarkUtility {

  private FMilaShortcutBookmarkUtility() {
  }

  public static void activateDownloadedECardsTablePage() throws ProcessingException {
    Bookmark bookmark = new Bookmark();
    bookmark.setOutlineClassName(ResultsOutline.class.getName());
    AbstractPageState root = getRootPageState();
    bookmark.addPathElement(root);
    NodePageState node = new NodePageState();
    node.setPageClassName(DownloadedECardsNodePage.class.getName());
    node.setExpanded(true);
    bookmark.addPathElement(node);
    TablePageState table = new TablePageState();
    table.setPageClassName(DownloadedECardsAllTablePage.class.getName());
    table.setExpanded(true);
    bookmark.addPathElement(table);
    IDesktop desktop = ClientSession.get().getDesktop();
    desktop.activateBookmark(bookmark, false);
    if (desktop.getOutline().getActivePage() instanceof DownloadedECardsAllTablePage) {
      DownloadedECardsAllTablePage page = (DownloadedECardsAllTablePage) desktop.getOutline().getActivePage();
      ((DownloadedECardsSearchForm) page.getSearchFormInternal()).getResetButton().doClick();
      page.reloadPage();
      page.getTable().selectFirstRow();
      StatusUpdaterJob.setText(TEXTS.get("DownloadedECards"), ClientSession.get());
    }
    else {
      throw new ProcessingException("Failed loading downloaded e-cards page.");
    }
  }

  public static void activateEntriesTablePage() throws ProcessingException {
    Bookmark bookmark = new Bookmark();
    bookmark.setOutlineClassName(RegistrationOutline.class.getName());
    AbstractPageState root = getRootPageState();
    bookmark.addPathElement(root);
    NodePageState node = new NodePageState();
    node.setPageClassName(EntriesNodePage.class.getName());
    node.setExpanded(true);
    bookmark.addPathElement(node);
    TablePageState table = new TablePageState();
    table.setPageClassName(EntriesTablePage.class.getName());
    table.setExpanded(true);
    bookmark.addPathElement(table);
    IDesktop desktop = ClientSession.get().getDesktop();
    desktop.activateBookmark(bookmark, false);
    if (desktop.getOutline().getActivePage() instanceof EntriesTablePage) {
      EntriesTablePage page = (EntriesTablePage) desktop.getOutline().getActivePage();
      ((EntriesSearchForm) page.getSearchFormInternal()).getResetButton().doClick();
      page.reloadPage();
      StatusUpdaterJob.setText(TEXTS.get("Entries"), ClientSession.get());
    }
    else {
      throw new ProcessingException("Failed loading entries page.");
    }
  }

  private static AbstractPageState getRootPageState() {
    AbstractPageState root = new NodePageState();
    root.setPageClassName(AbstractOutline.class.getName() + "." + "InvisibleRootPage");
    return root;
  }

}
