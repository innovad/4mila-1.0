package com.rtiming.shared.event;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;


public class EventsSearchFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public EventsSearchFormData() {
  }

  public Location getLocation() {
    return getFieldByClass(Location.class);
  }

  public Mapp getMapp() {
    return getFieldByClass(Mapp.class);
  }

  public Name getName() {
    return getFieldByClass(Name.class);
  }

  public Type getType() {
    return getFieldByClass(Type.class);
  }

  public ZeroTimeFrom getZeroTimeFrom() {
    return getFieldByClass(ZeroTimeFrom.class);
  }

  public ZeroTimeTo getZeroTimeTo() {
    return getFieldByClass(ZeroTimeTo.class);
  }

  public static class Location extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Location() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Mapp extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Mapp() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Name extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Name() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Type extends AbstractValueFieldData<Long[]> {
    private static final long serialVersionUID = 1L;

    public Type() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class ZeroTimeFrom extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public ZeroTimeFrom() {
    }
  }

  public static class ZeroTimeTo extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public ZeroTimeTo() {
    }
  }
}
