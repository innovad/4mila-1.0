package com.rtiming.server.common.web;

import java.util.Date;
import java.util.UUID;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.common.web.IWebService;
import com.rtiming.shared.dao.RtWebSession;
import com.rtiming.shared.settings.account.AccountFormData;

public class WebService  implements IWebService {

  @Override
  public String createNewSession(AccountFormData account, String clientIp, String userAgent) throws ProcessingException {
    if (account == null || clientIp == null || userAgent == null) {
      throw new IllegalArgumentException("arguments mandatory");
    }

    String key = UUID.randomUUID().toString();

    RtWebSession session = new RtWebSession();
    session.setSessionKey(key);
    session.setEvtLastLogin(new Date());
    session.setClientIp(clientIp);
    session.setUserAgent(userAgent);
    session.setAccountNr(account.getAccountNr());
    session.setClientNr(ServerSession.get().getSessionClientNr());
    JPA.persist(session);

    return key;
  }

  @Override
  public Long getAccountNrFromSessionKey(String sessionKey) throws ProcessingException {

    String queryString = "SELECT MAX(accountNr) " +
        "FROM RtWebSession " +
        "WHERE sessionKey = :sessionKey ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("sessionKey", sessionKey);
    Long accountNr = query.getSingleResult();

    return accountNr;
  }
}
