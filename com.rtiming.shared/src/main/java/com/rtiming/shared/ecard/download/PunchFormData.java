package com.rtiming.shared.ecard.download;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class PunchFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public PunchFormData() {
  }

  public RawTimeProperty getRawTimeProperty() {
    return getPropertyByClass(RawTimeProperty.class);
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

  public ControlNo getControlNo() {
    return getFieldByClass(ControlNo.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public PunchSession getPunchSession() {
    return getFieldByClass(PunchSession.class);
  }

  public SortCode getSortCode() {
    return getFieldByClass(SortCode.class);
  }

  public Time getTime() {
    return getFieldByClass(Time.class);
  }

  public class RawTimeProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RawTimeProperty() {
    }
  }

  public static class ControlNo extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public ControlNo() {
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

  public static class PunchSession extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public PunchSession() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class SortCode extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public SortCode() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Time extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public Time() {
    }
  }
}
