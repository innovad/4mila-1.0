package com.rtiming.client.ecard.download;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.shared.ecard.ECardStationLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class ECardStationLookupCallTest extends AbstractDefaultLookupCallTest {

  @Override
  protected LookupCall createLookupCall() {
    return new ECardStationLookupCall();
  }

}
