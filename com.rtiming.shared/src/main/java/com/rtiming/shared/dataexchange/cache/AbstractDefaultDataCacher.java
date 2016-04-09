package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

/**
 * 
 */
public abstract class AbstractDefaultDataCacher<T extends AbstractFormData> extends AbstractDataCacher<T, String> {

  @Override
  protected final T store(T formData) throws ProcessingException {
    // not implemented
    return null;
  }

  @Override
  protected final T create(T formData) throws ProcessingException {
    // not implemented
    return null;
  }

  @Override
  protected final T find(String string) throws ProcessingException {
    return createDefaultValue();
  }

  public final T getDefault() throws ProcessingException {
    return get("default");
  }

  protected abstract T createDefaultValue() throws ProcessingException;

}
