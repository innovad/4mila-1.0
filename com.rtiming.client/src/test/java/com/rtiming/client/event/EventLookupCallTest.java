package com.rtiming.client.event;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractSimpleDatabaseLookupServiceTest;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.event.EventLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class EventLookupCallTest extends AbstractSimpleDatabaseLookupServiceTest {

  private EventTestDataProvider event;

  @Override
  protected void init() throws ProcessingException {
    event = new EventTestDataProvider();
    setLookupCall(new EventLookupCall());
    setText(event.getForm().getNameField().getValue());
    setKey(event.getEventNr());
  }

  @Override
  protected void insertTestRow() {
  }

  @Override
  protected void deleteTestRow() throws ProcessingException {
    event.remove();
  }

}
