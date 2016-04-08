package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.shared.FMilaUtility;

public abstract class AbstractDefaultDateTimeWithSecondsField extends AbstractDefaultDateTimeField {

  @Override
  protected void execInitField() throws ProcessingException {
    setFormat(FMilaUtility.DEFAULT_TIME_FORMAT_DATE_HMS);
  }

}
