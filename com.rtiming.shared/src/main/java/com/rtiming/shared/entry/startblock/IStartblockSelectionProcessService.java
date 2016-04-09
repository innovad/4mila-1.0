package com.rtiming.shared.entry.startblock;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IStartblockSelectionProcessService extends IService {

  StartblockSelectionFormData prepareCreate(StartblockSelectionFormData formData) throws ProcessingException;

  StartblockSelectionFormData create(StartblockSelectionFormData formData) throws ProcessingException;

  StartblockSelectionFormData load(StartblockSelectionFormData formData) throws ProcessingException;

  StartblockSelectionFormData store(StartblockSelectionFormData formData) throws ProcessingException;
}
