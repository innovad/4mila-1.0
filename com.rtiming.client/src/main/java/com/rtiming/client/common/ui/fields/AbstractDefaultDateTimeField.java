package com.rtiming.client.common.ui.fields;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateTimeField;
import org.eclipse.scout.rt.platform.util.date.DateUtility;

public abstract class AbstractDefaultDateTimeField extends AbstractDateTimeField {

  public Date getConfiguredDefaultDate() {
    return new Date();
  }

  @Override
  protected Date execValidateValue(Date rawValue) throws ProcessingException {
    if (rawValue != null) {
      Date date1970 = new Date(0);
      Date dateDefault = getConfiguredDefaultDate();
      if (DateUtility.isSameDay(date1970, rawValue)) {
        GregorianCalendar greg = new GregorianCalendar();
        greg.setTime(rawValue);

        GregorianCalendar def = new GregorianCalendar();
        def.setTime(dateDefault);

        greg.set(def.get(Calendar.YEAR), def.get(Calendar.MONTH), def.get(Calendar.DAY_OF_MONTH));
        rawValue = greg.getTime();
      }
    }
    return super.execValidateValue(rawValue);
  }

}
