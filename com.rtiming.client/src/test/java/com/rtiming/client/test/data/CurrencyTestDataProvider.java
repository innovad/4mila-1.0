package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.settings.currency.CurrencyForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;

public class CurrencyTestDataProvider extends AbstractTestDataProvider<CurrencyForm> {

  public CurrencyTestDataProvider() throws ProcessingException {
    callInitializer();
  }

  private CurrencyForm currency;

  @Override
  protected CurrencyForm createForm() throws ProcessingException {

    // Currency
    currency = new CurrencyForm();
    currency.startNew();
    FormTestUtility.fillFormFields(currency, new FieldValue(CurrencyForm.MainBox.ExchangeRateField.class, 1.0));
    currency.doOk();

    return currency;
  }

  @Override
  public void remove() throws ProcessingException {
    CurrencyFormData formData = new CurrencyFormData();
    formData.setCurrencyUid(currency.getCurrencyUid());
    BEANS.get(ICurrencyProcessService.class).delete(formData);
  }

  public Long getCurrencyUid() throws ProcessingException {
    return getForm().getCurrencyUid();
  }

}
