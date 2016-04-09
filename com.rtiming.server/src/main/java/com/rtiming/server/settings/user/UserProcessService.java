package com.rtiming.server.settings.user;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.security.EncryptionUtility;
import org.eclipse.scout.rt.platform.util.Base64Utility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateUserPermission;
import com.rtiming.shared.common.security.permission.ReadUserPermission;
import com.rtiming.shared.common.security.permission.UpdateUserPermission;
import com.rtiming.shared.dao.RtUser;
import com.rtiming.shared.dao.RtUserKey;
import com.rtiming.shared.dao.RtUserRole;
import com.rtiming.shared.dao.RtUserRoleKey;
import com.rtiming.shared.settings.user.IUserProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;
import com.rtiming.shared.settings.user.UserFormData;

public class UserProcessService  implements IUserProcessService {

  @Override
  public UserFormData prepareCreate(UserFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateUserPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // set default language to system locale
    String language = Locale.getDefault().getLanguage();
    if (!StringUtility.isNullOrEmpty(language)) {
      for (ICode<?> code : CODES.getCodeType(LanguageCodeType.class).getCodes()) {
        if (language.equalsIgnoreCase(code.getExtKey())) {
          formData.getLanguage().setValue(TypeCastUtility.castValue(code.getId(), Long.class));
        }
      }
    }

    return formData;
  }

  @Override
  public UserFormData create(UserFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateUserPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtUserKey key = RtUserKey.create((Long) null);
    RtUser user = new RtUser();
    user.setId(key);
    JPA.persist(user);
    formData.setUserNr(user.getId().getId());
    formData = store(formData);

    return formData;
  }

  @Override
  public UserFormData load(UserFormData formData) throws ProcessingException {
    if (ServerSession.get().getRoleUids() != null && !ACCESS.check(new ReadUserPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT " +
        "username, " +
        "password, " +
        "password, " +
        "languageUid " +
        "FROM RtUser " +
        "WHERE id.userNr = :userNr " +
        "AND id.clientNr = :sessionClientNr " +
        "INTO :username, :password, :repeatPassword, :language ", formData);

    String queryString = "SELECT id.roleUid " +
        "FROM RtUserRole " +
        "WHERE id.userNr = :userNr " +
        "AND id.clientNr = :clientNr";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("userNr", formData.getUserNr());
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    List<Long> result = query.getResultList();
    formData.getRoles().setValue(result.toArray(new Long[]{}));

    return formData;
  }

  @Override
  public UserFormData store(UserFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateUserPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // check unique username
    String queryString = "SELECT COUNT(U.id.userNr) " +
        "FROM RtUser U " +
        "WHERE U.username = :username " +
        "AND U.id.clientNr = :sessionClientNr " +
        "AND U.id.userNr != :userNr ";

    FMilaTypedQuery<Long> queryLong = JPA.createQuery(queryString, Long.class);
    JPAUtility.setAutoParameters(queryLong, queryString, formData);
    Long checkExistingUsernames = queryLong.getSingleResult();

    if (checkExistingUsernames > 0) {
      throw new VetoException(Texts.get("UserNameExistsErrorMessage"));
    }

    // check at least one role
    if (formData.getRoles().getValue() == null || formData.getRoles().getValue().length == 0) {
      throw new VetoException(TEXTS.get("UserRoleMandatoryMessage"));
    }

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

    // store user data
    queryString = "UPDATE RtUser U " +
        "SET U.username = :usernameLowercase, " +
        "U.password = :passwordEncrypted, " +
        "U.languageUid = :language " +
        "WHERE U.id.userNr = :userNr " +
        "AND U.id.clientNr = :sessionClientNr";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("usernameLowercase", formData.getUsername().getValue().toLowerCase());
    query.setParameter("passwordEncrypted", passwordEncrypted);
    query.setParameter("language", formData.getLanguage().getValue());
    query.setParameter("userNr", formData.getUserNr());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();

    deleteUserRoles(formData.getUserNr());

    for (Long roleUid : formData.getRoles().getValue()) {
      RtUserRole role = new RtUserRole();
      RtUserRoleKey key = new RtUserRoleKey();
      key.setClientNr(ServerSession.get().getSessionClientNr());
      key.setRoleUid(roleUid);
      key.setUserNr(formData.getUserNr());
      role.setId(key);
      JPA.merge(role);
    }

    return formData;
  }

  @Override
  public UserFormData find(String username) throws ProcessingException {
    UserFormData result = new UserFormData();

    String queryString = "SELECT MAX(U.id.userNr) " +
        "FROM RtUser U " +
        "WHERE LOWER(U.username) = LOWER(:username) " +
        "AND U.id.clientNr = :sessionClientNr ";

    FMilaTypedQuery<Long> queryLong = JPA.createQuery(queryString, Long.class);
    queryLong.setParameter("username", username);
    queryLong.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Long userNr = queryLong.getSingleResult();

    if (userNr != null) {
      result.setUserNr(userNr);
      result = load(result);
    }

    return result;
  }

  @Override
  public int delete(RtUserKey... keys) throws ProcessingException {
    int k = 0;
    for (RtUserKey key : keys) {
      RtUser user = JPA.find(RtUser.class, key);
      if (user != null) {
        deleteUserRoles(user.getId().getId());
        JPA.remove(user);
        k++;
      }
    }
    return k;
  }

  private void deleteUserRoles(Long userNr) throws ProcessingException {
    String queryString = "DELETE FROM RtUserRole UR WHERE UR.id.userNr = :userNr AND UR.id.clientNr = :clientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("userNr", userNr);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();
  }

}
