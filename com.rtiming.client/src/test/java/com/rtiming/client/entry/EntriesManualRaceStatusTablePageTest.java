package com.rtiming.client.entry;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
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
import com.rtiming.client.race.RaceForm;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.race.RaceStatusCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class EntriesManualRaceStatusTablePageTest extends AbstractTablePageTest<EntriesManualRaceStatusTablePage> {

  private EventWithIndividualClassTestDataProvider event;
  private EntryTestDataProvider entry;

  @Before
  public void before() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
  }

  @Override
  protected EntriesManualRaceStatusTablePage getTablePage() throws ProcessingException {
    return new EntriesManualRaceStatusTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
  }

  @Test
  public void testRow() throws Exception {
    RaceForm form = new RaceForm();
    form.setRaceNr(entry.getRaceNr());
    form.startModify();
    form.getManualStatusField().setValue(true);
    form.getRaceStatusField().setValue(RaceStatusCodeType.DisqualifiedCode.ID);
    form.doOk();

    EntriesManualRaceStatusTablePage page = new EntriesManualRaceStatusTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("1 Row", 1, page.getTable().getRowCount());
    Assert.assertEquals("Race in Table", form.getRaceNr(), page.getTable().getRaceNrColumn().getValue(0));
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

}
