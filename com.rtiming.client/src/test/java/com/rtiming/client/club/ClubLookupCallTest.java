package com.rtiming.client.club;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.data.ClubTestDataProvider;
import com.rtiming.shared.club.ClubLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class ClubLookupCallTest extends AbstractDefaultLookupCallTest {

  private ClubTestDataProvider club;

  @Override
  public void insertTestRow() throws ProcessingException {
    club = new ClubTestDataProvider();
  }

  @Override
  protected LookupCall createLookupCall() {
    return new ClubLookupCall();
  }

  @Override
  public void deleteTestRow() throws ProcessingException {
    club.remove();
  }

}
