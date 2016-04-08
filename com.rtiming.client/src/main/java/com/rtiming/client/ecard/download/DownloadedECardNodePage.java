package com.rtiming.client.ecard.download;

import java.util.List;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.AbstractIcons;

import com.rtiming.client.race.RaceControlsTablePage;
import com.rtiming.shared.Texts;

public class DownloadedECardNodePage extends AbstractPageWithNodes {

  private final Long m_punchSessionNr;
  private final Long m_raceNr;

  public DownloadedECardNodePage(Long punchSessionNr, Long raceNr) {
    super();
    m_punchSessionNr = punchSessionNr;
    m_raceNr = raceNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("DownloadedECard");
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new PunchesTablePage(getPunchSessionNr()));
    if (getRaceNr() != null) {
      pageList.add(new RaceControlsTablePage(getRaceNr()));
    }
  }

  @FormData
  public Long getPunchSessionNr() {
    return m_punchSessionNr;
  }

  @FormData
  public Long getRaceNr() {
    return m_raceNr;
  }
}
