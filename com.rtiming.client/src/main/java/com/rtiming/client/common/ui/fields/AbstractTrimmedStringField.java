package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;

public abstract class AbstractTrimmedStringField extends AbstractStringField {

  @Override
  protected String execValidateValue(String rawValue) throws ProcessingException {
    return StringUtility.trim(rawValue);
  }

}
