package com.rtiming.client.entry.startblock;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.data.StartblockTestDataProvider;
import com.rtiming.shared.entry.startblock.StartblockLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class StartblockLookupCallTest extends AbstractDefaultLookupCallTest {

  private EventTestDataProvider event;
  private StartblockTestDataProvider startblock;

  @Override
  protected LookupCall createLookupCall() throws ProcessingException {
    event = new EventTestDataProvider();
    startblock = new StartblockTestDataProvider();

    EventStartblockForm form = new EventStartblockForm();
    form.setEventNr(event.getEventNr());
    form.startNew();
    FormTestUtility.fillFormFields(form);
    form.getStartblockUidField().setValue(startblock.getStartblockUid());
    form.doOk();

    StartblockLookupCall lookupCall = new StartblockLookupCall();
    lookupCall.setEventNr(event.getEventNr());
    return lookupCall;
  }

  @Override
  public void deleteTestRow() throws ProcessingException {
    event.remove();
    startblock.remove();
  }

}
