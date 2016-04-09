package com.rtiming.server.settings.account;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.security.EncryptionUtility;
import org.eclipse.scout.rt.platform.util.Base64Utility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.dao.RtAccount;
import com.rtiming.shared.dao.RtAccountClient;
import com.rtiming.shared.dao.RtAccountClientKey;
import com.rtiming.shared.dao.RtClient;
import com.rtiming.shared.dao.util.IKeyService;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.settings.account.AccountFormData;
import com.rtiming.shared.settings.account.IAccountProcessService;

public class AccountProcessService implements IAccountProcessService {

  @Override
  public AccountFormData prepareCreate(AccountFormData formData) throws ProcessingException {
    return formData;
  }

  @Override
  public AccountFormData create(AccountFormData formData, boolean createNewClientNr) throws ProcessingException {
    // check email AND username unique
    boolean exists = checkUniqueEmailUsername(formData.getUsername().getValue(), formData.getEMail().getValue());
    if (exists) {
      throw new VetoException(TEXTS.get("UserNameEMailExistsErrorMessage"));
    }

    // create account
    RtAccount account = new RtAccount();
    JPA.persist(account);

    formData.setAccountNr(account.getAccountNr());
    formData = store(formData);

    if (createNewClientNr) {
      long clientNr = createNewClient(formData.getAccountNr());
      formData.setClientNr(clientNr);
    }

    return formData;
  }

  protected boolean checkUniqueEmailUsername(String usernameString, String emailString) throws ProcessingException {
    String username = StringUtility.emptyIfNull(usernameString).toLowerCase();
    String email = StringUtility.emptyIfNull(emailString).toLowerCase();

    String queryString = "SELECT COUNT(A.accountNr) FROM RtAccount A " + "WHERE (LOWER(email) = :email OR LOWER(username) = :username) ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("email", email);
    query.setParameter("username", username);
    Long result = query.getSingleResult();

    return result != null && result >= 1;
  }

  @Override
  public AccountFormData load(AccountFormData formData) throws ProcessingException {

    JPAUtility.select("SELECT username, password, password, firstName, lastName, email " + "FROM RtAccount " + "WHERE accountNr = :accountNr " + "INTO :username, :password, :repeatPassword, :firstName, :lastName, :eMail ", formData);

    return formData;
  }

  @Override
  public AccountFormData store(AccountFormData formData) throws ProcessingException {
    // encrypt password
    String passwordEncrypted = formData.getPassword().getValue();
    if (formData.isUpdatePassword()) {
      try {
        passwordEncrypted = Base64Utility.encode(EncryptionUtility.signMD5(passwordEncrypted.getBytes()));
      }
      catch (NoSuchAlgorithmException e) {
        throw new ProcessingException("Password Encryption failed", e);
      }
    }

    String queryString = "UPDATE RtAccount " + "SET username = :usernameLowercase, " + "password = :passwordEncrypted, " + "firstName = :firstName, " + "lastName = :lastName, " + "email = :eMail " + "WHERE accountNr = :accountNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("usernameLowercase", formData.getUsername().getValue() == null ? "" : formData.getUsername().getValue().toLowerCase());
    query.setParameter("passwordEncrypted", passwordEncrypted);
    query.setParameter("accountNr", formData.getAccountNr());
    query.setParameter("eMail", formData.getEMail().getValue());
    query.setParameter("firstName", formData.getFirstName().getValue());
    query.setParameter("lastName", formData.getLastName().getValue());
    query.executeUpdate();

    // update link between online accounts and uploaded runners (based on email address)
    BEANS.get(IRunnerProcessService.class).updateOnlineAccountRunnerLinks(formData.getAccountNr(), null, null);

    return formData;
  }

  @Override
  public long createNewClient(long accountNr) throws ProcessingException {
    RtClient client = new RtClient();
    client.setClientNr(BEANS.get(IKeyService.class).verifyId(null));
    JPA.persist(client);
    long newClientNr = client.getClientNr();

    RtAccountClient ac = new RtAccountClient();
    RtAccountClientKey key = new RtAccountClientKey();
    key.setAccountNr(accountNr);
    key.setClientNr(newClientNr);
    ac.setId(key);
    JPA.persist(ac);

    return newClientNr;
  }

  @Override
  public Long loadClientNr() throws ProcessingException {

    // select the one and only client nr on an event database
    String queryString = "SELECT MAX(id.clientNr) " + "FROM RtClient " + "WHERE 1 = (SELECT COUNT(id.clientNr) FROM RtClient) ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    Long sessionClientNr = query.getSingleResult();

    // select the account's global client nr on global database running 4mila manager
    if (sessionClientNr == null) {
      queryString = "SELECT rtClient.clientNr " + "FROM RtAccount " + "WHERE username = :userId ";
      query = JPA.createQuery(queryString, Long.class);
      query.setParameter("userId", ServerSession.get().getUserId());
      sessionClientNr = query.getSingleResult();
    }

    ServerSession.get().setSessionClientNr(sessionClientNr);
    if (sessionClientNr != null) {
      // ServerSession.get().getSharedVariableMap().put(ICodeType.PROP_PARTITION_ID, sessionClientNr.getValue());
    }

    return sessionClientNr;
  }

  @Override
  public AccountFormData login(AccountFormData formData, boolean createNewClientNr) throws ProcessingException {

    String passwordEncrypted = StringUtility.emptyIfNull(formData.getPassword().getValue());
    try {
      passwordEncrypted = Base64Utility.encode(EncryptionUtility.signMD5(passwordEncrypted.getBytes()));
    }
    catch (NoSuchAlgorithmException e) {
      throw new ProcessingException("Password Encryption failed", e);
    }

    String username = StringUtility.emptyIfNull(StringUtility.nvl(formData.getUsername().getValue(), formData.getEMail().getValue())).toLowerCase();
    String email = StringUtility.emptyIfNull(StringUtility.nvl(formData.getEMail().getValue(), formData.getUsername().getValue())).toLowerCase();

    String queryString = "SELECT MAX(id.accountNr) " + "FROM RtAccount " + "WHERE (LOWER(username) = :username OR LOWER(email) = :email) " + "AND password = :passwordEncrypted ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("username", username);
    query.setParameter("email", email);
    query.setParameter("passwordEncrypted", passwordEncrypted);
    Long accountNr = query.getSingleResult();

    if (accountNr != null) {
      AccountFormData login = new AccountFormData();
      login.setAccountNr(accountNr);
      login = load(login);

      if (createNewClientNr) {
        long clientNr = createNewClient(login.getAccountNr());
        login.setClientNr(clientNr);
      }

      return login;
    }
    return null;
  }

  @Override
  public AccountFormData find(String email) throws ProcessingException {
    AccountFormData result = new AccountFormData();
    String queryString = "SELECT MAX(id.accountNr) " + "FROM RtAccount " + "WHERE LOWER(email) = :email ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("email", email);
    Long accountNr = query.getSingleResult();

    if (accountNr != null) {
      result.setAccountNr(accountNr);
      result = load(result);
    }
    else {
      result.getEMail().setValue(email);
    }
    return result;
  }

  @Override
  public Date getServerTime() throws ProcessingException {
    return new Date();
  }

}
