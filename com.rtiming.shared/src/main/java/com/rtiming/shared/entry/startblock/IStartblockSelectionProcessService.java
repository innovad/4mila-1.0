package com.rtiming.shared.entry.startblock;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;

public interface IStartblockSelectionProcessService extends IService {

  StartblockSelectionFormData prepareCreate(StartblockSelectionFormData formData) throws ProcessingException;

  StartblockSelectionFormData create(StartblockSelectionFormData formData) throws ProcessingException;

  StartblockSelectionFormData load(StartblockSelectionFormData formData) throws ProcessingException;

  StartblockSelectionFormData store(StartblockSelectionFormData formData) throws ProcessingException;
}
