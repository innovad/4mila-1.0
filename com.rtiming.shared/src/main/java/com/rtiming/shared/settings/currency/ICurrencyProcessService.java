package com.rtiming.shared.settings.currency;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface ICurrencyProcessService extends IService {

  CurrencyFormData prepareCreate(CurrencyFormData formData) throws ProcessingException;

  CurrencyFormData create(CurrencyFormData formData) throws ProcessingException;

  CurrencyFormData load(CurrencyFormData formData) throws ProcessingException;

  CurrencyFormData store(CurrencyFormData formData) throws ProcessingException;

  void delete(CurrencyFormData formData) throws ProcessingException;
}
