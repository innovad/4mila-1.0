package com.rtiming.shared.settings.city;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

public interface ICityProcessService extends IService {

  CityFormData prepareCreate(CityFormData formData) throws ProcessingException;

  CityFormData create(CityFormData formData) throws ProcessingException;

  CityFormData load(CityFormData formData) throws ProcessingException;

  CityFormData store(CityFormData formData) throws ProcessingException;

  CityFormData findCity(String name, String zip, String region, String countryName, Long countryLanguageUid, String countryCode) throws ProcessingException;

  CityFormData delete(CityFormData formData) throws ProcessingException;

}
