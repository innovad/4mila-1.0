package com.rtiming.shared.settings.user;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;


public class UserFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public UserFormData() {
  }

  public UpdatePasswordProperty getUpdatePasswordProperty() {
    return getPropertyByClass(UpdatePasswordProperty.class);
  }

  /**
   * access method for property UpdatePassword.
   */
  public boolean isUpdatePassword() {
    return (getUpdatePasswordProperty().getValue() == null) ? (false) : (getUpdatePasswordProperty().getValue());
  }

  /**
   * access method for property UpdatePassword.
   */
  public void setUpdatePassword(boolean updatePassword) {
    getUpdatePasswordProperty().setValue(updatePassword);
  }

  public UserNrProperty getUserNrProperty() {
    return getPropertyByClass(UserNrProperty.class);
  }

  /**
   * access method for property UserNr.
   */
  public Long getUserNr() {
    return getUserNrProperty().getValue();
  }

  /**
   * access method for property UserNr.
   */
  public void setUserNr(Long userNr) {
    getUserNrProperty().setValue(userNr);
  }

  public Language getLanguage() {
    return getFieldByClass(Language.class);
  }

  public Password getPassword() {
    return getFieldByClass(Password.class);
  }

  public RepeatPassword getRepeatPassword() {
    return getFieldByClass(RepeatPassword.class);
  }

  public Roles getRoles() {
    return getFieldByClass(Roles.class);
  }

  public Username getUsername() {
    return getFieldByClass(Username.class);
  }

  public class UpdatePasswordProperty extends AbstractPropertyData<Boolean> {
    private static final long serialVersionUID = 1L;

    public UpdatePasswordProperty() {
    }
  }

  public class UserNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public UserNrProperty() {
    }
  }

  public static class Language extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Language() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Password extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Password() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class RepeatPassword extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public RepeatPassword() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Roles extends AbstractValueFieldData<Long[]> {
    private static final long serialVersionUID = 1L;

    public Roles() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Username extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Username() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}