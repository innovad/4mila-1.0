package com.rtiming.client.ecard.download;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.test.ClientTestingUtility;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.serial.SerialUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.IECardStationProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class ECardStationStatusFormTest {

  private static final String TESTINGPORT = ClientTestingUtility.getSerialTestingPort();
  private static final String RED = "CC0000";
  private EventTestDataProvider event;

  @Test(expected = VetoException.class)
  public void testPortModusRequired() throws Exception {
    ECardStationStatusForm form = new ECardStationStatusForm();
    form.startForm();
    form.getRestartButton().doClick();
    form.doClose();
  }

  @Test(expected = VetoException.class)
  public void testPortRequired() throws Exception {
    ECardStationStatusForm form = new ECardStationStatusForm();
    form.startForm();
    form.getModusField().setValue(ECardStationDownloadModusCodeType.EntryCode.ID);
    form.getRestartButton().doClick();
    form.doClose();
  }

  @Test(expected = VetoException.class)
  public void testModusRequired() throws Exception {
    ECardStationStatusForm form = new ECardStationStatusForm();
    form.startForm();
    form.getComPortField().setValue(TESTINGPORT);
    form.getRestartButton().doClick();
    form.doClose();
  }

  @Test
  public void testNewPort() throws Exception {
    System.out.println("Testing Port: " + TESTINGPORT);
    System.out.println("*** Begin Port ***");
    for (String port : SerialUtility.getPorts()) {
      System.out.println("Port: " + port);
    }
    System.out.println("*** End Port ***");
    ECardStationFormData formData = BEANS.get(IECardStationProcessService.class).find(TESTINGPORT, FMilaUtility.getHostAddress());
    while (formData.getECardStationNr() != null) {
      formData = BEANS.get(IECardStationProcessService.class).find(TESTINGPORT, FMilaUtility.getHostAddress());
      if (formData.getECardStationNr() != null) {
        BEANS.get(IECardStationProcessService.class).delete(formData, false);
      }
    }
    formData = BEANS.get(IECardStationProcessService.class).find(TESTINGPORT, FMilaUtility.getHostAddress());
    Assert.assertNull("Should be null, station deleted", formData.getECardStationNr());

    event = new EventTestDataProvider();
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());

    ECardStationStatusForm form = new ECardStationStatusForm();
    form.startForm();
    form.getModusField().setValue(ECardStationDownloadModusCodeType.EntryCode.ID);
    form.getComPortField().setValue(TESTINGPORT);
    form.getRestartButton().doClick();
    form.doClose();

    Assert.assertTrue(RED.equals(form.getStatusField().getBackgroundColor()));

    formData = BEANS.get(IECardStationProcessService.class).find(TESTINGPORT, FMilaUtility.getHostAddress());
    Assert.assertNotNull("Should not be null, station created", formData.getECardStationNr());
  }

  @Test
  public void testFindPort() throws Exception {
    ECardStationTestDataProvider provider = new ECardStationTestDataProvider();
    provider.getForm().startModify();
    provider.getForm().getPortField().setValue(TESTINGPORT);
    provider.getForm().getClientAddressField().setValue(FMilaUtility.getHostAddress());
    provider.getForm().doOk();

    ECardStationFormData formData = BEANS.get(IECardStationProcessService.class).find(TESTINGPORT, FMilaUtility.getHostAddress());
    Assert.assertNotNull(formData.getECardStationNr());
    Assert.assertEquals(provider.getECardStationNr(), formData.getECardStationNr());

    BEANS.get(IECardStationProcessService.class).delete(formData, false);
    provider.remove();
  }

  @Test
  public void testDummyPort() throws Exception {
    event = new EventTestDataProvider();
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());

    ECardStationStatusForm form = new ECardStationStatusForm();
    form.startForm();
    form.getModusField().setValue(ECardStationDownloadModusCodeType.EntryCode.ID);
    form.getComPortField().setValue("DUMMY");
    form.getRestartButton().doClick();
    form.doClose();

    Assert.assertTrue(RED.equals(form.getStatusField().getBackgroundColor()));
  }

  @Test(expected = VetoException.class)
  public void testNoDefaultEvent() throws ProcessingException {
    ECardStationStatusForm form = new ECardStationStatusForm();
    form.startForm();
    form.init(ECardStationDownloadModusCodeType.EntryCode.ID, "DUMMY2");
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
  }

}
