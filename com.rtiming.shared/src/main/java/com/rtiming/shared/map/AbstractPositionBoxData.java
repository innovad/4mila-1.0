package com.rtiming.shared.map;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public abstract class AbstractPositionBoxData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractPositionBoxData() {
  }

  public X getX() {
    return getFieldByClass(X.class);
  }

  public Y getY() {
    return getFieldByClass(Y.class);
  }

  public static class X extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public X() {
    }
  }

  public static class Y extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public Y() {
    }
  }
}
