package com.rtiming.client.ecard;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.ecard.ECardForm.MainBox.CancelButton;
import com.rtiming.client.ecard.ECardForm.MainBox.ECardTypeField;
import com.rtiming.client.ecard.ECardForm.MainBox.NumberField;
import com.rtiming.client.ecard.ECardForm.MainBox.OkButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.security.permission.UpdateECardPermission;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.ecard.ECardFormData;
import com.rtiming.shared.ecard.ECardTypeCodeType;
import com.rtiming.shared.ecard.ECardUtility;
import com.rtiming.shared.ecard.IECardProcessService;

@FormData(value = ECardFormData.class, sdkCommand = SdkCommand.CREATE)
public class ECardForm extends AbstractForm {

  private RtEcardKey eCardKey;

  public ECardForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("ECard");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public RtEcardKey getECardKey() {
    return eCardKey;
  }

  @FormData
  public void setECardKey(RtEcardKey nr) {
    this.eCardKey = nr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public ECardTypeField getECardTypeField() {
    return getFieldByClass(ECardTypeField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NumberField getNumberField() {
    return getFieldByClass(NumberField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class NumberField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Number");
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
      protected boolean getConfiguredUpdateDisplayTextOnModify() {
        return true;
      }

      @Override
      protected String execValidateValue(String rawValue) throws ProcessingException {
        if (rawValue != null) {
          getECardTypeField().setValue(ECardUtility.getType(rawValue));
        }
        return rawValue;
      }
    }

    @Order(20.0)
    public class ECardTypeField extends AbstractSmartField<Long> {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("ECardType");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return ECardTypeCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(30.0)
    public class RentalCardField extends AbstractBooleanField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("RentalCard");
      }
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
      IECardProcessService service = BEANS.get(IECardProcessService.class);
      ECardFormData formData = new ECardFormData();
      exportFormData(formData);
      formData = BeanUtility.eCardBean2FormData(service.load(formData.getECardKey()));
      importFormData(formData);
      setEnabledPermission(new UpdateECardPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IECardProcessService service = BEANS.get(IECardProcessService.class);
      ECardFormData formData = new ECardFormData();
      exportFormData(formData);
      formData = BeanUtility.eCardBean2FormData(service.store(BeanUtility.eCardFormData2bean(formData)));
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IECardProcessService service = BEANS.get(IECardProcessService.class);
      ECardFormData formData = new ECardFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IECardProcessService service = BEANS.get(IECardProcessService.class);
      ECardFormData formData = new ECardFormData();
      exportFormData(formData);
      formData = BeanUtility.eCardBean2FormData(service.create(BeanUtility.eCardFormData2bean(formData)));
      importFormData(formData);
    }
  }
}
