package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.Assert;

import com.rtiming.client.ecard.download.ECardStationForm;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.IECardStationProcessService;

public class ECardStationTestDataProvider extends AbstractTestDataProvider<ECardStationForm> {

  private Long modus;

  public ECardStationTestDataProvider() throws ProcessingException {
    modus = ECardStationDownloadModusCodeType.DownloadSplitTimesCode.ID;
    callInitializer();
  }

  public ECardStationTestDataProvider(Long modus) throws ProcessingException {
    this.modus = modus;
    callInitializer();
  }

  @Override
  protected ECardStationForm createForm() throws ProcessingException {
    ECardStationForm form = new ECardStationForm();
    MaxFormFieldValueProvider provider = new MaxFormFieldValueProvider();
    form.getIdentifierField().setEnabled(true);
    form.getBaudField().setEnabled(true);
    form.getClientAddressField().setEnabled(true);
    form.getPortField().setEnabled(true);
    provider.fillValueField(form.getIdentifierField(), null);
    provider.fillValueField(form.getBaudField(), null);
    provider.fillValueField(form.getClientAddressField(), null);
    provider.fillValueField(form.getPortField(), null);
    form.getIdentifierField().setEnabled(false);
    form.getBaudField().setEnabled(false);
    form.getClientAddressField().setEnabled(false);
    form.getPortField().setEnabled(false);
    form.getModusField().setValue(modus);
    form.startNew();
    form.touch();
    form.doOk();
    Assert.assertNotNull(form.getECardStationNr());
    return form;
  }

  @Override
  public void remove() throws ProcessingException {
    ECardStationFormData formData = new ECardStationFormData();
    formData.setECardStationNr(getECardStationNr());
    BEANS.get(IECardStationProcessService.class).delete(formData, true);
  }

  public Long getECardStationNr() throws ProcessingException {
    return getForm().getECardStationNr();
  }

  public ECardStationFormData getFormData() throws ProcessingException {
    ECardStationFormData formData = new ECardStationFormData();
    formData.setECardStationNr(getECardStationNr());
    formData = BEANS.get(IECardStationProcessService.class).load(formData);
    return formData;
  }

  public static void assignToCurrentHost(Long eCardStationNr) throws ProcessingException {
    ECardStationFormData formData = new ECardStationFormData();
    formData.setECardStationNr(eCardStationNr);
    formData = BEANS.get(IECardStationProcessService.class).load(formData);
    formData.getClientAddress().setValue(FMilaUtility.getHostAddress());
    BEANS.get(IECardStationProcessService.class).store(formData);
  }

}
