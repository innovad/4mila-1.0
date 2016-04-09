package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;

public class DefaultCountryDataCacher extends AbstractDefaultDataCacher<CountryFormData> {

  @Override
  protected Long getPrimaryKey(CountryFormData formData) {
    return formData.getCountryUid();
  }

  @Override
  protected CountryFormData createDefaultValue() throws ProcessingException {
    // simply return the default country
    Long countryUid = BEANS.get(IDefaultProcessService.class).getDefaultCountryUid();
    CountryFormData country = new CountryFormData();
    country.setCountryUid(countryUid);
    return BEANS.get(ICountryProcessService.class).load(country);
  }

}
