package com.rtiming.client.entry;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class EntriesTablePageRegistrationTest {

  private EventWithIndividualClassTestDataProvider event;
  private EntryTestDataProvider entry;

  @Test
  public void test() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    EntriesTablePage page = new EntriesTablePage(null, ClientSession.get().getSessionClientNr(), entry.getRegistrationNr(), null, null, null);
    page.nodeAddedNotify();
    page.loadChildren();

    EntriesSearchForm search = page.getSearchForm();

    Assert.assertNull("Event on search form", search.getEventField().getValue());
    Assert.assertNull("From on search form", search.getStartTimeFrom().getValue());
    Assert.assertNull("To on search form", search.getStartTimeTo().getValue());

    // 1 row loaded
    Assert.assertEquals("1 row", 1, page.getTable().getRowCount());
    Assert.assertEquals("Entry Nr", entry.getEntryNr(), page.getTable().getEntryNrColumn().getValue(0));
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
    if (entry != null) {
      entry.remove();
    }
  }

}
