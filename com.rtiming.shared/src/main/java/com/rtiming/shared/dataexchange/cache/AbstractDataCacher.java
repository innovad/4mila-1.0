package com.rtiming.shared.dataexchange.cache;

import java.util.HashMap;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

public abstract class AbstractDataCacher<T extends AbstractFormData, K extends Object> {

  private final HashMap<Object, T> cache;

  public AbstractDataCacher() {
    cache = new HashMap<Object, T>(1000);
  }

  public T get(K string) throws ProcessingException {
    T result = cache.get(string);
    if (result == null) {
      result = find(string);
      if (getPrimaryKey(result) != null) {
        cache.put(string, result);
      }
    }
    return result;
  }

  public T put(K string, T formData) throws ProcessingException {
    if (getPrimaryKey(formData) == null) {
      formData = create(formData);
    }
    else {
      formData = store(formData);
    }
    cache.put(string, formData);
    return formData;
  }

  protected abstract T find(K string) throws ProcessingException;

  protected abstract T store(T formData) throws ProcessingException;

  protected abstract T create(T formData) throws ProcessingException;

  protected abstract Long getPrimaryKey(T formData);

  @Override
  public String toString() {
    return "[" + this.getClass().getSimpleName() + " size=" + cache.size() + "]";
  }

}
