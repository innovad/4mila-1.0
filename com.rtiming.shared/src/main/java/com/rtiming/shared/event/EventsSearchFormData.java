package com.rtiming.shared.event;

import java.util.Date;
import java.util.Set;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.event.EventsSearchForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class EventsSearchFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

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
  }

  public static class Mapp extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class Name extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class Type extends AbstractValueFieldData<Set<Long>> {

    private static final long serialVersionUID = 1L;
  }

  public static class ZeroTimeFrom extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class ZeroTimeTo extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }
}
