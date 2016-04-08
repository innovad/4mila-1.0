package com.rtiming.client.settings.clazz;

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
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.settings.clazz.AgeForm.MainBox.CancelButton;
import com.rtiming.client.settings.clazz.AgeForm.MainBox.ClazzField;
import com.rtiming.client.settings.clazz.AgeForm.MainBox.FromField;
import com.rtiming.client.settings.clazz.AgeForm.MainBox.OkButton;
import com.rtiming.client.settings.clazz.AgeForm.MainBox.SexField;
import com.rtiming.client.settings.clazz.AgeForm.MainBox.ToField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.security.permission.UpdateAgePermission;
import com.rtiming.shared.dao.RtClassAgeKey;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.clazz.AgeFormData;
import com.rtiming.shared.settings.clazz.IAgeProcessService;

@FormData(value = AgeFormData.class, sdkCommand = SdkCommand.CREATE)
public class AgeForm extends AbstractForm {

  private RtClassAgeKey key;

  public AgeForm() throws ProcessingException {
    super();
  }

  @FormData
  public RtClassAgeKey getKey() {
    return key;
  }

  @FormData
  public void setKey(RtClassAgeKey key) {
    this.key = key;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Age");
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

  public ClazzField getClazzField() {
    return getFieldByClass(ClazzField.class);
  }

  public FromField getFromField() {
    return getFieldByClass(FromField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public SexField getSexField() {
    return getFieldByClass(SexField.class);
  }

  public ToField getToField() {
    return getFieldByClass(ToField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class ClazzField extends AbstractSmartField<Long> {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Class");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return ClassCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(20.0)
    public class SexField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Sex");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return SexCodeType.class;
      }
    }

    @Order(30.0)
    public class FromField extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("From");
      }

      @Override
      protected Long getConfiguredMaxValue() {
        return 199L;
      }

      @Override
      protected Long getConfiguredMinValue() {
        return 0L;
      }
    }

    @Order(40.0)
    public class ToField extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("To");
      }

      @Override
      protected Long getConfiguredMaxValue() {
        return 199L;
      }

      @Override
      protected Long getConfiguredMinValue() {
        return 0L;
      }
    }

    @Order(48.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(50.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(60.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IAgeProcessService service = BEANS.get(IAgeProcessService.class);
      AgeFormData formData = new AgeFormData();
      exportFormData(formData);
      formData = BeanUtility.ageBean2FormData(service.load(formData.getKey()));
      importFormData(formData);
      setEnabledPermission(new UpdateAgePermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IAgeProcessService service = BEANS.get(IAgeProcessService.class);
      AgeFormData formData = new AgeFormData();
      exportFormData(formData);
      formData = BeanUtility.ageBean2FormData(service.store(BeanUtility.ageFormData2bean(formData)));
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IAgeProcessService service = BEANS.get(IAgeProcessService.class);
      AgeFormData formData = new AgeFormData();
      exportFormData(formData);
      formData = BeanUtility.ageBean2FormData(service.prepareCreate(BeanUtility.ageFormData2bean(formData)));
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IAgeProcessService service = BEANS.get(IAgeProcessService.class);
      AgeFormData formData = new AgeFormData();
      exportFormData(formData);
      formData = BeanUtility.ageBean2FormData(service.create(BeanUtility.ageFormData2bean(formData)));
      importFormData(formData);
    }
  }
}
