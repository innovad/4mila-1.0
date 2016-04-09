package com.rtiming.client.result;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ranking.RankingEventResultsTablePage;
import com.rtiming.client.ranking.RankingEventsTablePage;
import com.rtiming.client.ranking.RankingSummaryResultsTablePage;
import com.rtiming.client.ranking.RankingsTablePage;
import com.rtiming.client.result.split.SplitTimesTablePage;
import com.rtiming.shared.event.course.ClassTypeCodeType;

public class ResultTypeNodePage extends AbstractPageWithNodes {

  private final Long m_clientNr;
  private final Long m_eventNr;
  private final Long m_parentUid;
  private final Long m_classUid;
  private final Long m_clubNr;
  private final Long m_courseNr;
  private final Long m_classTypeUid;

  public ResultTypeNodePage(Long clientNr, Long eventNr, Long parentUid, Long classUid, Long classTypeUid, Long courseNr, Long clubNr) {
    super();
    m_eventNr = eventNr;
    m_parentUid = parentUid;
    m_clientNr = clientNr;
    m_classUid = classUid;
    m_classTypeUid = classTypeUid;
    m_courseNr = courseNr;
    m_clubNr = clubNr;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Results");
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    // relay leg summary results
    if (ClassTypeCodeType.isRelayType(m_classTypeUid)) {
      pageList.add(new RelayAfterLegResultsTablePage(m_eventNr, m_parentUid, m_classUid));
    }
    // standard results for class or course
    if (ClassTypeCodeType.isLegClassType(m_classTypeUid) || m_courseNr != null) {
      pageList.add(new ResultsTablePage(m_clientNr, m_classUid, m_classTypeUid, m_courseNr, m_clubNr));
      pageList.add(new SplitTimesTablePage(m_clientNr, m_classUid, m_courseNr, m_clubNr));

      if (m_classUid != null) {
        RankingsTablePage rankings = new RankingsTablePage();
        rankings.nodeAddedNotify();
        rankings.loadChildren();
        for (Long rankingNr : rankings.getTable().getRankingNrColumn().getValues()) {
          RankingEventsTablePage page = new RankingEventsTablePage(rankingNr);
          page.nodeAddedNotify();
          page.loadChildren();
          for (Long rankingEventNr : page.getTable().getEventNrColumn().getValues()) {
            if (CompareUtility.equals(m_eventNr, rankingEventNr)) {
              pageList.add(new RankingEventResultsTablePage(rankingNr, m_eventNr, m_classUid));
              if (page.getTable().getEventNrColumn().getValues().size() > 1) {
                // only add summary if there is more than 1 event
                pageList.add(new RankingSummaryResultsTablePage(rankingNr, m_eventNr, m_classUid));
              }
            }
          }
        }
      }
    }
  }

}
