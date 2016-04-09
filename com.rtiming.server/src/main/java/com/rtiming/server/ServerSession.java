package com.rtiming.server;

import java.util.Locale;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.server.AbstractServerSession;
import org.eclipse.scout.rt.server.session.ServerSessionProvider;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.shared.settings.account.IAccountProcessService;
import com.rtiming.shared.settings.user.IUserProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;
import com.rtiming.shared.settings.user.UserFormData;

public class ServerSession extends AbstractServerSession {
  private static Logger logger = LoggerFactory.getLogger(ServerSession.class);
  private Long[] roleUids;

  public ServerSession() {
    super(true);
  }

  /**
   * @return session in current ThreadContext
   */
  public static ServerSession get() {
    return (ServerSession) ServerSessionProvider.currentSession();
  }

  @Override
  protected void execLoadSession() throws ProcessingException {
    // get Client Nr first
    BEANS.get(IAccountProcessService.class).loadClientNr();

    // Default Language
    setLanguageUid(LanguageCodeType.English.ID);

    // User Settings
    UserFormData user = BEANS.get(IUserProcessService.class).find(getUserId());
    if (user.getLanguage().getValue() != null) {
      setLanguageUid(user.getLanguage().getValue());
    }
    roleUids = user.getRoles().getValue();

    Locale locale = new Locale(CODES.getCodeType(LanguageCodeType.class).getCode(getLanguageUid()).getExtKey());
    // TODO MIG setLocale(locale);
    // TODO MIG LocaleThreadLocal.set(locale);

    logger.info("created a new session for " + getUserId() + " with clientNr " + getSessionClientNr());
  }

  public Long[] getRoleUids() {
    return roleUids;
  }

  public Long getLanguageUid() {
    return getSharedContextVariable("languageUid", Long.class);
  }

  private void setLanguageUid(Long languageUid) {
    setSharedContextVariable("languageUid", Long.class, languageUid);
  }

  public Long getSessionClientNr() {
    return getSharedContextVariable("sessionClientNr", Long.class);
  }

  public void setSessionClientNr(Long clientNr) {
    setSharedContextVariable("sessionClientNr", Long.class, clientNr);
  }
}
