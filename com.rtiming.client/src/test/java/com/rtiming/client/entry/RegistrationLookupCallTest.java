package com.rtiming.client.entry;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.data.RegistrationTestDataProvider;
import com.rtiming.shared.entry.RegistrationLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class RegistrationLookupCallTest extends AbstractDefaultLookupCallTest {

  private RegistrationTestDataProvider registration;

  @Override
  protected void init() throws ProcessingException {
    registration = new RegistrationTestDataProvider();
    setLookupCall(new RegistrationLookupCall());
    setText("R" + ClientSession.get().getSessionClientNr() + "-" + registration.getRegistrationNr());
    setKey(registration.getRegistrationNr());
  }

  @Override
  protected void insertTestRow() {
  }

  @Override
  protected void deleteTestRow() throws ProcessingException {
    registration.remove();
  }

  @Override
  protected LookupCall createLookupCall() {
    return new RegistrationLookupCall();
  }

}
