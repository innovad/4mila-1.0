package com.rtiming.client.map;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.data.MapTestDataProvider;
import com.rtiming.shared.map.MapLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class MapLookupCallTest extends AbstractDefaultLookupCallTest {

  private MapTestDataProvider map;

  @Override
  public void insertTestRow() throws ProcessingException {
    map = new MapTestDataProvider();
  }

  @Override
  protected LookupCall createLookupCall() {
    return new MapLookupCall();
  }

  @Override
  public void deleteTestRow() throws ProcessingException {
    map.remove();
  }

}
