package com.rtiming.shared.common.web;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.settings.account.AccountFormData;

@TunnelToServer
public interface IWebService extends IService {
  String createNewSession(AccountFormData account, String clientIp, String userAgent) throws ProcessingException;

  Long getAccountNrFromSessionKey(String sessionKey) throws ProcessingException;

}
