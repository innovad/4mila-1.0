package com.rtiming.shared.settings.account;

import java.util.Date;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IAccountProcessService extends IService {

  /**
   * Global Usage
   */
  AccountFormData prepareCreate(AccountFormData formData) throws ProcessingException;

  /**
   * Global Usage
   */
  AccountFormData create(AccountFormData formData, boolean createNewClientNr) throws ProcessingException;

  /**
   * Global Usage
   */
  AccountFormData load(AccountFormData formData) throws ProcessingException;

  /**
   * Global Usage
   */
  AccountFormData store(AccountFormData formData) throws ProcessingException;

  /**
   * Global Usage
   */
  long createNewClient(long accountNr) throws ProcessingException;

  /**
   * Global/Local Usage
   */
  Long loadClientNr() throws ProcessingException;

  /**
   * Login into a 4mila account on 4mila online server
   * 
   * @param formData
   *          set user name, password for login
   * @return AccountFormData with accountNr if successful, null otherwise
   * @throws ProcessingException
   */
  AccountFormData login(AccountFormData formData, boolean createNewClientNr) throws ProcessingException;

  /**
   * Global Usage
   */
  AccountFormData find(String email) throws ProcessingException;

  /**
   * Local/Global Usage
   */
  Date getServerTime() throws ProcessingException;
}
