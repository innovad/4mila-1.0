package com.rtiming.client.common.database;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.shared.dao.RtRunner;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class TableColumnsTablePageTest extends AbstractTablePageTest<TableColumnsTablePage> {

  @Override
  protected TableColumnsTablePage getTablePage() throws ProcessingException {
    return new TableColumnsTablePage(RtRunner.class.getSimpleName());
  }

}
