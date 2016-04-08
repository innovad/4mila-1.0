package com.rtiming.shared.settings.account;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class AccountFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public AccountFormData() {
  }

  public AccountNrProperty getAccountNrProperty() {
    return getPropertyByClass(AccountNrProperty.class);
  }

  /**
   * access method for property AccountNr.
   */
  public Long getAccountNr() {
    return getAccountNrProperty().getValue();
  }

  /**
   * access method for property AccountNr.
   */
  public void setAccountNr(Long accountNr) {
    getAccountNrProperty().setValue(accountNr);
  }

  public ClientNrProperty getClientNrProperty() {
    return getPropertyByClass(ClientNrProperty.class);
  }

  /**
   * access method for property ClientNr.
   */
  public Long getClientNr() {
    return getClientNrProperty().getValue();
  }

  /**
   * access method for property ClientNr.
   */
  public void setClientNr(Long clientNr) {
    getClientNrProperty().setValue(clientNr);
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

  public AccountInfo getAccountInfo() {
    return getFieldByClass(AccountInfo.class);
  }

  public EMail getEMail() {
    return getFieldByClass(EMail.class);
  }

  public FirstName getFirstName() {
    return getFieldByClass(FirstName.class);
  }

  public LastName getLastName() {
    return getFieldByClass(LastName.class);
  }

  public NameInfo getNameInfo() {
    return getFieldByClass(NameInfo.class);
  }

  public Password getPassword() {
    return getFieldByClass(Password.class);
  }

  public PasswordInfo getPasswordInfo() {
    return getFieldByClass(PasswordInfo.class);
  }

  public RepeatPassword getRepeatPassword() {
    return getFieldByClass(RepeatPassword.class);
  }

  public Username getUsername() {
    return getFieldByClass(Username.class);
  }

  public class AccountNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public AccountNrProperty() {
    }
  }

  public class ClientNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public ClientNrProperty() {
    }
  }

  public class UpdatePasswordProperty extends AbstractPropertyData<Boolean> {
    private static final long serialVersionUID = 1L;

    public UpdatePasswordProperty() {
    }
  }

  public static class AccountInfo extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public AccountInfo() {
    }
  }

  public static class EMail extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public EMail() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class FirstName extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public FirstName() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class LastName extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public LastName() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class NameInfo extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public NameInfo() {
    }
  }

  public static class Password extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Password() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class PasswordInfo extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public PasswordInfo() {
    }
  }

  public static class RepeatPassword extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public RepeatPassword() {
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
