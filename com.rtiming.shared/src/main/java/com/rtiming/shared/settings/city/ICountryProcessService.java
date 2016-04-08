package com.rtiming.shared.settings.city;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;


public interface ICountryProcessService extends IService {

  CountryFormData prepareCreate(CountryFormData formData) throws ProcessingException;

  CountryFormData create(CountryFormData formData) throws ProcessingException;

  CountryFormData load(CountryFormData formData) throws ProcessingException;

  CountryFormData store(CountryFormData formData) throws ProcessingException;

  CountryFormData find(String name, String code, String nation) throws ProcessingException;

  void delete(CountryFormData formData) throws ProcessingException;
}
