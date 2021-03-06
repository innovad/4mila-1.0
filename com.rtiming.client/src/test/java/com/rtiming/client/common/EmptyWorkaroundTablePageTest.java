package com.rtiming.client.common;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class EmptyWorkaroundTablePageTest extends AbstractTablePageTest<EmptyWorkaroundTablePage> {

  @Override
  protected EmptyWorkaroundTablePage getTablePage() {
    return new EmptyWorkaroundTablePage();
  }

}
