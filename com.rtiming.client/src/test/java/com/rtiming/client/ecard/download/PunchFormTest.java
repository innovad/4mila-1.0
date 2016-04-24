package com.rtiming.client.ecard.download;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.ecard.download.DownloadedECardFormData;
import com.rtiming.shared.ecard.download.IDownloadedECardProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class PunchFormTest extends AbstractFormTest<PunchForm> {

  private DownloadedECardForm punchSession;
  private static ECardTestDataProvider ecard;
  private static ECardStationTestDataProvider station;
  private static EventTestDataProvider event;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    ecard = new ECardTestDataProvider();
    station = new ECardStationTestDataProvider();
    event = new EventTestDataProvider();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    event.remove();
    ecard.remove();
    station.remove();
  }

  @Override
  public void setUpForm() throws ProcessingException {

    // Punch Session
    punchSession = new DownloadedECardForm();
    punchSession.getECardStationField().setValue(station.getECardStationNr());
    punchSession.getECardField().setValue(ecard.getECardNr());
    punchSession.startNew();
    FormTestUtility.fillFormFields(punchSession);
    punchSession.doOk();

    super.setUpForm();
  }

  @Override
  protected PunchForm getStartedForm() throws ProcessingException {
    PunchForm punchForm = new PunchForm();
    punchForm.getPunchSessionField().setValue(punchSession.getPunchSessionNr());
    punchForm.startNew();
    return punchForm;
  }

  @Override
  protected PunchForm getModifyForm() throws ProcessingException {
    PunchForm punchForm = new PunchForm();
    punchForm.getPunchSessionField().setValue(getForm().getPunchSessionField().getValue());
    punchForm.getSortCodeField().setValue(getForm().getSortCodeField().getValue());
    punchForm.startModify();
    return punchForm;
  }

  @Override
  public void cleanup() throws ProcessingException {
    DownloadedECardFormData punchSessionData = new DownloadedECardFormData();
    punchSessionData.setPunchSessionNr(punchSession.getPunchSessionNr());
    BEANS.get(IDownloadedECardProcessService.class).delete(punchSessionData);
  }

}
