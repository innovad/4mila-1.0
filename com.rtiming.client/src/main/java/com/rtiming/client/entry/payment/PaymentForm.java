package com.rtiming.client.entry.payment;

import java.math.BigDecimal;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractDefaultDateTimeField;
import com.rtiming.client.entry.payment.PaymentForm.MainBox.AmountField;
import com.rtiming.client.entry.payment.PaymentForm.MainBox.CancelButton;
import com.rtiming.client.entry.payment.PaymentForm.MainBox.CurrencyUidField;
import com.rtiming.client.entry.payment.PaymentForm.MainBox.EvtPaymentField;
import com.rtiming.client.entry.payment.PaymentForm.MainBox.OkButton;
import com.rtiming.client.entry.payment.PaymentForm.MainBox.PaymentNoField;
import com.rtiming.client.entry.payment.PaymentForm.MainBox.RegistrationField;
import com.rtiming.client.entry.payment.PaymentForm.MainBox.TypeUidField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.PaymentBean;
import com.rtiming.shared.common.security.permission.UpdatePaymentPermission;
import com.rtiming.shared.entry.RegistrationLookupCall;
import com.rtiming.shared.entry.payment.IPaymentProcessService;
import com.rtiming.shared.entry.payment.PaymentFormData;
import com.rtiming.shared.entry.payment.PaymentTypeCodeType;
import com.rtiming.shared.settings.currency.CurrencyCodeType;

@FormData(value = PaymentFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class PaymentForm extends AbstractForm {

  private Long paymentNr;

  public PaymentForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Payment");
  }

  public AmountField getAmountField() {
    return getFieldByClass(AmountField.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getPaymentNr() {
    return paymentNr;
  }

  @FormData
  public void setPaymentNr(Long paymentNr) {
    this.paymentNr = paymentNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new PaymentForm.ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new PaymentForm.NewHandler());
  }

  public CurrencyUidField getCurrencyUidField() {
    return getFieldByClass(CurrencyUidField.class);
  }

  public EvtPaymentField getEvtPaymentField() {
    return getFieldByClass(EvtPaymentField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public PaymentNoField getPaymentNoField() {
    return getFieldByClass(PaymentNoField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public RegistrationField getRegistrationField() {
    return getFieldByClass(RegistrationField.class);
  }

  public TypeUidField getTypeUidField() {
    return getFieldByClass(TypeUidField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class PaymentNoField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Number");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 60;
      }
    }

    @Order(20.0)
    public class RegistrationField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Registration");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return RegistrationLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(30.0)
    public class EvtPaymentField extends AbstractDefaultDateTimeField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Date");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(40.0)
    public class AmountField extends AbstractBigDecimalField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Amount");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected BigDecimal getConfiguredMaxValue() {
        return BigDecimal.valueOf(9.99999999E8);
      }

      @Override
      protected BigDecimal getConfiguredMinValue() {
        return BigDecimal.valueOf(-9.99999999E8);
      }
    }

    @Order(50.0)
    public class CurrencyUidField extends AbstractSmartField<Long> {

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

    @Order(60.0)
    public class TypeUidField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("PaymentType");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return PaymentTypeCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(68.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(70.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(80.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  private PaymentBean formData2bean(PaymentFormData formData) {
    PaymentBean bean = new PaymentBean();
    bean.setAmount(formData.getAmount().getValue());
    bean.setCurrencyUid(formData.getCurrencyUid().getValue());
    bean.setEvtPayment(formData.getEvtPayment().getValue());
    bean.setPaymentNo(formData.getPaymentNo().getValue());
    bean.setPaymentNr(formData.getPaymentNr());
    bean.setRegistrationNr(formData.getRegistration().getValue());
    bean.setTypeUid(formData.getTypeUid().getValue());
    return bean;
  }

  private PaymentFormData bean2formData(PaymentBean bean) {
    PaymentFormData formData = new PaymentFormData();
    formData.getAmount().setValue(bean.getAmount());
    formData.getCurrencyUid().setValue(bean.getCurrencyUid());
    formData.getEvtPayment().setValue(bean.getEvtPayment());
    formData.getPaymentNo().setValue(bean.getPaymentNo());
    formData.setPaymentNr(bean.getPaymentNr());
    formData.getRegistration().setValue(bean.getRegistrationNr());
    formData.getTypeUid().setValue(bean.getTypeUid());
    return formData;
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IPaymentProcessService service = BEANS.get(IPaymentProcessService.class);
      PaymentFormData formData = new PaymentFormData();
      exportFormData(formData);
      PaymentBean bean = service.load(formData2bean(formData));
      importFormData(bean2formData(bean));
      setEnabledPermission(new UpdatePaymentPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IPaymentProcessService service = BEANS.get(IPaymentProcessService.class);
      PaymentFormData formData = new PaymentFormData();
      exportFormData(formData);
      service.store(formData2bean(formData));
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IPaymentProcessService service = BEANS.get(IPaymentProcessService.class);
      PaymentFormData formData = new PaymentFormData();
      exportFormData(formData);
      PaymentBean bean = service.prepareCreate(formData2bean(formData));
      importFormData(bean2formData(bean));
    }

    @Override
    public void execStore() throws ProcessingException {
      IPaymentProcessService service = BEANS.get(IPaymentProcessService.class);
      PaymentFormData formData = new PaymentFormData();
      exportFormData(formData);
      PaymentBean bean = service.create(formData2bean(formData));
      importFormData(bean2formData(bean));
    }
  }
}
