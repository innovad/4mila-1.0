package com.rtiming.shared.entry.startblock;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class StartblockSelectionFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public StartblockSelectionFormData() {
  }

  public EntryNrsProperty getEntryNrsProperty() {
    return getPropertyByClass(EntryNrsProperty.class);
  }

  /**
   * access method for property EntryNrs.
   */
  public Long[] getEntryNrs() {
    return getEntryNrsProperty().getValue();
  }

  /**
   * access method for property EntryNrs.
   */
  public void setEntryNrs(Long[] entryNrs) {
    getEntryNrsProperty().setValue(entryNrs);
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

  public Overwrite getOverwrite() {
    return getFieldByClass(Overwrite.class);
  }

  public StartblockUid getStartblockUid() {
    return getFieldByClass(StartblockUid.class);
  }

  public class EntryNrsProperty extends AbstractPropertyData<Long[]> {
    private static final long serialVersionUID = 1L;

    public EntryNrsProperty() {
    }
  }

  public class EventNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public EventNrProperty() {
    }
  }

  public static class Overwrite extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public Overwrite() {
    }
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
