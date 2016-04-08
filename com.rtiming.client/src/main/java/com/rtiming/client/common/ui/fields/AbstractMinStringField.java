package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.TEXTS;

public abstract class AbstractMinStringField extends AbstractTrimmedStringField {

  protected int getConfiguredMinLength() {
    return 6;
  }

  @Override
  protected int getConfiguredMaxLength() {
    return 250;
  }

  @Override
  protected void execChangedValue() throws ProcessingException {
    clearErrorStatus();
    if (StringUtility.length(getValue()) < getConfiguredMinLength()) {
      setErrorStatus(TEXTS.get("TextMinimumException", NumberUtility.format(getConfiguredMinLength())));
    }
  }

}
