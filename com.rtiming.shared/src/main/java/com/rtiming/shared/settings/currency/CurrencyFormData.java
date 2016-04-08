package com.rtiming.shared.settings.currency;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.common.AbstractCodeBoxData;

public class CurrencyFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public CurrencyFormData() {
  }

  public CurrencyUidProperty getCurrencyUidProperty() {
    return getPropertyByClass(CurrencyUidProperty.class);
  }

  /**
   * access method for property CurrencyUid.
   */
  public Long getCurrencyUid() {
    return getCurrencyUidProperty().getValue();
  }

  /**
   * access method for property CurrencyUid.
   */
  public void setCurrencyUid(Long currencyUid) {
    getCurrencyUidProperty().setValue(currencyUid);
  }

  public CodeBox getCodeBox() {
    return getFieldByClass(CodeBox.class);
  }

  public ExchangeRate getExchangeRate() {
    return getFieldByClass(ExchangeRate.class);
  }

  public class CurrencyUidProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public CurrencyUidProperty() {
    }
  }

  public static class CodeBox extends AbstractCodeBoxData {
    private static final long serialVersionUID = 1L;

    public CodeBox() {
    }
  }

  public static class ExchangeRate extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public ExchangeRate() {
    }

    /**
     * list of derived validation rules.
     */
 
  }
}
