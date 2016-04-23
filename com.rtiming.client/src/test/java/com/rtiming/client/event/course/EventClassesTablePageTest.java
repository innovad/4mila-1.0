package com.rtiming.client.event.course;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractTablePageTest;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class EventClassesTablePageTest extends AbstractTablePageTest<EventClassesTablePage> {

  @Override
  protected EventClassesTablePage getTablePage() {
    return new EventClassesTablePage(null, null);
  }

}
