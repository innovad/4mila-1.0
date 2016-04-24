package com.rtiming.client.entry;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class EntriesFinishTimesStoredTablePageTest extends AbstractTablePageTest<EntriesFinishTimesStoredTablePage> {

  private EventWithIndividualClassTestDataProvider event;

  @Before
  public void before() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

  @Override
  protected EntriesFinishTimesStoredTablePage getTablePage() throws ProcessingException {
    return new EntriesFinishTimesStoredTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, event.getClassUid(), event.getCourseNr(), null);
  }

}
