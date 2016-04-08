package com.rtiming.shared.event;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public class EventMapFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public EventMapFormData() {
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public Map getMap() {
    return getFieldByClass(Map.class);
  }

  public static class Event extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Event() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Map extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Map() {
    }

    /**
     * list of derived validation rules.
     */
 
  }
}
