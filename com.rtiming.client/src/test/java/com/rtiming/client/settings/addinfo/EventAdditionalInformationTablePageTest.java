package com.rtiming.client.settings.addinfo;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.event.IEventProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class EventAdditionalInformationTablePageTest extends AbstractTablePageTest<EventAdditionalInformationTablePage> {

  private EventTestDataProvider event;

  @Before
  public void before() throws ProcessingException {
    event = new EventTestDataProvider();
  }

  @Override
  protected EventAdditionalInformationTablePage getTablePage() throws ProcessingException {
    return new EventAdditionalInformationTablePage(event.getEventNr());
  }

  @After
  public void after() throws ProcessingException {
    BEANS.get(IEventProcessService.class).delete(event.getEventNr(), true);
  }

}
