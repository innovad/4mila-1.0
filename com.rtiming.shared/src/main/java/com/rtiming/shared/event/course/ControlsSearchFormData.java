package com.rtiming.shared.event.course;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public class ControlsSearchFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public ControlsSearchFormData() {
  }

  public ActiveGroup getActiveGroup() {
    return getFieldByClass(ActiveGroup.class);
  }

  public static class ActiveGroup extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public ActiveGroup() {
    }
  }
}
