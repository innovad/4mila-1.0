package com.rtiming.shared.runner;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.runner.AbstractRunnerSearchBox", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public abstract class AbstractRunnerSearchBoxData extends AbstractFormFieldData {

  private static final long serialVersionUID = 1L;

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
  }

  public static class ExtKey extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class Name extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }
}
