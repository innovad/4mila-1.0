package com.rtiming.shared.settings.addinfo;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IAdditionalInformationAdministrationProcessService extends IService {

  AdditionalInformationAdministrationFormData prepareCreate(AdditionalInformationAdministrationFormData formData) throws ProcessingException;

  AdditionalInformationAdministrationFormData create(AdditionalInformationAdministrationFormData formData) throws ProcessingException;

  AdditionalInformationAdministrationFormData load(AdditionalInformationAdministrationFormData formData) throws ProcessingException;

  AdditionalInformationAdministrationFormData store(AdditionalInformationAdministrationFormData formData) throws ProcessingException;

  void delete(AdditionalInformationAdministrationFormData formData) throws ProcessingException;
}
