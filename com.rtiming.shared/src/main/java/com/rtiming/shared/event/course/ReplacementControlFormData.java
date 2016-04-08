package com.rtiming.shared.event.course;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public class ReplacementControlFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public ReplacementControlFormData() {
  }

  public Control getControl() {
    return getFieldByClass(Control.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public ReplacementControl getReplacementControl() {
    return getFieldByClass(ReplacementControl.class);
  }

  public static class Control extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Control() {
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

  public static class ReplacementControl extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ReplacementControl() {
    }

    /**
     * list of derived validation rules.
     */
 
  }
}
