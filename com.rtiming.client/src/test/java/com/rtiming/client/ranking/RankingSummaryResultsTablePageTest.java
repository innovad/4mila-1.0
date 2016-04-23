package com.rtiming.client.ranking;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.client.test.data.RankingEventTestDataProvider;
import com.rtiming.client.test.data.RankingTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RankingSummaryResultsTablePageTest extends AbstractTablePageTest<RankingSummaryResultsTablePage> {

  private EventWithIndividualValidatedRaceTestDataProvider event;
  private RankingTestDataProvider ranking;
  private RankingEventTestDataProvider rankingEvent;

  @Override
  protected RankingSummaryResultsTablePage getTablePage() throws ProcessingException {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    ranking = new RankingTestDataProvider();
    rankingEvent = new RankingEventTestDataProvider(ranking.getRankingNr(), event.getEventNr());
    return new RankingSummaryResultsTablePage(ranking.getRankingNr(), event.getEventNr(), event.getClassUid());
  }

  @After
  public void after() throws ProcessingException {
    rankingEvent.remove();
    ranking.remove();
    event.remove();
  }

}
