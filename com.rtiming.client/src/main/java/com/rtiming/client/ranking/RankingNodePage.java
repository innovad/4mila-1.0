package com.rtiming.client.ranking;

import java.util.List;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.shared.ranking.AbstractFormulaCode.RankingType;

public class RankingNodePage extends AbstractPageWithNodes {

  private final Long m_rankingNr;
  private final Long m_eventNr;

  public RankingNodePage(Long rankingNr, Long eventNr) {
    super();
    m_rankingNr = rankingNr;
    m_eventNr = eventNr;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Ranking");
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new RankingClassesTablePage(m_rankingNr, m_eventNr, RankingType.EVENT));
    pageList.add(new RankingClassesTablePage(m_rankingNr, m_eventNr, RankingType.SUMMARY));
  }

  @FormData
  public Long getRankingNr() {
    return m_rankingNr;
  }

}
