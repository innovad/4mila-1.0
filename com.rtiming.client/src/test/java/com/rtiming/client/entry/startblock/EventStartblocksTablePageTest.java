package com.rtiming.client.entry.startblock;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class EventStartblocksTablePageTest extends AbstractTablePageTest<EventStartblocksTablePage> {

  @Override
  protected EventStartblocksTablePage getTablePage() {
    return new EventStartblocksTablePage(null);
  }

}
