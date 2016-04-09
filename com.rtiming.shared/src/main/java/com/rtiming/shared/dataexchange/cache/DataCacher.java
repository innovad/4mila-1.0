package com.rtiming.shared.dataexchange.cache;

import java.util.HashMap;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

public final class DataCacher implements IDataCacher {

  private static HashMap<String, DataCacher> cacher;
  private final HashMap<Class<? extends AbstractDataCacher>, AbstractDataCacher> cache = new HashMap<Class<? extends AbstractDataCacher>, AbstractDataCacher>();

  private DataCacher() {
  }

  public static DataCacher get(String id) {
    if (cacher == null) {
      cacher = new HashMap<String, DataCacher>();
    }
    if (cacher.get(id) == null) {
      cacher.put(id, new DataCacher());
    }
    return cacher.get(id);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends AbstractDataCacher> T getCache(Class<T> clazz) throws ProcessingException {
    if (cache.get(clazz) == null) {
      try {
        AbstractDataCacher<?, ?> newCache = clazz.newInstance();
        cache.put(clazz, newCache);
      }
      catch (Exception e) {
        throw new ProcessingException("Creating cacher failed ", e);
      }
    }
    return (T) cache.get(clazz);
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractDataCacher> T addCache(AbstractDataCacher addCacher) throws ProcessingException {
    if (cache.get(addCacher.getClass()) == null) {
      cache.put(addCacher.getClass(), addCacher);
    }
    return (T) cache.get(addCacher.getClass());
  }

  public void clear() {
    cache.clear();
  }

}
