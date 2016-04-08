package com.rtiming.shared.entry;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public abstract class AbstractRegistrationSearchBoxData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractRegistrationSearchBoxData() {
  }

  public EvtRegistrationFrom getEvtRegistrationFrom() {
    return getFieldByClass(EvtRegistrationFrom.class);
  }

  public EvtRegistrationTo getEvtRegistrationTo() {
    return getFieldByClass(EvtRegistrationTo.class);
  }

  public Number getNumber() {
    return getFieldByClass(Number.class);
  }

  public StartlistSettingOptionGroup getStartlistSettingOptionGroup() {
    return getFieldByClass(StartlistSettingOptionGroup.class);
  }

  public static class EvtRegistrationFrom extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public EvtRegistrationFrom() {
    }
  }

  public static class EvtRegistrationTo extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public EvtRegistrationTo() {
    }
  }

  public static class Number extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Number() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class StartlistSettingOptionGroup extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public StartlistSettingOptionGroup() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
