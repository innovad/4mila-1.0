package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.commons.exception.ProcessingException;

public interface IDataCacher {

  public <T extends AbstractDataCacher> T getCache(Class<T> clazz) throws ProcessingException;

}
