package com.rtiming.client.ecard.download;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;

import com.rtiming.client.ClientSession;
import com.rtiming.client.entry.EntriesFinishTimesStoredTablePage;
import com.rtiming.client.entry.EntriesManualRaceStatusTablePage;
import com.rtiming.client.entry.EntriesMissingTablePage;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.ecard.download.PunchingSystemCodeType;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;

public class DownloadedECardsNodePage extends AbstractPageWithNodes {

  private boolean electronicPunchingSystem = true;

  @Override
  protected boolean getConfiguredExpanded() {
    return true;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("DownloadedECards");
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    if (electronicPunchingSystem) {
      pageList.add(new DownloadedECardsAllTablePage());
      pageList.add(new DownloadedECardsDuplicateTablePage());
      pageList.add(new DownloadedECardsMissingPunchTablePage());
      pageList.add(new DownloadedECardsEntryNotFoundTablePage());
    }
    pageList.add(new EntriesMissingTablePage(null, ClientSession.get().getSessionClientNr(), null, null, null, null));
    if (electronicPunchingSystem) {
      pageList.add(new EntriesManualRaceStatusTablePage(null, ClientSession.get().getSessionClientNr(), null, null, null, null));
    }
    if (!electronicPunchingSystem) {
      pageList.add(new EntriesFinishTimesStoredTablePage(null, ClientSession.get().getSessionClientNr(), null, null, null, null));
    }
  }

  @Override
  protected void execPageActivated() throws ProcessingException {
    initNodeTitle();
  }

  @Override
  protected void execInitPage() throws ProcessingException {
    initNodeTitle();
  }

  private void initNodeTitle() throws ProcessingException {
    boolean oldStatus = electronicPunchingSystem;
    Long defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    if (defaultEventNr != null) {
      EventBean event = new EventBean();
      event.setEventNr(defaultEventNr);
      event = BEANS.get(IEventProcessService.class).load(event);
      if (CompareUtility.equals(event.getPunchingSystemUid(), PunchingSystemCodeType.PunchingSystemNoneCode.ID)) {
        electronicPunchingSystem = false;
      }
      else {
        electronicPunchingSystem = true;
      }
    }
    if (electronicPunchingSystem) {
      getCellForUpdate().setText(getConfiguredTitle());
    }
    else {
      getCellForUpdate().setText("Zielzeiten");
    }
    if (oldStatus != electronicPunchingSystem) {
      loadChildren();
    }
  }
}
