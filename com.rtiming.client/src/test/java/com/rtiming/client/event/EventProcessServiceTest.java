package com.rtiming.client.event;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.event.IEventProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class EventProcessServiceTest {

  private EventTestDataProvider event;

  @Test
  public void testUpload() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    BEANS.get(IEventProcessService.class).syncWithOnline(event.getEventNr());
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
  }

}
