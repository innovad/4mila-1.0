package com.rtiming.shared.settings.fee;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class FeeGroupFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public FeeGroupFormData() {
  }

  public FeeGroupNrProperty getFeeGroupNrProperty() {
    return getPropertyByClass(FeeGroupNrProperty.class);
  }

  /**
   * access method for property FeeGroupNr.
   */
  public Long getFeeGroupNr() {
    return getFeeGroupNrProperty().getValue();
  }

  /**
   * access method for property FeeGroupNr.
   */
  public void setFeeGroupNr(Long feeGroupNr) {
    getFeeGroupNrProperty().setValue(feeGroupNr);
  }

  public CashPaymentOnRegistration getCashPaymentOnRegistration() {
    return getFieldByClass(CashPaymentOnRegistration.class);
  }

  public Name getName() {
    return getFieldByClass(Name.class);
  }

  public class FeeGroupNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public FeeGroupNrProperty() {
    }
  }

  public static class CashPaymentOnRegistration extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public CashPaymentOnRegistration() {
    }
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
