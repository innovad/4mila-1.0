package com.rtiming.client.entry;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientSyncJob;
import com.rtiming.client.race.RaceForm;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.TestingRunnable;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class EntriesTablePageMissingTest {

  private EventWithIndividualClassTestDataProvider event;
  private EntryTestDataProvider entry;

  @Before
  public void before() throws ProcessingException {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());
  }

  @Test
  public void testEventDefault() throws Exception {
    EntriesMissingTablePage page = new EntriesMissingTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("Event", event.getEventNr(), ((EntriesSearchForm) page.getSearchFormInternal()).getEventField().getValue());
    Assert.assertEquals("No Entries", 0, page.getTable().getRowCount());
  }

  @Test
  public void testMissing() throws Exception {
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    EntriesMissingTablePage page = new EntriesMissingTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("1 Entry", 1, page.getTable().getRowCount());
  }

  @Test
  public void testDisqualification() throws Exception {
    for (EntryForm form : ClientSession.get().getDesktop().findForms(EntryForm.class)) {
      form.doClose();
    }

    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    final EntriesMissingTablePage page = new EntriesMissingTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("1 Entry", 1, page.getTable().getRowCount());
    Assert.assertNotNull("RaceNr must exists", page.getTable().getRaceNrColumn().getValue(0));

    Runnable runnableAfterMenu = new TestingRunnable() {

      @Override
      public void runTest() throws ProcessingException {
        RaceForm race = FMilaClientTestUtility.findLastAddedForm(RaceForm.class);
        Assert.assertNull("Initial No Status", race.getRaceStatusField().getValue());
        ScoutClientAssert.assertDisabled(race.getRaceStatusField());
        race.getManualStatusField().setValue(true);
        ScoutClientAssert.assertEnabled(race.getRaceStatusField());
        race.getRaceStatusField().setValue(RaceStatusCodeType.DisqualifiedCode.ID);
        race.doOk();
      }
    };

    FMilaClientSyncJob job = new FMilaClientSyncJob("RaceForm", ClientSession.get()) {
      @Override
      protected void runVoid() {
        RaceForm form = new RaceForm();
        form.setRaceNr(page.getTable().getRaceNrColumn().getValue(0));
        form.startModify();
        form.waitFor();
      }
    };
    FMilaClientTestUtility.runBlockingJob(job, runnableAfterMenu);

    EntriesMissingTablePage page2 = new EntriesMissingTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
    page2.nodeAddedNotify();
    page2.loadChildren();

    Assert.assertEquals("0 Missing Entries", 0, page2.getTable().getRowCount());
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    if (entry != null) {
      entry.remove();
    }
  }

}
