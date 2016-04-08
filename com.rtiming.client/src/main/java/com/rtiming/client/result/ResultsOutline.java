package com.rtiming.client.result;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;

import com.rtiming.client.ecard.download.DownloadedECardsNodePage;
import com.rtiming.client.ecard.download.ECardStationsTablePage;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.ResultsOutlinePermission;

public class ResultsOutline extends AbstractOutline {

  @Override
  protected String getConfiguredIconId() {
    return Icons.TIMEFIELDTIME;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Results");
  }

  @Override
  protected void execInitTree() throws ProcessingException {
    setVisiblePermission(new ResultsOutlinePermission());
  }

  @Override
  protected void initConfig() {
    super.initConfig();
    setVisible(FMilaUtility.isRichClient());
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new ResultsNodePage());
    pageList.add(new DownloadedECardsNodePage());
    pageList.add(new ECardStationsTablePage());
  }
}
