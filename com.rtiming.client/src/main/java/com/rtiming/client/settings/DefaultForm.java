package com.rtiming.client.settings;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.settings.DefaultForm.MainBox.CancelButton;
import com.rtiming.client.settings.DefaultForm.MainBox.DefaultUidField;
import com.rtiming.client.settings.DefaultForm.MainBox.OkButton;
import com.rtiming.client.settings.DefaultForm.MainBox.ValueIntegerField;
import com.rtiming.client.settings.DefaultForm.MainBox.ValueStringField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateDefaultPermission;
import com.rtiming.shared.settings.DefaultCodeType;
import com.rtiming.shared.settings.DefaultFormData;
import com.rtiming.shared.settings.IDefaultProcessService;

@FormData(value = DefaultFormData.class, sdkCommand = SdkCommand.CREATE)
public class DefaultForm extends AbstractForm {

  public DefaultForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Settings");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public DefaultUidField getDefaultUidField() {
    return getFieldByClass(DefaultUidField.class);
  }

  public ValueIntegerField getValueIntegerField() {
    return getFieldByClass(ValueIntegerField.class);
  }

  public ValueStringField getValueStringField() {
    return getFieldByClass(ValueStringField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class DefaultUidField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Setting");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return DefaultCodeType.class;
      }
    }

    @Order(20.0)
    public class ValueIntegerField extends AbstractLongField {
    }

    @Order(30.0)
    public class ValueStringField extends AbstractStringField {
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
      IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);
      DefaultFormData formData = new DefaultFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateDefaultPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);
      DefaultFormData formData = new DefaultFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);
      DefaultFormData formData = new DefaultFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);
      DefaultFormData formData = new DefaultFormData();
      exportFormData(formData);
      formData = service.create(formData);
    }
  }
}
