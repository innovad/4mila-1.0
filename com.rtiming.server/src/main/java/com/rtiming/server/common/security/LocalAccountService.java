package com.rtiming.server.common.security;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.common.security.ILocalAccountService;
import com.rtiming.shared.dao.RtClient;
import com.rtiming.shared.settings.account.AccountFormData;

public class LocalAccountService implements ILocalAccountService {

  private static final IScoutLogger LOG = ScoutLogManager.getLogger(LocalAccountService.class);

  @Override
  public AccountFormData loginOnline(AccountFormData formData) throws ProcessingException {
    try {
      // only create a new client nr when there is no client nr on event server
      // user may cancel setup wizard and retry
      if (formData.getClientNr() == null) {
        formData.setClientNr(ServerSession.get().getSessionClientNr());
      }

      // TODO MIG
      // OnlineServiceSoap onlineService = BEANS.get(OnlineServiceClient.class).getPortType();
//
//      LoginAccount login = new LoginAccount();
//      login.setAccountData(AccountUtility.formDataToWsdl(formData));
//      LoginAccountResponse response = onlineService.loginAccountOnline(login);
//      return AccountUtility.wsdlToFormData(response.getAccountData());
      return null;
    }
    catch (Exception e) {
      LOG.error("connection failed", e);
      throw new VetoException(TEXTS.get("GlobalServerConnectionFailure", e.getMessage()));
    }
  }

  @Override
  public AccountFormData createOnline(AccountFormData formData) throws ProcessingException {
// TODO MIG    
//    OnlineServiceSoap onlineService = BEANS.get(OnlineServiceClient.class).getPortType();
//
//    CreateAccount create = new CreateAccount();
//    create.setAccountData(AccountUtility.formDataToWsdl(formData));
//    AccountFormData account;
//    try {
//      UploadAccountResponse response = onlineService.createAccountOnline(create);
//      account = AccountUtility.wsdlToFormData(response.getAccountData());
//      if (account == null) {
//        throw new VetoException(TEXTS.get("UserNameEMailExistsErrorMessage"));
//      }
//    }
//    catch (WebServiceException e) {
//      throw new VetoException(TEXTS.get("GlobalServerConnectionFailure", e.getMessage()));
//    }
//
//    return account;
    return null;
  }

  @Override
  public void createLocalClient(Long clientNr, boolean isEventServerDatabase) throws ProcessingException {
    if (isEventServerDatabase && ServerSession.get().getSessionClientNr() != null) {
      // there is already a client nr assigned
      return;
    }
    if (clientNr == null) {
      throw new IllegalArgumentException("ClientNr must not be NULL");
    }
    RtClient client = new RtClient();
    client.setClientNr(clientNr);
    JPA.merge(client);
    ServerSession.get().setSessionClientNr(clientNr);
  }

}
