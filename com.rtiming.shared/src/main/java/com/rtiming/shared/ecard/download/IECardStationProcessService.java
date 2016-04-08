package com.rtiming.shared.ecard.download;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IECardStationProcessService extends IService {

  ECardStationFormData prepareCreate(ECardStationFormData formData) throws ProcessingException;

  ECardStationFormData create(ECardStationFormData formData) throws ProcessingException;

  ECardStationFormData load(ECardStationFormData formData) throws ProcessingException;

  ECardStationFormData store(ECardStationFormData formData) throws ProcessingException;

  ECardStationFormData find(String port, String clientAddress) throws ProcessingException;

  void delete(ECardStationFormData formData, boolean deletePunches) throws ProcessingException;
}
