package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.FMilaUtility;

public abstract class AbstractMillisecondsDateTimeField extends AbstractDefaultDateTimeField {

  @Override
  protected void execInitField() throws ProcessingException {
    setFormat(FMilaUtility.DEFAULT_TIME_FORMAT);
  }

}
