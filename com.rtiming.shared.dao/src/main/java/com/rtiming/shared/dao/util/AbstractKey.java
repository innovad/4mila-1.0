package com.rtiming.shared.dao.util;

public abstract class AbstractKey<T extends IKey> implements IKey {

  private static Long getSessionClientNr() {
    // return BEANS.get(IKeyService.class).getClientNr();
    // TODO MIG
    return 1L;
  }

  private static Long verifyId(Long id) {
    // TODO MIG
    // return BEANS.get(IKeyService.class).verifyId(id);
    return 1L;
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
