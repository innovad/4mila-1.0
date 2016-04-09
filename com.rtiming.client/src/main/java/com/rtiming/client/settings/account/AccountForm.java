package com.rtiming.client.settings.account;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.labelfield.AbstractLabelField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.ui.fields.AbstractEmailField;
import com.rtiming.client.common.ui.fields.AbstractMinStringField;
import com.rtiming.client.common.ui.fields.AbstractTrimmedStringField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.AccountInfoField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.EMailField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.FirstNameField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.LastNameField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.NameInfoField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.PasswordField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.PasswordInfoField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.RepeatPasswordField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.UsernameField;
import com.rtiming.client.settings.account.AccountForm.MainBox.CancelButton;
import com.rtiming.client.settings.account.AccountForm.MainBox.OkButton;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.ILocalAccountService;
import com.rtiming.shared.settings.account.AccountFormData;
import com.rtiming.shared.settings.account.IAccountProcessService;

@FormData(value = AccountFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class AccountForm extends AbstractForm {

  private Long accountNr;
  private boolean m_updatePassword;
  private Long m_clientNr;

  public AccountForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Account");
  }

  public AccountBox getAccountBox() {
    return getFieldByClass(AccountBox.class);
  }

  public AccountInfoField getAccountInfoField() {
    return getFieldByClass(AccountInfoField.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getAccountNr() {
    return accountNr;
  }

  @FormData
  public void setAccountNr(Long userNr) {
    this.accountNr = userNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new AccountForm.ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new AccountForm.NewHandler());
  }

  public EMailField getEMailField() {
    return getFieldByClass(EMailField.class);
  }

  public FirstNameField getFirstNameField() {
    return getFieldByClass(FirstNameField.class);
  }

  public LastNameField getLastNameField() {
    return getFieldByClass(LastNameField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NameInfoField getNameInfoField() {
    return getFieldByClass(NameInfoField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public PasswordField getPasswordField() {
    return getFieldByClass(PasswordField.class);
  }

  public PasswordInfoField getPasswordInfoField() {
    return getFieldByClass(PasswordInfoField.class);
  }

  public RepeatPasswordField getRepeatPasswordField() {
    return getFieldByClass(RepeatPasswordField.class);
  }

  public UsernameField getUsernameField() {
    return getFieldByClass(UsernameField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(70.0)
    public class AccountBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("CreateNewAccount");
      }

      @Order(10.0)
      public class AccountInfoField extends AbstractLabelField {

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected boolean getConfiguredLabelVisible() {
          return false;
        }

        @Override
        protected void execInitField() throws ProcessingException {
          setValue(TEXTS.get("NewAccountUsernameInfo"));
        }
      }

      @Order(20.0)
      public class EMailField extends AbstractEmailField {

        @Override
        protected int getConfiguredLabelPosition() {
          return LABEL_POSITION_TOP;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

      }

      @Order(30.0)
      public class UsernameField extends AbstractMinStringField {

        @Override
        protected boolean getConfiguredFormatLower() {
          return true;
        }

        @Override
        protected String getConfiguredLabel() {
          return ScoutTexts.get("Username");
        }

        @Override
        protected int getConfiguredLabelPosition() {
          return LABEL_POSITION_TOP;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 250;
        }

      }

      @Order(40.0)
      public class PasswordInfoField extends AbstractLabelField {

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected boolean getConfiguredLabelVisible() {
          return false;
        }

        @Override
        protected void execInitField() throws ProcessingException {
          setValue(TEXTS.get("NewAccountPasswordInfo"));
        }

      }

      @Order(50.0)
      public class PasswordField extends AbstractMinStringField {

        @Override
        protected boolean getConfiguredInputMasked() {
          return true;
        }

        @Override
        protected String getConfiguredLabel() {
          return ScoutTexts.get("Password");
        }

        @Override
        protected int getConfiguredLabelPosition() {
          return LABEL_POSITION_TOP;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 250;
        }

        @Override
        protected int getConfiguredMinLength() {
          return 7;
        }

        @Override
        protected void execChangedValue() throws ProcessingException {
          setUpdatePassword(CompareUtility.notEquals(getInitValue(), getValue()));
          super.execChangedValue();
        }

      }

      @Order(60.0)
      public class RepeatPasswordField extends AbstractStringField {

        @Override
        protected boolean getConfiguredInputMasked() {
          return true;
        }

        @Override
        protected String getConfiguredLabel() {
          return ScoutTexts.get("RepeatPassword");
        }

        @Override
        protected int getConfiguredLabelPosition() {
          return LABEL_POSITION_TOP;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 250;
        }
      }

      @Order(70.0)
      public class NameInfoField extends AbstractLabelField {

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }
      }

      @Order(80.0)
      public class LastNameField extends AbstractTrimmedStringField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("LastName");
        }

        @Override
        protected int getConfiguredLabelPosition() {
          return LABEL_POSITION_TOP;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 250;
        }
      }

      @Order(90.0)
      public class FirstNameField extends AbstractTrimmedStringField {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("FirstName");
        }

        @Override
        protected int getConfiguredLabelPosition() {
          return LABEL_POSITION_TOP;
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 250;
        }
      }
    }

    @Order(90.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(100.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IAccountProcessService service = BEANS.get(IAccountProcessService.class);
      AccountFormData formData = new AccountFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IAccountProcessService service = BEANS.get(IAccountProcessService.class);
      AccountFormData formData = new AccountFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IAccountProcessService service = BEANS.get(IAccountProcessService.class);
      AccountFormData formData = new AccountFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      // do not call store locally, connect to online server instead
      ILocalAccountService localService = BEANS.get(ILocalAccountService.class);
      IAccountProcessService globalService = BEANS.get(IAccountProcessService.class);
      AccountFormData formData = new AccountFormData();
      exportFormData(formData);
      if (FMilaClientUtility.isTestEnvironment() ||
          (FMilaUtility.isWebClient() && FMilaClientUtility.isAdminUser())) {
        // on global web client
        formData = globalService.create(formData, false);
      }
      else {
        // on local rich client
        formData = localService.createOnline(formData);
        localService.createLocalClient(formData.getClientNr(), true);
      }
      importFormData(formData);
    }
  }

  @FormData
  public boolean isUpdatePassword() {
    return m_updatePassword;
  }

  @FormData
  public void setUpdatePassword(boolean updatePassword) {
    m_updatePassword = updatePassword;
  }

  @Override
  public void validateForm() throws ProcessingException {
    if (CompareUtility.notEquals(getPasswordField().getValue(), getRepeatPasswordField().getValue())) {
      getPasswordField().requestFocus();
      throw new VetoException(Texts.get("PasswordsDoNotMatch"));
    }
    super.validateForm();
  }

  @FormData
  public Long getClientNr() {
    return m_clientNr;
  }

  @FormData
  public void setClientNr(Long clientNr) {
    m_clientNr = clientNr;
  }

}
