package com.rtiming.client.result;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class ResultsClubsTablePageTest extends AbstractTablePageTest<ResultsClubsTablePage> {

  @Override
  protected ResultsClubsTablePage getTablePage() {
    return new ResultsClubsTablePage();
  }

}
