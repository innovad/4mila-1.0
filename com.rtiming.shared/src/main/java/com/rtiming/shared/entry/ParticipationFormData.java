package com.rtiming.shared.entry;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.entry.ParticipationForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class ParticipationFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  /**
   * access method for property BaseBibNumber.
   */
  public String getBaseBibNumber() {
    return getBaseBibNumberProperty().getValue();
  }

  /**
   * access method for property BaseBibNumber.
   */
  public void setBaseBibNumber(String baseBibNumber) {
    getBaseBibNumberProperty().setValue(baseBibNumber);
  }

  public BaseBibNumberProperty getBaseBibNumberProperty() {
    return getPropertyByClass(BaseBibNumberProperty.class);
  }

  /**
   * access method for property EntryNr.
   */
  public Long getEntryNr() {
    return getEntryNrProperty().getValue();
  }

  /**
   * access method for property EntryNr.
   */
  public void setEntryNr(Long entryNr) {
    getEntryNrProperty().setValue(entryNr);
  }

  public EntryNrProperty getEntryNrProperty() {
    return getPropertyByClass(EntryNrProperty.class);
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

  public EventNrProperty getEventNrProperty() {
    return getPropertyByClass(EventNrProperty.class);
  }

  /**
   * access method for property RawStartTime.
   */
  public Long getRawStartTime() {
    return getRawStartTimeProperty().getValue();
  }

  /**
   * access method for property RawStartTime.
   */
  public void setRawStartTime(Long rawStartTime) {
    getRawStartTimeProperty().setValue(rawStartTime);
  }

  public RawStartTimeProperty getRawStartTimeProperty() {
    return getPropertyByClass(RawStartTimeProperty.class);
  }

  public static class BaseBibNumberProperty extends AbstractPropertyData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class EntryNrProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class EventNrProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class RawStartTimeProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }
}
