package com.rtiming.client.entry;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractDefaultDateTimeField;
import com.rtiming.client.entry.RegistrationForm.MainBox.CancelButton;
import com.rtiming.client.entry.RegistrationForm.MainBox.EvtRegistrationField;
import com.rtiming.client.entry.RegistrationForm.MainBox.OkButton;
import com.rtiming.client.entry.RegistrationForm.MainBox.RegistrationNoField;
import com.rtiming.client.entry.RegistrationForm.MainBox.StartlistSettingOptionGroupBox;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateRegistrationPermission;
import com.rtiming.shared.entry.IRegistrationProcessService;
import com.rtiming.shared.entry.RegistrationFormData;

@FormData(value = RegistrationFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class RegistrationForm extends AbstractForm {

  private Long registrationNr;

  public RegistrationForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Registration");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getRegistrationNr() {
    return registrationNr;
  }

  @FormData
  public void setRegistrationNr(Long registrationNr) {
    this.registrationNr = registrationNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new RegistrationForm.ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new RegistrationForm.NewHandler());
  }

  public EvtRegistrationField getEvtRegistrationField() {
    return getFieldByClass(EvtRegistrationField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public RegistrationNoField getRegistrationNoField() {
    return getFieldByClass(RegistrationNoField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public StartlistSettingOptionGroupBox getStartlistSettingOptionGroupBox() {
    return getFieldByClass(StartlistSettingOptionGroupBox.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class RegistrationNoField extends AbstractStringField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Number");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(20.0)
    public class EvtRegistrationField extends AbstractDefaultDateTimeField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Date");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(30.0)
    public class StartlistSettingOptionGroupBox extends AbstractStartlistSettingOptionGroupBox {
    }

    @Order(38.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(40.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(50.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IRegistrationProcessService service = BEANS.get(IRegistrationProcessService.class);
      RegistrationFormData formData = new RegistrationFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateRegistrationPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IRegistrationProcessService service = BEANS.get(IRegistrationProcessService.class);
      RegistrationFormData formData = new RegistrationFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IRegistrationProcessService service = BEANS.get(IRegistrationProcessService.class);
      RegistrationFormData formData = new RegistrationFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      // required since all fields are pre-filled
      touch();
    }

    @Override
    public void execStore() throws ProcessingException {
      IRegistrationProcessService service = BEANS.get(IRegistrationProcessService.class);
      RegistrationFormData formData = new RegistrationFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }
}
