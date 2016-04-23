package com.rtiming.client.ranking;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.RankingTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RankingEventsTablePageTest extends AbstractTablePageTest<RankingEventsTablePage> {

  private RankingTestDataProvider ranking;

  @Override
  protected RankingEventsTablePage getTablePage() throws ProcessingException {
    ranking = new RankingTestDataProvider();
    return new RankingEventsTablePage(ranking.getRankingNr());
  }

  @After
  public void after() throws ProcessingException {
    if (ranking != null) {
      ranking.remove();
    }
  }

}
