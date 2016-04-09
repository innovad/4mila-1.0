package com.rtiming.client.settings.fee;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractDefaultDateTimeField;
import com.rtiming.client.settings.fee.FeeForm.MainBox.AgeBox;
import com.rtiming.client.settings.fee.FeeForm.MainBox.AgeBox.AgeFrom;
import com.rtiming.client.settings.fee.FeeForm.MainBox.AgeBox.AgeTo;
import com.rtiming.client.settings.fee.FeeForm.MainBox.CancelButton;
import com.rtiming.client.settings.fee.FeeForm.MainBox.CurrencyField;
import com.rtiming.client.settings.fee.FeeForm.MainBox.DateBox;
import com.rtiming.client.settings.fee.FeeForm.MainBox.DateBox.DateFrom;
import com.rtiming.client.settings.fee.FeeForm.MainBox.DateBox.DateTo;
import com.rtiming.client.settings.fee.FeeForm.MainBox.FeeField;
import com.rtiming.client.settings.fee.FeeForm.MainBox.OkButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateFeePermission;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.fee.FeeFormData;
import com.rtiming.shared.settings.fee.IFeeProcessService;

@FormData(value = FeeFormData.class, sdkCommand = SdkCommand.CREATE)
public class FeeForm extends AbstractForm {

  private Long feeNr;
  private Long feeGroupNr;
  private boolean m_cashPaymentOnRegistration;
  private Long m_eventNr;
  private Long m_classUid;
  private Long m_additionalInformationUid;

  public FeeForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Fee");
  }

  @FormData
  public Long getFeeGroupNr() {
    return feeGroupNr;
  }

  @FormData
  public void setFeeGroupNr(Long feeGroupNr) {
    this.feeGroupNr = feeGroupNr;
  }

  @FormData
  public Long getFeeNr() {
    return feeNr;
  }

  @FormData
  public void setFeeNr(Long feeNr) {
    this.feeNr = feeNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public AgeBox getAgeBox() {
    return getFieldByClass(AgeBox.class);
  }

  public AgeFrom getAgeFrom() {
    return getFieldByClass(AgeFrom.class);
  }

  public AgeTo getAgeTo() {
    return getFieldByClass(AgeTo.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public CurrencyField getCurrencyField() {
    return getFieldByClass(CurrencyField.class);
  }

  public DateBox getDateBox() {
    return getFieldByClass(DateBox.class);
  }

  public DateFrom getDateFrom() {
    return getFieldByClass(DateFrom.class);
  }

  public DateTo getDateTo() {
    return getFieldByClass(DateTo.class);
  }

  public FeeField getFeeField() {
    return getFieldByClass(FeeField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class FeeField extends AbstractBigDecimalField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Fee");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected BigDecimal getConfiguredMaxValue() {
        return BigDecimal.valueOf(9.99999999999E11);
      }

      @Override
      protected BigDecimal getConfiguredMinValue() {
        return BigDecimal.valueOf(-9.99999999999E11);
      }

    }

    @Order(20.0)
    public class CurrencyField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Currency");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return CurrencyCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(30.0)
    public class DateBox extends AbstractSequenceBox {

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Date");
      }

      @Order(10.0)
      public class DateFrom extends AbstractDefaultDateTimeField {

        @Override
        protected String getConfiguredLabel() {
          return ScoutTexts.get("from");
        }
      }

      @Order(20.0)
      public class DateTo extends AbstractDefaultDateTimeField {

        @Override
        protected String getConfiguredLabel() {
          return ScoutTexts.get("to");
        }
      }
    }

    @Order(40.0)
    public class AgeBox extends AbstractSequenceBox {

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Age");
      }

      @Order(10.0)
      public class AgeFrom extends AbstractLongField {

        @Override
        protected String getConfiguredLabel() {
          return ScoutTexts.get("from");
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

      @Order(20.0)
      public class AgeTo extends AbstractLongField {

        @Override
        protected String getConfiguredLabel() {
          return ScoutTexts.get("to");
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
      IFeeProcessService service = BEANS.get(IFeeProcessService.class);
      FeeFormData formData = new FeeFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateFeePermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IFeeProcessService service = BEANS.get(IFeeProcessService.class);
      FeeFormData formData = new FeeFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IFeeProcessService service = BEANS.get(IFeeProcessService.class);
      FeeFormData formData = new FeeFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IFeeProcessService service = BEANS.get(IFeeProcessService.class);
      FeeFormData formData = new FeeFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }

  @FormData
  public boolean isCashPaymentOnRegistration() {
    return m_cashPaymentOnRegistration;
  }

  @FormData
  public void setCashPaymentOnRegistration(boolean cashPaymentOnRegistration) {
    m_cashPaymentOnRegistration = cashPaymentOnRegistration;
  }

  @FormData
  public Long getEventNr() {
    return m_eventNr;
  }

  @FormData
  public void setEventNr(Long eventNr) {
    m_eventNr = eventNr;
  }

  @FormData
  public Long getClassUid() {
    return m_classUid;
  }

  @FormData
  public void setClassUid(Long classUid) {
    m_classUid = classUid;
  }

  @FormData
  public Long getAdditionalInformationUid() {
    return m_additionalInformationUid;
  }

  @FormData
  public void setAdditionalInformationUid(Long additionalInformationUid) {
    m_additionalInformationUid = additionalInformationUid;
  }
}
