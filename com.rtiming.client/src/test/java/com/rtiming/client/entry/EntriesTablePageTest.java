package com.rtiming.client.entry;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.client.entry.startlist.StartListNodePage;
import com.rtiming.client.event.EventNodePage;
import com.rtiming.client.event.EventsOutline;
import com.rtiming.client.event.EventsTablePage;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.ClientTestingUtility;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestClientSession.class)
public class EntriesTablePageTest extends AbstractTablePageTest<EntriesTablePage> {

  private EventWithIndividualClassTestDataProvider event;

  @Before
  public void before() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
  }

  @Override
  protected EntriesTablePage getTablePage() throws ProcessingException {
    return new EntriesTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

  @Test
  public void testNewEntryMenu() throws ProcessingException {
    IPage root = ClientTestingUtility.gotoOutline(EventsOutline.class);
    EventsTablePage events = ClientTestingUtility.gotoChildPage(root, EventsTablePage.class);
    events.loadChildren();
    EventNodePage node = ClientTestingUtility.gotoChildPage(events, EventNodePage.class);
    StartListNodePage startlistNode = ClientTestingUtility.gotoChildPage(node, StartListNodePage.class);
    AbstractEntriesTablePage entries = ClientTestingUtility.gotoChildPage(startlistNode, AbstractEntriesTablePage.class);

    EntryForm form = new EntryForm();
    form.getRegistrationField().setValue(entries.getRegistrationNr());
    form.startNew();
    if (entries.getEventNr() != null) {
      form.getEventsField().getTable().getEventNrColumn().setValue(0, entries.getEventNr());
    }
    Assert.assertEquals(entries.getEventNr(), form.getEventsField().getTable().getEventNrColumn().getValue(0));

    form.doClose();
  }

}
