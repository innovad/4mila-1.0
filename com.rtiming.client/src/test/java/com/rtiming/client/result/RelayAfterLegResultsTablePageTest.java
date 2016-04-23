package com.rtiming.client.result;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.EventWithRelayValidatedRaceTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RelayAfterLegResultsTablePageTest extends AbstractTablePageTest<RelayAfterLegResultsTablePage> {

  private EventWithRelayValidatedRaceTestDataProvider relay;

  @Override
  protected RelayAfterLegResultsTablePage getTablePage() throws ProcessingException {
    String[][] controlNos = new String[][]{new String[]{"31"}, new String[]{"32"}};
    String[][] punchNos = new String[][]{new String[]{"31"}, new String[]{"32"}};
    Integer[] start = new Integer[]{10000, 20000};
    Integer[] finish = new Integer[]{20000, 30000};
    Integer[][] legTimes = new Integer[][]{new Integer[]{12000, 14000}, new Integer[]{24000, 26000}};
    relay = new EventWithRelayValidatedRaceTestDataProvider(controlNos, punchNos, start, finish, legTimes);
    RelayAfterLegResultsTablePage page = new RelayAfterLegResultsTablePage(relay.getEventNr(), relay.getParentUid(), relay.getLegUid(1));
    return page;
  }

  @After
  public void after() throws ProcessingException {
    if (relay != null) {
      relay.remove();
    }
  }

}
