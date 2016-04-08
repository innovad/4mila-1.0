package com.rtiming.shared.event;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class EventFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public EventFormData() {
  }

  public ClientNrProperty getClientNrProperty() {
    return getPropertyByClass(ClientNrProperty.class);
  }

  /**
   * access method for property ClientNr.
   */
  public Long getClientNr() {
    return getClientNrProperty().getValue();
  }

  /**
   * access method for property ClientNr.
   */
  public void setClientNr(Long clientNr) {
    getClientNrProperty().setValue(clientNr);
  }

  public EventNrProperty getEventNrProperty() {
    return getPropertyByClass(EventNrProperty.class);
  }

  /**
   * access method for property EventNr.
   */
  public Long getEventNr() {
    return getEventNrProperty().getValue();
  }

  /**
   * access method for property EventNr.
   */
  public void setEventNr(Long eventNr) {
    getEventNrProperty().setValue(eventNr);
  }

  public EvtLastUploadProperty getEvtLastUploadProperty() {
    return getPropertyByClass(EvtLastUploadProperty.class);
  }

  /**
   * access method for property EvtLastUpload.
   */
  public Date getEvtLastUpload() {
    return getEvtLastUploadProperty().getValue();
  }

  /**
   * access method for property EvtLastUpload.
   */
  public void setEvtLastUpload(Date evtLastUpload) {
    getEvtLastUploadProperty().setValue(evtLastUpload);
  }

  public FormatProperty getFormatProperty() {
    return getPropertyByClass(FormatProperty.class);
  }

  /**
   * access method for property Format.
   */
  public String getFormat() {
    return getFormatProperty().getValue();
  }

  /**
   * access method for property Format.
   */
  public void setFormat(String format) {
    getFormatProperty().setValue(format);
  }

  public LogoDataProperty getLogoDataProperty() {
    return getPropertyByClass(LogoDataProperty.class);
  }

  /**
   * access method for property LogoData.
   */
  public byte[] getLogoData() {
    return getLogoDataProperty().getValue();
  }

  /**
   * access method for property LogoData.
   */
  public void setLogoData(byte[] logoData) {
    getLogoDataProperty().setValue(logoData);
  }

  public EventType getEventType() {
    return getFieldByClass(EventType.class);
  }

  public FinishTime getFinishTime() {
    return getFieldByClass(FinishTime.class);
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

  public PunchingSystem getPunchingSystem() {
    return getFieldByClass(PunchingSystem.class);
  }

  public Timezone getTimezone() {
    return getFieldByClass(Timezone.class);
  }

  public ZeroTime getZeroTime() {
    return getFieldByClass(ZeroTime.class);
  }

  public class ClientNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public ClientNrProperty() {
    }
  }

  public class EventNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public EventNrProperty() {
    }
  }

  public class EvtLastUploadProperty extends AbstractPropertyData<Date> {
    private static final long serialVersionUID = 1L;

    public EvtLastUploadProperty() {
    }
  }

  public class FormatProperty extends AbstractPropertyData<String> {
    private static final long serialVersionUID = 1L;

    public FormatProperty() {
    }
  }

  public class LogoDataProperty extends AbstractPropertyData<byte[]> {
    private static final long serialVersionUID = 1L;

    public LogoDataProperty() {
    }
  }

  public static class EventType extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public EventType() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class FinishTime extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public FinishTime() {
    }

    /**
     * list of derived validation rules.
     */

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

  public static class PunchingSystem extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public PunchingSystem() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class Timezone extends AbstractValueFieldData<Integer> {
    private static final long serialVersionUID = 1L;

    public Timezone() {
    }

    /**
     * list of derived validation rules.
     */
  }

  public static class ZeroTime extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public ZeroTime() {
    }

    /**
     * list of derived validation rules.
     */

  }
}
