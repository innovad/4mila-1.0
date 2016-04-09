package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.AdditionalInformationUtility;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;

public class DefaultCurrencyDataCacher extends AbstractDefaultDataCacher<CurrencyFormData> {

  private final Long eventNr;

  public DefaultCurrencyDataCacher(Long eventNr) {
    this.eventNr = eventNr;
  }

  @Override
  protected Long getPrimaryKey(CurrencyFormData formData) {
    return formData.getCurrencyUid();
  }

  @Override
  protected CurrencyFormData createDefaultValue() throws ProcessingException {
    // simply return the default country
    Long currencyUid = BEANS.get(IDefaultProcessService.class).getDefaultCurrencyUid();
    CurrencyFormData country = new CurrencyFormData();
    country.setCurrencyUid(currencyUid);
    AdditionalInformationUtility.createIndividualStartFee(eventNr);
    return BEANS.get(ICurrencyProcessService.class).load(country);
  }

}
