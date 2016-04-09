package com.rtiming.shared.event.course;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IControlProcessService extends IService {

  ControlFormData prepareCreate(ControlFormData formData) throws ProcessingException;

  ControlFormData create(ControlFormData formData) throws ProcessingException;

  ControlFormData load(ControlFormData formData) throws ProcessingException;

  ControlFormData store(ControlFormData formData) throws ProcessingException;

  ControlFormData find(String controlNo, long eventNr) throws ProcessingException;

  void georeferenceFromLocalPosition(Long[] controlNrs, long eventNr) throws ProcessingException;

  void delete(ControlFormData control) throws ProcessingException;
}
