package com.rtiming.client.ranking;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.data.RankingEventTestDataProvider;
import com.rtiming.client.test.data.RankingTestDataProvider;
import com.rtiming.shared.ranking.AbstractFormulaCode.RankingType;

/**
 * @author amo
 */
@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RankingClassesTablePageTest extends AbstractTablePageTest<RankingClassesTablePage> {

  private static RankingTestDataProvider ranking;
  private static RankingEventTestDataProvider rankingEvent;
  private static EventTestDataProvider event;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    ranking = new RankingTestDataProvider();
    event = new EventTestDataProvider();
    rankingEvent = new RankingEventTestDataProvider(ranking.getRankingNr(), event.getEventNr());
  }

  @Override
  protected RankingClassesTablePage getTablePage() throws ProcessingException {
    return new RankingClassesTablePage(ranking.getRankingNr(), event.getEventNr(), RankingType.EVENT);
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    rankingEvent.remove();
    ranking.remove();
    event.remove();
  }

}
