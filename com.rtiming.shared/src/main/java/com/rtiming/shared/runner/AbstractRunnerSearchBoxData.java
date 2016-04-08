package com.rtiming.shared.runner;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public abstract class AbstractRunnerSearchBoxData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractRunnerSearchBoxData() {
  }

  public ActiveGroup getActiveGroup() {
    return getFieldByClass(ActiveGroup.class);
  }

  public ExtKey getExtKey() {
    return getFieldByClass(ExtKey.class);
  }

  public Name getName() {
    return getFieldByClass(Name.class);
  }

  public static class ActiveGroup extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public ActiveGroup() {
    }
  }

  public static class ExtKey extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public ExtKey() {
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
}
