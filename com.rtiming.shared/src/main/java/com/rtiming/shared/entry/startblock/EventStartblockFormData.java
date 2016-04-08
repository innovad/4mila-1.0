package com.rtiming.shared.entry.startblock;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;


public class EventStartblockFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public EventStartblockFormData() {
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

  public SortCode getSortCode() {
    return getFieldByClass(SortCode.class);
  }

  public StartblockUid getStartblockUid() {
    return getFieldByClass(StartblockUid.class);
  }

  public class EventNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public EventNrProperty() {
    }
  }

  public static class SortCode extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public SortCode() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class StartblockUid extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public StartblockUid() {
    }

    /**
     * list of derived validation rules.
     */
 
  }
}
