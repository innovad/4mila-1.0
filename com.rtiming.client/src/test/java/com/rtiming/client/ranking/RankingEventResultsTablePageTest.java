package com.rtiming.client.ranking;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.client.test.data.RankingEventTestDataProvider;
import com.rtiming.client.test.data.RankingTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class RankingEventResultsTablePageTest extends AbstractTablePageTest<RankingEventResultsTablePage> {

  private EventWithIndividualValidatedRaceTestDataProvider event;
  private RankingTestDataProvider ranking;
  private RankingEventTestDataProvider rankingEvent;

  @Override
  protected RankingEventResultsTablePage getTablePage() throws ProcessingException {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    ranking = new RankingTestDataProvider();
    rankingEvent = new RankingEventTestDataProvider(ranking.getRankingNr(), event.getEventNr());
    return new RankingEventResultsTablePage(ranking.getRankingNr(), event.getEventNr(), event.getClassUid());
  }

  @After
  public void after() throws ProcessingException {
    rankingEvent.remove();
    ranking.remove();
    event.remove();
  }

}
