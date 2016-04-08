package com.rtiming.shared.entry;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IParticipationProcessService extends IService {

  ParticipationFormData prepareCreate(ParticipationFormData formData) throws ProcessingException;

  ParticipationFormData create(ParticipationFormData formData) throws ProcessingException;

  ParticipationFormData load(ParticipationFormData formData) throws ProcessingException;

  ParticipationFormData store(ParticipationFormData formData) throws ProcessingException;
}
