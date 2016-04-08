package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.ICityProcessService;

public class CityRegionDataCacher extends AbstractDataCacher<CityFormData, CityRegionCacheKey> {

  @Override
  protected Long getPrimaryKey(CityFormData formData) {
    return formData.getCityNr();
  }

  @Override
  protected CityFormData store(CityFormData formData) throws ProcessingException {
    return BEANS.get(ICityProcessService.class).store(formData);
  }

  @Override
  protected CityFormData create(CityFormData formData) throws ProcessingException {
    return BEANS.get(ICityProcessService.class).create(formData);
  }

  @Override
  protected CityFormData find(CityRegionCacheKey string) throws ProcessingException {
    return BEANS.get(ICityProcessService.class).findCity(string.getCityName(), string.getZipCode(), string.getRegion(), string.getCountryName(), string.getLanguageUid(), string.getCountryCode());
  }

}
