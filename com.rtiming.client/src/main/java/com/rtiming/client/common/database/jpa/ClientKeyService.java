package com.rtiming.client.common.database.jpa;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.dao.util.IKeyService;

public class ClientKeyService implements IKeyService {

  @Override
  public Long getClientNr() {
    return ClientSession.get().getSessionClientNr();
  }

  @Override
  public Long verifyId(Long id) {
    return id;
  }

}
