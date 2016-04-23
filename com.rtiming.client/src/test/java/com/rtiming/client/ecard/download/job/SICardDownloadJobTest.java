package com.rtiming.client.ecard.download.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
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
import com.rtiming.client.ecard.download.FMilaSerialTestPort;
import com.rtiming.client.ecard.download.processor.TestSICardProcessor;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.race.RaceControlsTablePage;
import com.rtiming.client.runner.RunnerForm.MainBox.ECardField;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class SICardDownloadJobTest {

  private ECardStationTestDataProvider station;
  private EventWithIndividualValidatedRaceTestDataProvider event;
  private EntryTestDataProvider entry;
  private RunnerTestDataProvider runner;
  private RunnerTestDataProvider runner2;
  private ECardTestDataProvider ecard;
  private ECardTestDataProvider ecard2;
  private CurrencyTestDataProvider currency;

  private RtEcard ecardBean;
  private Long defaultEventNr;

  @Before
  public void before() throws ProcessingException {
    defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31", "32", "33"}, new String[]{});
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());
  }

  @Test
  public void testDownload() throws Exception {
    station = new ECardStationTestDataProvider(ECardStationDownloadModusCodeType.DownloadSplitTimesCode.ID);
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(event.getEventNr());
    FMilaSerialPort port = new FMilaSerialTestPort();

    Long eCardNr = entry.getECardNr();
    RtEcardKey key = RtEcardKey.create(eCardNr);
    RtEcard eCard = BEANS.get(IECardProcessService.class).load(key);

    TestSICardProcessor processor = new TestSICardProcessor(eCard.getEcardNo(), new String[]{"31", "32", "33"}, station.getFormData(), port, evtZero, event.getEventNr(), ClientSession.get());
    SICardDownloadJob job = new SICardDownloadJob(station.getFormData(), ClientSession.get(), port, ECardStationDownloadModusCodeType.DownloadSplitTimesCode.ID, evtZero, event.getEventNr(), processor);
    job.start();

    RaceControlsTablePage tablePage = new RaceControlsTablePage(entry.getRaceNr());
    tablePage.loadChildren();
    Assert.assertEquals(5, tablePage.getTable().getRowCount());
  }

  @Test
  public void testDownloadAndRegisterNewECard() throws Exception {
    final String ecardNo = "99900";

    station = new ECardStationTestDataProvider(ECardStationDownloadModusCodeType.DownloadSplitTimesAndRegisterCode.ID);
    ecardBean = BEANS.get(IECardProcessService.class).findECard(ecardNo);
    Assert.assertNull(ecardBean.getKey());

    Runnable runnableAfterWaitFor = new Runnable() {
      @Override
      public void run() {
        EntryForm form = FMilaClientTestUtility.findLastAddedForm(EntryForm.class);
        try {
          Assert.assertNotNull(form.getECardField().getValue());
          RtEcardKey key = RtEcardKey.create(form.getECardField().getValue());
          RtEcard check = BEANS.get(IECardProcessService.class).load(key);
          Assert.assertEquals(ecardNo, check.getEcardNo());
          form.doClose();
        }
        catch (ProcessingException e) {
          e.printStackTrace();
        }
      }
    };

    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(event.getEventNr());
    FMilaSerialPort port = new FMilaSerialTestPort();
    TestSICardProcessor processor = new TestSICardProcessor(ecardNo, new String[]{"31", "32", "33"}, station.getFormData(), port, evtZero, event.getEventNr(), ClientSession.get());
    SICardDownloadJob job = new SICardDownloadJob(station.getFormData(), ClientSession.get(), port, ECardStationDownloadModusCodeType.DownloadSplitTimesAndRegisterCode.ID, evtZero, event.getEventNr(), processor);

    FMilaClientTestUtility.runBlockingJob(job, runnableAfterWaitFor);

    ecardBean = BEANS.get(IECardProcessService.class).findECard(ecardNo);
    Assert.assertNotNull(ecardBean.getKey());
    Assert.assertNotNull(ecardBean.getKey().getId());
  }

  @Test
  public void testDownloadAndRegisterExistingRunner() throws Exception {
    station = new ECardStationTestDataProvider(ECardStationDownloadModusCodeType.DownloadSplitTimesAndRegisterCode.ID);
    ecard = new ECardTestDataProvider("99999");
    List<FieldValue> fixedValues = new ArrayList<FieldValue>();
    fixedValues.add(new FieldValue(ECardField.class, ecard.getECardNr()));
    runner = new RunnerTestDataProvider(fixedValues);
    currency = new CurrencyTestDataProvider();

    Runnable runnableAfterWaitFor = new Runnable() {
      @Override
      public void run() {
        EntryForm form = FMilaClientTestUtility.findLastAddedForm(EntryForm.class);
        try {
          Assert.assertNotNull(form.getECardField().getValue());
          Assert.assertNotNull(form.getRunnerField().getValue());
          Assert.assertEquals(ecard.getECardNr(), form.getECardField().getValue());
          Assert.assertEquals(runner.getRunnerNr(), form.getRunnerField().getValue());
          form.getCurrencyField().setValue(currency.getCurrencyUid());
          form.doOk();
        }
        catch (ProcessingException e) {
          e.printStackTrace();
        }
      }
    };

    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(event.getEventNr());
    FMilaSerialPort port = new FMilaSerialTestPort();
    TestSICardProcessor processor = new TestSICardProcessor("99999", new String[]{"31", "32", "33"}, station.getFormData(), port, evtZero, event.getEventNr(), ClientSession.get());
    SICardDownloadJob job = new SICardDownloadJob(station.getFormData(), ClientSession.get(), port, ECardStationDownloadModusCodeType.DownloadSplitTimesAndRegisterCode.ID, evtZero, event.getEventNr(), processor);

    FMilaClientTestUtility.runBlockingJob(job, runnableAfterWaitFor);
  }

  @Test
  public void testDownloadAndRegisterExistingRunnerButChangeRunner() throws Exception {
    station = new ECardStationTestDataProvider(ECardStationDownloadModusCodeType.DownloadSplitTimesAndRegisterCode.ID);
    currency = new CurrencyTestDataProvider();

    // Runner 1
    ecard = new ECardTestDataProvider("99999");
    List<FieldValue> fixedValues = new ArrayList<FieldValue>();
    fixedValues.add(new FieldValue(ECardField.class, ecard.getECardNr()));
    runner = new RunnerTestDataProvider(fixedValues);

    // Runner 2
    ecard2 = new ECardTestDataProvider("88888");
    fixedValues = new ArrayList<FieldValue>();
    fixedValues.add(new FieldValue(ECardField.class, ecard2.getECardNr()));
    runner2 = new RunnerTestDataProvider(fixedValues);

    Runnable runnableAfterWaitFor = new Runnable() {
      @Override
      public void run() {
        EntryForm form = FMilaClientTestUtility.findLastAddedForm(EntryForm.class);
        try {
          Assert.assertNotNull(form.getECardField().getValue());
          Assert.assertNotNull(form.getRunnerField().getValue());
          Assert.assertEquals(ecard.getECardNr(), form.getECardField().getValue());
          Assert.assertEquals(runner.getRunnerNr(), form.getRunnerField().getValue());
          form.getRunnerField().setValue(runner2.getRunnerNr());
          Assert.assertEquals(ecard.getECardNr(), form.getECardField().getValue());
          form.getCurrencyField().setValue(currency.getCurrencyUid());
          form.doOk();
        }
        catch (ProcessingException e) {
          e.printStackTrace();
        }
      }
    };

    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(event.getEventNr());
    FMilaSerialPort port = new FMilaSerialTestPort();
    TestSICardProcessor processor = new TestSICardProcessor("99999", new String[]{"31", "32", "33"}, station.getFormData(), port, evtZero, event.getEventNr(), ClientSession.get());
    SICardDownloadJob job = new SICardDownloadJob(station.getFormData(), ClientSession.get(), port, ECardStationDownloadModusCodeType.DownloadSplitTimesAndRegisterCode.ID, evtZero, event.getEventNr(), processor);

    FMilaClientTestUtility.runBlockingJob(job, runnableAfterWaitFor);
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    if (entry != null) {
      entry.remove();
    }
    station.remove();
    if (ecardBean != null && ecardBean.getKey() != null) {
      BEANS.get(IECardProcessService.class).delete(ecardBean.getKey());
    }
    if (runner != null) {
      runner.remove();
    }
    if (ecard != null) {
      ecard.remove();
    }
    if (currency != null) {
      currency.remove();
    }
    if (defaultEventNr != null) {
      BEANS.get(IDefaultProcessService.class).setDefaultEventNr(defaultEventNr);
    }
  }

}
