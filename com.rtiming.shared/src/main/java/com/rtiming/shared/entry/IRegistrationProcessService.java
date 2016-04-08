package com.rtiming.shared.entry;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IRegistrationProcessService extends IService {

  RegistrationFormData prepareCreate(RegistrationFormData formData) throws ProcessingException;

  RegistrationFormData create(RegistrationFormData formData) throws ProcessingException;

  RegistrationFormData load(RegistrationFormData formData) throws ProcessingException;

  RegistrationFormData store(RegistrationFormData formData) throws ProcessingException;

  void delete(RegistrationFormData formData) throws ProcessingException;

  RegistrationFormData find(String no) throws ProcessingException;

}
