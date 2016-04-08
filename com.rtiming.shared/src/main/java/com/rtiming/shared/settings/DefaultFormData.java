package com.rtiming.shared.settings;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public class DefaultFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public DefaultFormData() {
  }

  public DefaultUid getDefaultUid() {
    return getFieldByClass(DefaultUid.class);
  }

  public ValueInteger getValueInteger() {
    return getFieldByClass(ValueInteger.class);
  }

  public ValueString getValueString() {
    return getFieldByClass(ValueString.class);
  }

  public static class DefaultUid extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public DefaultUid() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class ValueInteger extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ValueInteger() {
    }
  }

  public static class ValueString extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public ValueString() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
