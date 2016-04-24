package com.rtiming.client.race;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class RaceControlTablePageNegativeTest {

  private EventWithIndividualValidatedRaceTestDataProvider event;

  @Test
  public void testNegativeTimeFormatting() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"33"}, new String[]{"32"}, 0, 1000, new Integer[]{-500});

    RaceControlsTablePage page = new RaceControlsTablePage(event.getRaceNr());
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("4 rows", 4, page.getTable().getRowCount());
    Assert.assertEquals("Formatted Negative Time", "", page.getTable().getRelativeTimeColumn().getValue(3));
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
  }

}
