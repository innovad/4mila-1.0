package com.rtiming.client.result.split;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class SplitTimesTablePageTest extends AbstractTablePageTest<SplitTimesTablePage> {

  private EventWithIndividualValidatedRaceTestDataProvider event;

  @Override
  protected SplitTimesTablePage getTablePage() throws ProcessingException {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    SplitTimesTablePage splitTimesTablePage = new SplitTimesTablePage(ClientSession.get().getSessionClientNr(), event.getClassUid(), null, null);
    ((SingleEventSearchForm) splitTimesTablePage.getSearchFormInternal()).getEventField().setValue(event.getEventNr());
    return splitTimesTablePage;
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

}
