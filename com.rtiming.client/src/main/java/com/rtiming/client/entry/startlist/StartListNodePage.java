package com.rtiming.client.entry.startlist;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ClientSession;
import com.rtiming.client.entry.EntriesTablePage;
import com.rtiming.client.entry.startblock.EventStartblocksTablePage;

public class StartListNodePage extends AbstractPageWithNodes {

  private final Long eventNr;

  public StartListNodePage(Long eventNr) {
    super();
    this.eventNr = eventNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("StartList");
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new EntriesTablePage(getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null));
    pageList.add(new StartlistSettingsTablePage(getEventNr()));
    pageList.add(new EventStartblocksTablePage(getEventNr()));
    pageList.add(new VacantsTablePage(getEventNr()));
  }

  public Long getEventNr() {
    return eventNr;
  }

}
