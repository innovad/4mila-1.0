package com.rtiming.shared.ecard;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public abstract class AbstractECardSearchBoxData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractECardSearchBoxData() {
  }

  public ECardType getECardType() {
    return getFieldByClass(ECardType.class);
  }

  public Number getNumber() {
    return getFieldByClass(Number.class);
  }

  public RentalCardGroup getRentalCardGroup() {
    return getFieldByClass(RentalCardGroup.class);
  }

  public static class ECardType extends AbstractValueFieldData<Long[]> {
    private static final long serialVersionUID = 1L;

    public ECardType() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Number extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Number() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class RentalCardGroup extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public RentalCardGroup() {
    }
  }
}
