package com.rtiming.client.entry;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class EntriesClubsTablePageTest extends AbstractTablePageTest<EntriesClubsTablePage> {

  @Override
  protected EntriesClubsTablePage getTablePage() {
    return new EntriesClubsTablePage();
  }

}
