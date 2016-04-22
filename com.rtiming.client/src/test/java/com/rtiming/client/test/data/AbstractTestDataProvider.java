package com.rtiming.client.test.data;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

public abstract class AbstractTestDataProvider<T extends AbstractForm> {

  private T form;

  public AbstractTestDataProvider() throws ProcessingException {
  }

  protected final void callInitializer() throws ProcessingException {
    form = createForm();
  }

  protected abstract T createForm() throws ProcessingException;

  public T getForm() throws ProcessingException {
    return form;
  }

  public abstract void remove() throws ProcessingException;

}
