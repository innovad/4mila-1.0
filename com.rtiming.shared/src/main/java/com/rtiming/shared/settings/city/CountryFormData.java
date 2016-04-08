package com.rtiming.shared.settings.city;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.common.AbstractCodeBoxData;

public class CountryFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public CountryFormData() {
  }

  public CountryUidProperty getCountryUidProperty() {
    return getPropertyByClass(CountryUidProperty.class);
  }

  /**
   * access method for property CountryUid.
   */
  public Long getCountryUid() {
    return getCountryUidProperty().getValue();
  }

  /**
   * access method for property CountryUid.
   */
  public void setCountryUid(Long countryUid) {
    getCountryUidProperty().setValue(countryUid);
  }

  public CodeBox getCodeBox() {
    return getFieldByClass(CodeBox.class);
  }

  public CountryCode getCountryCode() {
    return getFieldByClass(CountryCode.class);
  }

  public Nation getNation() {
    return getFieldByClass(Nation.class);
  }

  public class CountryUidProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public CountryUidProperty() {
    }
  }

  public static class CodeBox extends AbstractCodeBoxData {
    private static final long serialVersionUID = 1L;

    public CodeBox() {
    }
  }

  public static class CountryCode extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public CountryCode() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Nation extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Nation() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
