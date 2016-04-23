package com.rtiming.client;

import java.util.Locale;

import org.eclipse.scout.rt.client.AbstractClientSession;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.servicetunnel.IServiceTunnel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.client.common.ui.desktop.Desktop;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.settings.account.IAccountProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;

public class ClientSession extends AbstractClientSession {
  private static Logger logger = LoggerFactory.getLogger(ClientSession.class);

  public ClientSession() {
    super(true);
  }

  public ClientSession(boolean initConfig) {
    super(initConfig);
  }

  /**
   * @return session in current ThreadContext
   */
  public static ClientSession get() {
    return (ClientSession) ClientSessionProvider.currentSession();
  }

// TODO MIG  
//  @Override
//  public Subject getOfflineSubject() {
//    Subject subject = new Subject();
//    subject.getPrincipals().add(new SimplePrincipal("admin"));
//    return subject;
//  }

  @Override
  public void execLoadSession() throws ProcessingException {
    boolean isStandalone = FMilaUtility.isStandalone();
    setMemoryPolicy(new FMilaMemoryPolicy());

    if (isStandalone) {
// TODO MIG      
//      OfflineState.setOfflineDefault(true);
//      OfflineState.setOfflineInCurrentThread(true);
//
//      setServiceTunnel(execCreateServiceTunnel());
    }
    else {
      String version = FMilaUtility.getVersion();
      // TODO MIG String serverUrl = getBundle().getBundleContext().getProperty("server.url");

      // TODO MIG setServiceTunnel(new HttpServiceTunnel(this, serverUrl, version));
    }

    Locale locale = new Locale(BEANS.get(LanguageCodeType.class).getCode(getLanguageUid()).getExtKey());
    setLocale(locale);
    // TODO MIG LocaleThreadLocal.set(locale);

    // Get Client Nr
    BEANS.get(IAccountProcessService.class).loadClientNr();

    // Load code types
    CODES.getAllCodeTypes("com.rtiming");

    setDesktop(new Desktop());

    // turn client notification polling on
    if (FMilaUtility.isRichClient() && !FMilaUtility.isTestEnvironment()) {
      // getServiceTunnel().setClientNotificationPollInterval(2000L); TODO MIG
    }
  }

  @Override
  public void execStoreSession() throws ProcessingException {
  }

  public Long getLanguageUid() {
    return getSharedContextVariable("languageUid", Long.class);
  }

  public Long getSessionClientNr() {
    return getSharedContextVariable("sessionClientNr", Long.class);
  }

  protected IServiceTunnel execCreateServiceTunnel() throws ProcessingException {
// TODO MIG    
//    return new HttpServiceTunnel(this, "http://www.4mila.com") { // never used, always offline
//
//      @Override
//      protected ServiceTunnelResponse tunnel(ServiceTunnelRequest call) {
//        return tunnelOffline(call);
//      }
//
//    };
    return null;
  }
}
