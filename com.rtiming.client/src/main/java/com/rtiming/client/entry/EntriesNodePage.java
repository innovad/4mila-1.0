package com.rtiming.client.entry;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.AbstractIcons;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.Texts;

public class EntriesNodePage extends AbstractPageWithNodes {

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
    return Texts.get("StartLists");
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new EntriesTablePage(null, ClientSession.get().getSessionClientNr(), null, null, null, null));
    pageList.add(new EntriesClassesTablePage());
    pageList.add(new EntriesCoursesTablePage());
    pageList.add(new EntriesClubsTablePage());
  }
}
