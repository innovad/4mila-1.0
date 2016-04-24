package com.rtiming.client.ecard.download.job;

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
import com.rtiming.client.TestClientSession;
import com.rtiming.client.ecard.download.AbstractDownloadedECardsTablePage;
import com.rtiming.client.ecard.download.DownloadedECardForm;
import com.rtiming.client.ecard.download.DownloadedECardsAllTablePage;
import com.rtiming.client.ecard.download.DownloadedECardsSearchForm;
import com.rtiming.client.ecard.download.FMilaSerialTestPort;
import com.rtiming.client.ecard.download.processor.TestSICardProcessor;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.ecard.download.DownloadedECardFormData;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.ecard.download.IDownloadedECardProcessService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.test.helper.ITestingJPAService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestClientSession.class)
public class SICardDownloadJobAssignRaceTest {

  private EventWithIndividualClassTestDataProvider event;
  private EntryTestDataProvider entry;
  private ECardStationTestDataProvider station;
  private ECardTestDataProvider ecard;
  private Long defaultEventNr;

  @Before
  public void before() throws ProcessingException {
    defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    event = new EventWithIndividualClassTestDataProvider();
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());
  }

  @Test
  public void testAssignRace() throws Exception {
    FMilaSerialPort port = new FMilaSerialTestPort();
    long mode = ECardStationDownloadModusCodeType.DownloadSplitTimesAndAssignRaceCode.ID;
    station = new ECardStationTestDataProvider(mode);
    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(event.getEventNr());
    ecard = new ECardTestDataProvider("77777");
    TestSICardProcessor processor = new TestSICardProcessor(ecard.getForm().getNumberField().getValue(), new String[]{"31", "32", "33"}, station.getFormData(), port, evtZero, event.getEventNr(), ClientSession.get());

    SICardDownloadJob job = new SICardDownloadJob(station.getFormData(), ClientSession.get(), port, mode, evtZero, event.getEventNr(), processor);
    Runnable runnableAfterWaitFor = new Runnable() {
      @Override
      public void run() {
        DownloadedECardForm form = FMilaClientTestUtility.findLastAddedForm(DownloadedECardForm.class);
        Assert.assertNotNull("DownloadedECardForm is open", form);
        Assert.assertTrue("No Race", form.getRaceField().isEmpty());
        try {
          form.getRaceField().setValue(entry.getRaceNr());
          form.doOk();
        }
        catch (ProcessingException e) {
          e.printStackTrace();
          Assert.fail(e.getMessage());
        }
      }
    };
    FMilaClientTestUtility.runBlockingJob(job, runnableAfterWaitFor);

    AbstractDownloadedECardsTablePage page = new DownloadedECardsAllTablePage();
    page.setSearchForm(new DownloadedECardsSearchForm());
    page.getSearchFormInternal().start();
    page.loadChildren();

    Assert.assertEquals("1 row", 1, page.getTable().getRowCount());
    Assert.assertEquals("Race stored", page.getTable().getRaceNrColumn().getValue(0), entry.getRaceNr());
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    List<Long> punchSessionNrs = BEANS.get(ITestingJPAService.class).getPunchSessionsForStation(null);

    for (Long punchSessionNr : punchSessionNrs) {
      DownloadedECardFormData formData = new DownloadedECardFormData();
      formData.setPunchSessionNr(punchSessionNr);
      BEANS.get(IDownloadedECardProcessService.class).delete(formData);
    }
    ecard.remove();
    station.remove();
    if (defaultEventNr != null) {
      BEANS.get(IDefaultProcessService.class).setDefaultEventNr(defaultEventNr);
    }
  }
}
