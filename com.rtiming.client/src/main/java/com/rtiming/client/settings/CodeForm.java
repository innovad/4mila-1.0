package com.rtiming.client.settings;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractCodeBox;
import com.rtiming.client.common.ui.fields.AbstractCodeBox.ShortcutField;
import com.rtiming.client.settings.CodeForm.MainBox.CancelButton;
import com.rtiming.client.settings.CodeForm.MainBox.OkButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateCodePermission;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.city.AreaCodeType;
import com.rtiming.shared.settings.currency.CurrencyCodeType;

@FormData(value = CodeFormData.class, sdkCommand = SdkCommand.CREATE)
public class CodeForm extends AbstractForm {

  private Long codeUid;
  private Long m_codeType;

  public CodeForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Code");
  }

  @Override
  protected void execInitForm() throws ProcessingException {
    if (getCodeType() == ClassCodeType.ID) {
      getMainBox().getFieldByClass(ShortcutField.class).setEnabled(true);
      getMainBox().getFieldByClass(ShortcutField.class).setMandatory(true);
    }
    else if (getCodeType() == CurrencyCodeType.ID) {
      getMainBox().getFieldByClass(ShortcutField.class).setEnabled(true);
      getMainBox().getFieldByClass(ShortcutField.class).setMandatory(true);
    }
    else if (getCodeType() == AreaCodeType.ID) {
      getMainBox().getFieldByClass(ShortcutField.class).setEnabled(true);
      getMainBox().getFieldByClass(ShortcutField.class).setMandatory(false);
    }
    else {
      getMainBox().getFieldByClass(ShortcutField.class).setEnabled(false);
      getMainBox().getFieldByClass(ShortcutField.class).setMandatory(false);
    }
  }

  @FormData
  public Long getCodeType() {
    return m_codeType;
  }

  @FormData
  public void setCodeType(Long codeType) {
    m_codeType = codeType;
  }

  @FormData
  public Long getCodeUid() {
    return codeUid;
  }

  @FormData
  public void setCodeUid(Long codeNr) {
    this.codeUid = codeNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  @Order(10.0)
  @FormData(value = CodeFormData.class, sdkCommand = SdkCommand.CREATE)
  public class MainBox extends AbstractCodeBox {

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
      ICodeProcessService service = BEANS.get(ICodeProcessService.class);
      CodeFormData formData = new CodeFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateCodePermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      ICodeProcessService service = BEANS.get(ICodeProcessService.class);
      CodeFormData formData = new CodeFormData();
      exportFormData(formData);
      formData = service.store(formData);
      importFormData(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      ICodeProcessService service = BEANS.get(ICodeProcessService.class);
      CodeFormData formData = new CodeFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      ICodeProcessService service = BEANS.get(ICodeProcessService.class);
      CodeFormData formData = new CodeFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }
}
