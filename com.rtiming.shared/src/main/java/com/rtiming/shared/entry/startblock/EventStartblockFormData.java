package com.rtiming.shared.entry.startblock;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.entry.startblock.EventStartblockForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class EventStartblockFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

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

  public SortCode getSortCode() {
    return getFieldByClass(SortCode.class);
  }

  public StartblockUid getStartblockUid() {
    return getFieldByClass(StartblockUid.class);
  }

  public static class EventNrProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class SortCode extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class StartblockUid extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }
}
