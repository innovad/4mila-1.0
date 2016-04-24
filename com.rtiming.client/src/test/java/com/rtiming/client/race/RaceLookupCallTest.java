package com.rtiming.client.race;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.Texts;
import com.rtiming.shared.race.RaceLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class RaceLookupCallTest extends AbstractDefaultLookupCallTest {

  private EventWithIndividualValidatedRaceTestDataProvider event;

  @Override
  protected void init() throws ProcessingException {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{}, new String[]{});
    setLookupCall(new RaceLookupCall());
    setText("TestStringabcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwx, TestStringabcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwx - 012345 (TestStringabcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmn), " + Texts.get("Downloaded"));
    setKey(event.getRaceNr());
  }

  @Override
  public void insertTestRow() throws ProcessingException {
  }

  @Override
  protected LookupCall createLookupCall() {
    return new RaceLookupCall();
  }

  @Override
  public void deleteTestRow() throws ProcessingException {
    event.remove();
  }

}
