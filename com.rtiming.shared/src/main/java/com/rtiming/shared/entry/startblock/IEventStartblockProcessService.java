package com.rtiming.shared.entry.startblock;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IEventStartblockProcessService extends IService {

  EventStartblockFormData prepareCreate(EventStartblockFormData formData) throws ProcessingException;

  EventStartblockFormData create(EventStartblockFormData formData) throws ProcessingException;

  EventStartblockFormData load(EventStartblockFormData formData) throws ProcessingException;

  EventStartblockFormData store(EventStartblockFormData formData) throws ProcessingException;

  EventStartblockFormData delete(EventStartblockFormData formData) throws ProcessingException;
}
