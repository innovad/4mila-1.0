package com.rtiming.client.common.database;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;

public class DatabaseNodePage extends AbstractPageWithNodes {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Database");
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new BackupsTablePage());
    pageList.add(new DatabaseInfoTablePage());
    pageList.add(new TablesTablePage());
  }
}
