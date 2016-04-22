package com.rtiming.client.event.course;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.event.course.CourseLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class CourseLookupCallTest extends AbstractDefaultLookupCallTest {

  private static EventWithIndividualClassTestDataProvider event;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    event.remove();
  }

  @Override
  protected LookupCall createLookupCall() throws ProcessingException {
    CourseLookupCall courseLookupCall = new CourseLookupCall();
    courseLookupCall.setEventNr(event.getEventNr());
    return courseLookupCall;
  }

}
