package com.rtiming.client;

import org.eclipse.scout.rt.client.MediumMemoryPolicy;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

/**
 * 
 */
public class FMilaMemoryPolicy extends MediumMemoryPolicy {

  @Override
  protected void loadSearchFormState(IForm f, String pageFormIdentifier) throws ProcessingException {
    // no caching
  }

}
