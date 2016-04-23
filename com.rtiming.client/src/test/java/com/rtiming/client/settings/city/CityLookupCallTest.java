package com.rtiming.client.settings.city;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.data.CityTestDataProvider;
import com.rtiming.shared.settings.city.CityLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class CityLookupCallTest extends AbstractDefaultLookupCallTest {

  private CityTestDataProvider city;

  @Override
  protected void insertTestRow() throws ProcessingException {
    city = new CityTestDataProvider();
  }

  @Override
  protected LookupCall createLookupCall() {
    return new CityLookupCall();
  }

  @Override
  protected void deleteTestRow() throws ProcessingException {
    city.remove();
  }

}
