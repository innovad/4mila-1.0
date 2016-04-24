package com.rtiming.client.settings.clazz;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.settings.clazz.ClazzLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class ClazzLookupCallTest extends AbstractDefaultLookupCallTest {

  private EventWithIndividualClassTestDataProvider event;

  @Override
  protected LookupCall createLookupCall() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    ClazzLookupCall lookupCall = new ClazzLookupCall();
    lookupCall.setEventNr(event.getEventNr());
    return lookupCall;
  }

  @Override
  public void deleteTestRow() throws ProcessingException {
    event.remove();
  }

}
