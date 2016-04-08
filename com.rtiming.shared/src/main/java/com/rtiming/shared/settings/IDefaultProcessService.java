package com.rtiming.shared.settings;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IDefaultProcessService extends IService {

  DefaultFormData prepareCreate(DefaultFormData formData) throws ProcessingException;

  DefaultFormData create(DefaultFormData formData) throws ProcessingException;

  DefaultFormData load(DefaultFormData formData) throws ProcessingException;

  DefaultFormData store(DefaultFormData formData) throws ProcessingException;

  Long getDefaultEventNr() throws ProcessingException;

  Long getDefaultCurrencyUid() throws ProcessingException;

  Long getDefaultCountryUid() throws ProcessingException;

  Long getBackupInterval() throws ProcessingException;

  String getBackupDirectory() throws ProcessingException;

  Long getBackupMaxNumber() throws ProcessingException;

  void setDefaultEventNr(Long eventNr) throws ProcessingException;

  void setDefaultCurrencyUid(Long currencyUid) throws ProcessingException;

  void setDefaultCountryUid(Long countryUid) throws ProcessingException;

  void setBackupInterval(Long backupInterval) throws ProcessingException;

  void setBackupDirectory(String backupDirectory) throws ProcessingException;

  void setBackupMaxNumber(Long backupMaxNumber) throws ProcessingException;

}
