package com.rtiming.client.result;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.AbstractIcons;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.Texts;

public class ResultsNodePage extends AbstractPageWithNodes {

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
    return Texts.get("Results");
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new ResultsClassesTablePage(ClientSession.get().getSessionClientNr()));
    pageList.add(new ResultsCoursesTablePage());
    pageList.add(new ResultsClubsTablePage());
  }
}
