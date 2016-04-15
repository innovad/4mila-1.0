package com.rtiming.client.entry.startblock;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractCodeBox;
import com.rtiming.client.entry.startblock.StartblockForm.MainBox.CancelButton;
import com.rtiming.client.entry.startblock.StartblockForm.MainBox.CodeBox;
import com.rtiming.client.entry.startblock.StartblockForm.MainBox.OkButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateStartblockPermission;
import com.rtiming.shared.entry.startblock.IStartblockProcessService;
import com.rtiming.shared.entry.startblock.StartblockFormData;

@FormData(value = StartblockFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class StartblockForm extends AbstractForm {

  private Long startblockUid;

  public StartblockForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Startblock");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getStartblockUid() {
    return startblockUid;
  }

  @FormData
  public void setStartblockUid(Long startblockNr) {
    this.startblockUid = startblockNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new StartblockForm.ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new StartblockForm.NewHandler());
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public CodeBox getCodeBox() {
    return getFieldByClass(CodeBox.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class CodeBox extends AbstractCodeBox {

      @Override
      protected boolean getConfiguredBorderVisible() {
        return false;
      }
    }

    @Order(18.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(20.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(30.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IStartblockProcessService service = BEANS.get(IStartblockProcessService.class);
      StartblockFormData formData = new StartblockFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateStartblockPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IStartblockProcessService service = BEANS.get(IStartblockProcessService.class);
      StartblockFormData formData = new StartblockFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IStartblockProcessService service = BEANS.get(IStartblockProcessService.class);
      StartblockFormData formData = new StartblockFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IStartblockProcessService service = BEANS.get(IStartblockProcessService.class);
      StartblockFormData formData = new StartblockFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }
}
