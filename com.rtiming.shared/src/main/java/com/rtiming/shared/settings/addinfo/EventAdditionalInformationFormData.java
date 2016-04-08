package com.rtiming.shared.settings.addinfo;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public class EventAdditionalInformationFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public EventAdditionalInformationFormData() {
  }

  public AdditionalInformation getAdditionalInformation() {
    return getFieldByClass(AdditionalInformation.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public static class AdditionalInformation extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public AdditionalInformation() {
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
}
