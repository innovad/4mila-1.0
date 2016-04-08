package com.rtiming.client;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.MediumMemoryPolicy;
import org.eclipse.scout.rt.client.ui.form.IForm;

/**
 * 
 */
public class FMilaMemoryPolicy extends MediumMemoryPolicy {

  @Override
  protected void loadSearchFormState(IForm f, String pageFormIdentifier) throws ProcessingException {
    // no caching
  }

}
