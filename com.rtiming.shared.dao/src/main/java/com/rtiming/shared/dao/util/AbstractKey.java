package com.rtiming.shared.dao.util;

import org.eclipse.scout.rt.platform.BEANS;

public abstract class AbstractKey<T extends IKey> implements IKey {

  private static Long getSessionClientNr() {
    return BEANS.get(IKeyService.class).getClientNr();
  }

  private static Long verifyId(Long id) {
    return BEANS.get(IKeyService.class).verifyId(id);
  }

  public static IKey createKeyInternal(IKey key) {
    if (key == null) {
      throw new IllegalArgumentException("key must be provided by create() method");
    }
    if (key.getClientNr() == null) {
      key.setClientNr(getSessionClientNr());
    }
    if (key.getId() == null) {
      key.setId(verifyId(key.getId()));
    }
    return key;
  }

}
