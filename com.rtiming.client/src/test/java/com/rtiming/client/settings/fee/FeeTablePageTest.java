package com.rtiming.client.settings.fee;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class FeeTablePageTest extends AbstractTablePageTest<FeeTablePage> {

  @Override
  protected FeeTablePage getTablePage() {
    return new FeeTablePage(null);
  }

}
