package com.rtiming.client.event.course;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.data.ControlTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.event.course.ControlLookupCall;
import com.rtiming.shared.event.course.ControlTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class ControlLookupCallTest extends AbstractDefaultLookupCallTest {

  private ControlTestDataProvider control;
  private EventTestDataProvider event;

  @Override
  protected void insertTestRow() throws ProcessingException {
    event = new EventTestDataProvider();
    control = new ControlTestDataProvider(event.getEventNr(), ControlTypeCodeType.ControlCode.ID, "31");
  }

  @Override
  protected LookupCall createLookupCall() {
    return new ControlLookupCall();
  }

  @Override
  protected void deleteTestRow() throws ProcessingException {
    event.remove();
    control.remove();
  }

}
