package com.rtiming.shared.settings.city;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface ICountryProcessService extends IService {

  CountryFormData prepareCreate(CountryFormData formData) throws ProcessingException;

  CountryFormData create(CountryFormData formData) throws ProcessingException;

  CountryFormData load(CountryFormData formData) throws ProcessingException;

  CountryFormData store(CountryFormData formData) throws ProcessingException;

  CountryFormData find(String name, String code, String nation) throws ProcessingException;

  void delete(CountryFormData formData) throws ProcessingException;
}
