package com.rtiming.shared.entry;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class ParticipationFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public ParticipationFormData() {
  }

  public BaseBibNumberProperty getBaseBibNumberProperty() {
    return getPropertyByClass(BaseBibNumberProperty.class);
  }

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

  public EntryNrProperty getEntryNrProperty() {
    return getPropertyByClass(EntryNrProperty.class);
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

  public RawStartTimeProperty getRawStartTimeProperty() {
    return getPropertyByClass(RawStartTimeProperty.class);
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

  public class BaseBibNumberProperty extends AbstractPropertyData<String> {
    private static final long serialVersionUID = 1L;

    public BaseBibNumberProperty() {
    }
  }

  public class EntryNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public EntryNrProperty() {
    }
  }

  public class EventNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public EventNrProperty() {
    }
  }

  public class RawStartTimeProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RawStartTimeProperty() {
    }
  }
}
