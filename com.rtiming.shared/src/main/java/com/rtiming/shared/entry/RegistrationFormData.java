package com.rtiming.shared.entry;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class RegistrationFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public RegistrationFormData() {
  }

  public RegistrationNrProperty getRegistrationNrProperty() {
    return getPropertyByClass(RegistrationNrProperty.class);
  }

  /**
   * access method for property RegistrationNr.
   */
  public Long getRegistrationNr() {
    return getRegistrationNrProperty().getValue();
  }

  /**
   * access method for property RegistrationNr.
   */
  public void setRegistrationNr(Long registrationNr) {
    getRegistrationNrProperty().setValue(registrationNr);
  }

  public EvtRegistration getEvtRegistration() {
    return getFieldByClass(EvtRegistration.class);
  }

  public RegistrationNo getRegistrationNo() {
    return getFieldByClass(RegistrationNo.class);
  }

  public StartlistSettingOptionGroupBox getStartlistSettingOptionGroupBox() {
    return getFieldByClass(StartlistSettingOptionGroupBox.class);
  }

  public class RegistrationNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RegistrationNrProperty() {
    }
  }

  public static class EvtRegistration extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public EvtRegistration() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class RegistrationNo extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public RegistrationNo() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class StartlistSettingOptionGroupBox extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public StartlistSettingOptionGroupBox() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
