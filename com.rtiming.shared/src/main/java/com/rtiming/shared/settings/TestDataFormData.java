package com.rtiming.shared.settings;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public class TestDataFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public TestDataFormData() {
  }

  public Clazz getClazz() {
    return getFieldByClass(Clazz.class);
  }

  public Count getCount() {
    return getFieldByClass(Count.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public static class Clazz extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Clazz() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Count extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Count() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Event extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Event() {
    }

    /**
     * list of derived validation rules.
     */
 
  }
}
