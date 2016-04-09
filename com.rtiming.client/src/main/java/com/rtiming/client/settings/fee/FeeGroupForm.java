package com.rtiming.client.settings.fee;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.ScoutTexts;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.settings.fee.FeeGroupForm.MainBox.CancelButton;
import com.rtiming.client.settings.fee.FeeGroupForm.MainBox.CashPaymentOnRegistrationField;
import com.rtiming.client.settings.fee.FeeGroupForm.MainBox.NameField;
import com.rtiming.client.settings.fee.FeeGroupForm.MainBox.OkButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateFeeGroupPermission;
import com.rtiming.shared.settings.fee.FeeGroupFormData;
import com.rtiming.shared.settings.fee.IFeeGroupProcessService;

@FormData(value = FeeGroupFormData.class, sdkCommand = SdkCommand.CREATE)
public class FeeGroupForm extends AbstractForm {

  private Long feeGroupNr;

  public FeeGroupForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("FeeGroup");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getFeeGroupNr() {
    return feeGroupNr;
  }

  @FormData
  public void setFeeGroupNr(Long feeGroupNr) {
    this.feeGroupNr = feeGroupNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public CashPaymentOnRegistrationField getCashPaymentOnRegistrationField() {
    return getFieldByClass(CashPaymentOnRegistrationField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class NameField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("Name");
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

    @Order(20.0)
    public class CashPaymentOnRegistrationField extends AbstractBooleanField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("CashPaymentOnRegistration");
      }
    }

    @Order(28.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(30.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(40.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IFeeGroupProcessService service = BEANS.get(IFeeGroupProcessService.class);
      FeeGroupFormData formData = new FeeGroupFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateFeeGroupPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IFeeGroupProcessService service = BEANS.get(IFeeGroupProcessService.class);
      FeeGroupFormData formData = new FeeGroupFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IFeeGroupProcessService service = BEANS.get(IFeeGroupProcessService.class);
      FeeGroupFormData formData = new FeeGroupFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IFeeGroupProcessService service = BEANS.get(IFeeGroupProcessService.class);
      FeeGroupFormData formData = new FeeGroupFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }
}
