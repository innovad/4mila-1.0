package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

public abstract class AbstractImportDataCacher<T extends AbstractFormData, K extends Object> extends AbstractDataCacher<T, K> {

  public T doImport(K value) throws ProcessingException {
    T formData = this.get(value);
    if (getPrimaryKey(formData) == null) {
      createNewData(formData, value);
      formData = this.put(value, formData);
    }
    return formData;
  }

  protected abstract void createNewData(T formData, K value);

}
