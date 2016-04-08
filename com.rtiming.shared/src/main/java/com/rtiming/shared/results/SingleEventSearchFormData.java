package com.rtiming.shared.results;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public class SingleEventSearchFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public SingleEventSearchFormData() {
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
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
