package com.rtiming.client.ecard.download;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.ecard.download.DownloadedECardFormData;
import com.rtiming.shared.ecard.download.IDownloadedECardProcessService;
import com.rtiming.shared.ecard.download.PunchFormData;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class DownloadedECardFormTest extends AbstractFormTest<DownloadedECardForm> {

  private EventTestDataProvider event;
  private ECardTestDataProvider ecard;
  private ECardStationTestDataProvider station;
  private EventWithIndividualValidatedRaceTestDataProvider eventWithDownload;

  @Override
  public void setUpForm() throws ProcessingException {
    event = new EventTestDataProvider();
    ecard = new ECardTestDataProvider();
    station = new ECardStationTestDataProvider();
    super.setUpForm();
  }

  @Override
  protected DownloadedECardForm getStartedForm() throws ProcessingException {
    DownloadedECardForm form = new DownloadedECardForm();
    form.getECardField().setValue(ecard.getECardNr());
    form.getECardStationField().setValue(station.getECardStationNr());
    form.startNew();
    return form;
  }

  @Override
  protected DownloadedECardForm getModifyForm() throws ProcessingException {
    DownloadedECardForm form = new DownloadedECardForm();
    form.setPunchSessionNr(getForm().getPunchSessionNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    if (getForm().getPunchSessionNr() != null) {
      DownloadedECardFormData formData = new DownloadedECardFormData();
      formData.setPunchSessionNr(getForm().getPunchSessionNr());
      BEANS.get(IDownloadedECardProcessService.class).delete(formData);
    }
    event.remove();
    ecard.remove();
    station.remove();
  }

  @Test
  public void testMatchClass() throws Exception {
    eventWithDownload = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31", "32", "33"}, new String[]{"31", "32", "33"});
    List<PunchFormData> punches = createPunchList("31", "32", "33");
    Long[] classes = BEANS.get(IDownloadedECardProcessService.class).matchClass(eventWithDownload.getPunchSessionNr(), punches, eventWithDownload.getEventNr(), eventWithDownload.getForm().getZeroTimeField().getValue(), null, null);
    assertEquals("1 Class should be found", 1, classes.length);
    assertEquals("Correct class should be found", eventWithDownload.getClassUid(), classes[0]);
  }

  private List<PunchFormData> createPunchList(String... controlNos) {
    List<PunchFormData> punches = new ArrayList<PunchFormData>();
    for (String controlNo : controlNos) {
      PunchFormData punch = new PunchFormData();
      punch.getControlNo().setValue(controlNo);
      punches.add(punch);
    }
    return punches;
  }

  @After
  public void after() throws ProcessingException {
    if (eventWithDownload != null) {
      eventWithDownload.remove();
    }
  }

}
