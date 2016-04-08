package com.rtiming.shared.common.security;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.settings.account.AccountFormData;

@TunnelToServer
public interface ILocalAccountService extends IService {

  /**
   * Local Usage
   */
  AccountFormData loginOnline(AccountFormData formData) throws ProcessingException;

  /**
   * Local Usage
   */
  AccountFormData createOnline(AccountFormData formData) throws ProcessingException;

  /**
   * Local Usage
   */
  void createLocalClient(Long clientNr, boolean isEventServerDatabase) throws ProcessingException;

}
