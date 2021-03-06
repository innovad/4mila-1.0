package com.rtiming.shared.ecard.download;

import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.ecard.download.PunchForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class PunchFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public ControlNo getControlNo() {
    return getFieldByClass(ControlNo.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public PunchSession getPunchSession() {
    return getFieldByClass(PunchSession.class);
  }

  /**
   * access method for property RawTime.
   */
  public Long getRawTime() {
    return getRawTimeProperty().getValue();
  }

  /**
   * access method for property RawTime.
   */
  public void setRawTime(Long rawTime) {
    getRawTimeProperty().setValue(rawTime);
  }

  public RawTimeProperty getRawTimeProperty() {
    return getPropertyByClass(RawTimeProperty.class);
  }

  public SortCode getSortCode() {
    return getFieldByClass(SortCode.class);
  }

  public Time getTime() {
    return getFieldByClass(Time.class);
  }

  public static class ControlNo extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class Event extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class PunchSession extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class RawTimeProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class SortCode extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class Time extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }
}
