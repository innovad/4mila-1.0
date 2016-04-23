package com.rtiming.client.ecard.download;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.IECardStationProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class ECardStationFormTest extends AbstractFormTest<ECardStationForm> {

  private ECardStationTestDataProvider station;

  @Override
  protected ECardStationForm getStartedForm() throws ProcessingException {
    return FMilaClientTestUtility.getECardStation();
  }

  @Override
  protected ECardStationForm getModifyForm() throws ProcessingException {
    ECardStationForm form = new ECardStationForm();
    form.setECardStationNr(getForm().getECardStationNr());
    form.startModify();
    return form;
  }

  @Test
  public void testDisabled() throws Exception {
    station = new ECardStationTestDataProvider();
    ECardStationForm form = new ECardStationForm();
    form.setECardStationNr(station.getECardStationNr());
    form.startModify();

    // always disabled
    ScoutClientAssert.assertDisabled(form.getClientAddressField());
    ScoutClientAssert.assertDisabled(form.getIdentifierField());
    ScoutClientAssert.assertDisabled(form.getBaudField());
    ScoutClientAssert.assertDisabled(form.getPortField());

    // disabled because client address does not match
    ScoutClientAssert.assertDisabled(form.getPrinterField());
    ScoutClientAssert.assertDisabled(form.getPosPrinterField());
    ScoutClientAssert.assertDisabled(form.getModusField());
    ScoutClientAssert.assertDisabled(form.getOkButton());

    // always enabled
    ScoutClientAssert.assertEnabled(form.getCancelButton());
  }

  @Test
  public void testEnabled() throws Exception {
    station = new ECardStationTestDataProvider();
    Long eCardStationNr = station.getECardStationNr();

    // set current host address on ecard station
    ECardStationTestDataProvider.assignToCurrentHost(eCardStationNr);

    ECardStationForm form = new ECardStationForm();
    form.setECardStationNr(eCardStationNr);
    form.startModify();

    // always disabled
    ScoutClientAssert.assertDisabled(form.getClientAddressField());
    ScoutClientAssert.assertDisabled(form.getIdentifierField());
    ScoutClientAssert.assertDisabled(form.getBaudField());
    ScoutClientAssert.assertDisabled(form.getPortField());

    // enabled because client address does match
    ScoutClientAssert.assertEnabled(form.getPrinterField());
    ScoutClientAssert.assertEnabled(form.getPosPrinterField());
    ScoutClientAssert.assertEnabled(form.getModusField());
    ScoutClientAssert.assertEnabled(form.getOkButton());

    // always enabled
    ScoutClientAssert.assertEnabled(form.getCancelButton());
  }

  @Override
  public void cleanup() throws ProcessingException {
    ECardStationFormData formData = new ECardStationFormData();
    formData.setECardStationNr(getForm().getECardStationNr());
    BEANS.get(IECardStationProcessService.class).delete(formData, true);
  }

  @After
  public void after() throws ProcessingException {
    if (station != null) {
      station.remove();
    }
  }

}
