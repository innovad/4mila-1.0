package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;

public abstract class AbstractTrimmedStringField extends AbstractStringField {

  @Override
  protected String execValidateValue(String rawValue) throws ProcessingException {
    return StringUtility.trim(rawValue);
  }

}
