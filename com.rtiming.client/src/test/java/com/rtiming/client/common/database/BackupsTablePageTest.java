package com.rtiming.client.common.database;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class BackupsTablePageTest extends AbstractTablePageTest<BackupsTablePage> {

  @Override
  protected BackupsTablePage getTablePage() throws ProcessingException {
    return new BackupsTablePage();
  }

}
