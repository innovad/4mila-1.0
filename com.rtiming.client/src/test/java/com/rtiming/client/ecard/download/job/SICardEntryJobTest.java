package com.rtiming.client.ecard.download.job;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.client.ecard.download.FMilaSerialTestPort;
import com.rtiming.client.entry.EntriesTablePage;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.runner.RunnerForm;
import com.rtiming.client.runner.RunnerForm.MainBox.ECardField;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestClientSession.class)
public class SICardEntryJobTest {

  private static CurrencyTestDataProvider currency;
  private static Long defaultEventNr;

  private EventWithIndividualClassTestDataProvider event;
  private RunnerTestDataProvider runner;
  private RunnerTestDataProvider runner2;
  private ECardTestDataProvider ecard;
  private ECardTestDataProvider ecard2;
  private RtEcardKey newECardKey;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    currency = new CurrencyTestDataProvider();
    defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
  }

  @Before
  public void before() throws ProcessingException {
    // Event
    event = new EventWithIndividualClassTestDataProvider();
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());
    Assert.assertNotNull(BEANS.get(IDefaultProcessService.class).getDefaultEventNr());

    // E-Card
    ecard = new ECardTestDataProvider("99999");
    List<FieldValue> preset = new ArrayList<FieldValue>();
    preset.add(new FieldValue(RunnerForm.MainBox.ECardField.class, ecard.getECardNr()));
    runner = new RunnerTestDataProvider(preset);

    // Runner
    runner2 = new RunnerTestDataProvider();
    runner2.getForm().startModify();
    runner2.getForm().getECardField().setValue(null);
    runner2.getForm().doOk();
  }

  @Before
  public void checkForm() throws ProcessingException {
    EntryForm form = ClientSession.get().getDesktop().findForm(EntryForm.class);
    if (form != null) {
      form.doClose();
    }
  }

  @Test
  public void testEntryFormClosed() throws Exception {
    SICardEntryJob job = new SICardEntryJob(ClientSession.get(), new FMilaSerialTestPort(), ecard.getForm().getNumberField().getValue());
    Runnable runnableAfterWaitFor = new Runnable() {
      public void run() {
        try {
          EntryForm form = ClientSession.get().getDesktop().findForm(EntryForm.class);

          Assert.assertNotNull(form);
          Assert.assertEquals(runner.getRunnerNr(), form.getRunnerField().getValue());

          form.doClose();
        }
        catch (ProcessingException e) {
          Assert.fail(e.getMessage());
        }
      }
    };
    FMilaClientTestUtility.runBlockingJob(job, runnableAfterWaitFor);
  }

  @Test
  public void testEntryFormOpen() throws Exception {
    EntryForm form = new EntryForm();
    form.startNew();

    SICardEntryJob job = new SICardEntryJob(ClientSession.get(), new FMilaSerialTestPort(), ecard.getForm().getNumberField().getValue());
    Runnable runnableAfterWaitFor = new Runnable() {
      public void run() {
        try {
          EntryForm form = ClientSession.get().getDesktop().findForm(EntryForm.class);
          Assert.assertEquals(runner.getRunnerNr(), form.getRunnerField().getValue());
          form.doClose();
        }
        catch (ProcessingException e) {
          Assert.fail(e.getMessage());
        }
      }
    };
    FMilaClientTestUtility.runBlockingJob(job, runnableAfterWaitFor);
  }

  @Test
  public void testEntryFormOpenOverwrite() throws Exception {
    EntryForm form = new EntryForm();
    form.startNew();
    form.getRunnerField().setValue(runner2.getRunnerNr());

    SICardEntryJob job = new SICardEntryJob(ClientSession.get(), new FMilaSerialTestPort(), ecard.getForm().getNumberField().getValue());
    Runnable runnableAfterWaitFor = new Runnable() {
      public void run() {
        try {
          EntryForm form = ClientSession.get().getDesktop().findForm(EntryForm.class);
          Assert.assertEquals(runner2.getRunnerNr(), form.getRunnerField().getValue());
          form.doClose();
        }
        catch (ProcessingException e) {
          Assert.fail(e.getMessage());
        }
      }
    };
    FMilaClientTestUtility.runBlockingJob(job, runnableAfterWaitFor);
  }

  @Test
  public void testECardEntryWithRunnerChange() throws Exception {
    Assert.assertEquals("Default Event", event.getEventNr(), BEANS.get(IDefaultProcessService.class).getDefaultEventNr());
    Assert.assertNull("No form should be open!", FMilaClientTestUtility.findLastAddedForm(EntryForm.class));
    Assert.assertNotNull("Zero Time", event.getForm().getZeroTimeField().getValue());

    // Runner 2
    ecard2 = new ECardTestDataProvider("88888");
    List<FieldValue> fixedValues = new ArrayList<FieldValue>();
    fixedValues.add(new FieldValue(ECardField.class, ecard2.getECardNr()));
    runner2 = new RunnerTestDataProvider(fixedValues);

    SICardEntryJob job = new SICardEntryJob(ClientSession.get(), new FMilaSerialTestPort(), ecard.getForm().getNumberField().getValue());
    Runnable runnableAfterWaitFor = new Runnable() {
      public void run() {
        try {
          EntryForm form = FMilaClientTestUtility.findLastAddedForm(EntryForm.class);
          Assert.assertNotNull(form.getECardField().getValue());
          Assert.assertNotNull(form.getRunnerField().getValue());
          Assert.assertEquals(ecard.getECardNr(), form.getECardField().getValue());
          Assert.assertEquals(runner.getRunnerNr(), form.getRunnerField().getValue());
          form.getRunnerField().setValue(runner2.getRunnerNr());
          Assert.assertEquals(ecard.getECardNr(), form.getECardField().getValue());
          form.getCurrencyField().setValue(currency.getCurrencyUid());
          form.getClazzField().setValue(event.getClassUid());
          form.doOk();
        }
        catch (ProcessingException e) {
          Assert.fail(e.getMessage());
        }
      }
    };
    FMilaClientTestUtility.runBlockingJob(job, runnableAfterWaitFor);

    EntriesTablePage entries = new EntriesTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, event.getClassUid(), null, null);
    entries.loadChildren();
    Assert.assertEquals("1 Entry", 1, entries.getTable().getRowCount());
    Assert.assertEquals("E-Card stored", entries.getTable().getECardColumn().getValue(0), ecard.getForm().getNumberField().getValue());

    ecard2.remove();
  }

  @Test
  public void testNewECard() throws Exception {
    final String eCardNo = "77777";
    RtEcard formData = BEANS.get(IECardProcessService.class).findECard(eCardNo);
    Assert.assertNull("E-Card should not exist", formData.getKey());

    SICardEntryJob job = new SICardEntryJob(ClientSession.get(), new FMilaSerialTestPort(), eCardNo);

    Runnable runnableAfterWaitFor = new Runnable() {
      @Override
      public void run() {
        EntryForm form = FMilaClientTestUtility.findLastAddedForm(EntryForm.class);
        Assert.assertNotNull(form);
        Assert.assertNotNull(form.getECardField().getValue());
        Assert.assertEquals(form.getECardField().getDisplayText(), eCardNo + " (SI-Card 5)");
        try {
          form.doClose();
        }
        catch (ProcessingException e) {
          Assert.fail(e.getMessage());
        }
      }
    };
    FMilaClientTestUtility.runBlockingJob(job, runnableAfterWaitFor);
    newECardKey = formData.getKey();
  }

  @Test
  public void testECardRunnerChange() throws Exception {
    final String eCardNo = "77777";

    SICardEntryJob job = new SICardEntryJob(ClientSession.get(), new FMilaSerialTestPort(), eCardNo);

    Runnable runnableAfterWaitFor = new Runnable() {
      @Override
      public void run() {
        try {
          EntryForm form = FMilaClientTestUtility.findLastAddedForm(EntryForm.class);
          Assert.assertNotNull(form);
          Assert.assertNotNull(form.getECardField().getValue());
          Assert.assertEquals(form.getECardField().getDisplayText(), eCardNo + " (SI-Card 5)");
          final RtEcard formData = BEANS.get(IECardProcessService.class).findECard(eCardNo);
          Assert.assertNotNull(formData.getKey());
          Assert.assertEquals(form.getECardField().getValue(), formData.getKey().getId());
          newECardKey = formData.getKey();

          form.getRunnerField().setValue(runner.getRunnerNr());
          assertEquals("E-Card should not change after Runner change", form.getECardField().getValue(), formData.getKey().getId());
          form.doClose();
        }
        catch (ProcessingException e) {
          Assert.fail(e.getMessage());
        }
      }
    };
    FMilaClientTestUtility.runBlockingJob(job, runnableAfterWaitFor);
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    runner.remove();
    runner2.remove();
    ecard.remove();
    if (ecard2 != null) {
      ecard2.remove();
    }
    if (newECardKey != null && newECardKey.getId() != null) {
      BEANS.get(IECardProcessService.class).delete(newECardKey);
    }
    for (IForm form : ClientSession.get().getDesktop().getDialogStack()) {
      form.doClose();
    }
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(defaultEventNr);
  }

}
