package com.rtiming.shared.settings.fee;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IFeeGroupProcessService extends IService {

  FeeGroupFormData prepareCreate(FeeGroupFormData formData) throws ProcessingException;

  FeeGroupFormData create(FeeGroupFormData formData) throws ProcessingException;

  FeeGroupFormData load(FeeGroupFormData formData) throws ProcessingException;

  FeeGroupFormData store(FeeGroupFormData formData) throws ProcessingException;

  void delete(FeeGroupFormData formData) throws ProcessingException;

  Long[] getMissingCurrencies(Long feeGroupNr) throws ProcessingException;
}
