package com.rtiming.client.common.excel;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.client.event.EventsTablePage;
import com.rtiming.client.test.data.EventTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class ExcelUtilityTest {

  private EventTestDataProvider event;

  @Before
  public void before() throws ProcessingException {
    event = new EventTestDataProvider();
  }

  @Test
  public void testExport() throws Exception {
    EventsTablePage page = new EventsTablePage(ClientSession.get().getSessionClientNr());
    page.loadChildren();
    ExcelUtility.export(page.getTable());
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

}
