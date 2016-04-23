package com.rtiming.client.entry;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RegistrationsTablePageTest extends AbstractTablePageTest<RegistrationsTablePage> {

  @Override
  protected RegistrationsTablePage getTablePage() {
    return new RegistrationsTablePage();
  }

}
