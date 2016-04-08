package com.rtiming.shared.event;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IEventClassProcessService extends IService {

  EventClassFormData prepareCreate(EventClassFormData formData) throws ProcessingException;

  EventClassFormData create(EventClassFormData formData) throws ProcessingException;

  EventClassFormData load(EventClassFormData formData) throws ProcessingException;

  EventClassFormData store(EventClassFormData formData) throws ProcessingException;

  EventClassFormData delete(EventClassFormData formData) throws ProcessingException;

}
