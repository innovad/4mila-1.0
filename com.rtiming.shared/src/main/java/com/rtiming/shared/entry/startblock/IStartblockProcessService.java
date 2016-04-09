package com.rtiming.shared.entry.startblock;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IStartblockProcessService extends IService {

  StartblockFormData prepareCreate(StartblockFormData formData) throws ProcessingException;

  StartblockFormData create(StartblockFormData formData) throws ProcessingException;

  StartblockFormData load(StartblockFormData formData) throws ProcessingException;

  StartblockFormData store(StartblockFormData formData) throws ProcessingException;
}
