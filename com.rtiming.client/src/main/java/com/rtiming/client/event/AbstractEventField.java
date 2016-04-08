package com.rtiming.client.event;

import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.shared.Texts;
import com.rtiming.shared.event.EventLookupCall;

public abstract class AbstractEventField extends AbstractSmartField<Long> {

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Event");
  }

  @Override
  protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
    return EventLookupCall.class;
  }

}
