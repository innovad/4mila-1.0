package com.rtiming.server.common.database.jpa;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.dao.RtKey;
import com.rtiming.shared.dao.util.IKeyService;

public class ServerKeyService  implements IKeyService {

  @Override
  public Long getClientNr() {
    return ServerSession.get().getSessionClientNr();
  }

  @Override
  public Long verifyId(Long id) {
    if (id == null) {
      RtKey key = new RtKey();
      try {
        JPA.persist(key);
      }
      catch (ProcessingException e) {
        throw new RuntimeException("failed creating new id", e);
      }
      id = key.getKeyNr();
    }
    return id;
  }

}
