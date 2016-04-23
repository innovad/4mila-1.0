package com.rtiming.client.runner;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractSimpleDatabaseLookupServiceTest;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.shared.runner.RunnerLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RunnerLookupCallTest extends AbstractSimpleDatabaseLookupServiceTest {

  private RunnerTestDataProvider runner;

  @Override
  protected void init() throws ProcessingException {
    runner = new RunnerTestDataProvider();
    setLookupCall(new RunnerLookupCall());
    setText(runner.getForm().getLastNameField().getValue());
    setKey(runner.getRunnerNr());
  }

  @Override
  protected void insertTestRow() {
  }

  @Override
  protected void deleteTestRow() throws ProcessingException {
    runner.remove();
  }

}
