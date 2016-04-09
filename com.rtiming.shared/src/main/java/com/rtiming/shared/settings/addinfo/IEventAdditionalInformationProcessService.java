package com.rtiming.shared.settings.addinfo;

import java.util.HashMap;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IEventAdditionalInformationProcessService extends IService {

  EventAdditionalInformationFormData prepareCreate(EventAdditionalInformationFormData formData) throws ProcessingException;

  EventAdditionalInformationFormData create(EventAdditionalInformationFormData formData) throws ProcessingException;

  EventAdditionalInformationFormData load(EventAdditionalInformationFormData formData) throws ProcessingException;

  EventAdditionalInformationFormData store(EventAdditionalInformationFormData formData) throws ProcessingException;

  EventAdditionalInformationFormData delete(EventAdditionalInformationFormData formData) throws ProcessingException;

  HashMap<Long, long[]> loadEventAdditionalInformationConfiguration() throws ProcessingException;

}
