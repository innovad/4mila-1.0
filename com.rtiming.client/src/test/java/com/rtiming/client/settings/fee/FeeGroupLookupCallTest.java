package com.rtiming.client.settings.fee;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.data.FeeGroupTestDataProvider;
import com.rtiming.shared.settings.fee.FeeGroupLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class FeeGroupLookupCallTest extends AbstractDefaultLookupCallTest {

  private FeeGroupTestDataProvider feeGroup;

  @Override
  protected void insertTestRow() throws ProcessingException {
    feeGroup = new FeeGroupTestDataProvider();
  }

  @Override
  protected LookupCall createLookupCall() {
    return new FeeGroupLookupCall();
  }

  @Override
  public void deleteTestRow() throws ProcessingException {
    feeGroup.remove();
  }

}
