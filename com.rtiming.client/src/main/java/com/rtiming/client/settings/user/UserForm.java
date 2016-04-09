package com.rtiming.client.settings.user;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractMinStringField;
import com.rtiming.client.settings.user.UserForm.MainBox.CancelButton;
import com.rtiming.client.settings.user.UserForm.MainBox.LanguageField;
import com.rtiming.client.settings.user.UserForm.MainBox.OkButton;
import com.rtiming.client.settings.user.UserForm.MainBox.PasswordField;
import com.rtiming.client.settings.user.UserForm.MainBox.RepeatPasswordField;
import com.rtiming.client.settings.user.UserForm.MainBox.RolesField;
import com.rtiming.client.settings.user.UserForm.MainBox.UsernameField;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateUserPermission;
import com.rtiming.shared.settings.user.IUserProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;
import com.rtiming.shared.settings.user.RoleCodeType;
import com.rtiming.shared.settings.user.UserFormData;

@FormData(value = UserFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class UserForm extends AbstractForm {

  private Long userNr;
  private boolean m_updatePassword;

  public UserForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("User");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getUserNr() {
    return userNr;
  }

  @FormData
  public void setUserNr(Long userNr) {
    this.userNr = userNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new UserForm.ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new UserForm.NewHandler());
  }

  public LanguageField getLanguageField() {
    return getFieldByClass(LanguageField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public PasswordField getPasswordField() {
    return getFieldByClass(PasswordField.class);
  }

  public RepeatPasswordField getRepeatPasswordField() {
    return getFieldByClass(RepeatPasswordField.class);
  }

  public RolesField getRolesField() {
    return getFieldByClass(RolesField.class);
  }

  public UsernameField getUsernameField() {
    return getFieldByClass(UsernameField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
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
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }

      @Override
      protected int getConfiguredMinLength() {
        return 3;
      }

    }

    @Order(20.0)
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
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 60;
      }

      @Override
      protected int getConfiguredMinLength() {
        return 3;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        setUpdatePassword(CompareUtility.notEquals(getInitValue(), getValue()));
        super.execChangedValue();
      }

    }

    @Order(30.0)
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
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 60;
      }
    }

    @Order(40.0)
    public class LanguageField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Language");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return LanguageCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(50.0)
    public class RolesField extends AbstractListBox<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return RoleCodeType.class;
      }

      @Override
      protected int getConfiguredGridH() {
        return 4;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Roles");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(58.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(60.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(70.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IUserProcessService service = BEANS.get(IUserProcessService.class);
      UserFormData formData = new UserFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateUserPermission());

      getUsernameField().setEnabled(!FMilaUtility.ADMIN_USER.equalsIgnoreCase(getUsernameField().getValue()));
    }

    @Override
    public void execStore() throws ProcessingException {
      IUserProcessService service = BEANS.get(IUserProcessService.class);
      UserFormData formData = new UserFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IUserProcessService service = BEANS.get(IUserProcessService.class);
      UserFormData formData = new UserFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IUserProcessService service = BEANS.get(IUserProcessService.class);
      UserFormData formData = new UserFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }

  @Override
  public void validateForm() throws ProcessingException {
    if (CompareUtility.notEquals(getPasswordField().getValue(), getRepeatPasswordField().getValue())) {
      throw new VetoException(Texts.get("PasswordsDoNotMatch"));
    }
    super.validateForm();
  }

  @FormData
  public boolean isUpdatePassword() {
    return m_updatePassword;
  }

  @FormData
  public void setUpdatePassword(boolean updatePassword) {
    m_updatePassword = updatePassword;
  }
}
