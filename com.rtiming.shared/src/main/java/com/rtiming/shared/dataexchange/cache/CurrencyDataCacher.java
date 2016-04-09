package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.AdditionalInformationUtility;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;

public class CurrencyDataCacher extends AbstractDataCacher<CurrencyFormData, String> {

  private final Long eventNr;

  public CurrencyDataCacher(Long eventNr) {
    this.eventNr = eventNr;
  }

  @Override
  protected Long getPrimaryKey(CurrencyFormData formData) {
    return formData.getCurrencyUid();
  }

  @Override
  protected CurrencyFormData store(CurrencyFormData formData) throws ProcessingException {
    return BEANS.get(ICurrencyProcessService.class).store(formData);
  }

  @Override
  protected CurrencyFormData create(CurrencyFormData formData) throws ProcessingException {
    CurrencyFormData currency = BEANS.get(ICurrencyProcessService.class).create(formData);
    AdditionalInformationUtility.createIndividualStartFee(eventNr);
    return currency;
  }

  @Override
  protected CurrencyFormData find(String string) throws ProcessingException {
    CodeFormData codeFormData = BEANS.get(ICodeProcessService.class).find(string, CurrencyCodeType.ID);
    CurrencyFormData currency = new CurrencyFormData();
    currency.setCurrencyUid(codeFormData.getCodeUid());
    return BEANS.get(ICurrencyProcessService.class).load(currency);
  }

}
