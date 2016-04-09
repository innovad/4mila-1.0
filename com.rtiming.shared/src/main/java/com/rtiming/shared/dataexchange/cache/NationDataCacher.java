package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;

public class NationDataCacher extends AbstractImportDataCacher<CountryFormData, String> {

  @Override
  protected Long getPrimaryKey(CountryFormData formData) {
    return formData.getCountryUid();
  }

  @Override
  protected CountryFormData store(CountryFormData formData) throws ProcessingException {
    return BEANS.get(ICountryProcessService.class).store(formData);
  }

  @Override
  protected CountryFormData create(CountryFormData formData) throws ProcessingException {
    return BEANS.get(ICountryProcessService.class).create(formData);
  }

  @Override
  protected CountryFormData find(String string) throws ProcessingException {
    return BEANS.get(ICountryProcessService.class).find(null, null, string);
  }

  @Override
  protected void createNewData(CountryFormData formData, String value) {
    formData.getNation().setValue(value);
    for (int k = 0; k < formData.getCodeBox().getLanguage().getRowCount(); k++) {
      formData.getCodeBox().getLanguage().rowAt(k).setTranslation(value);
    }
  }

}
