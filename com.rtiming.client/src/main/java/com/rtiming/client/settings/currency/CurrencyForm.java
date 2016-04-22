package com.rtiming.client.settings.currency;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;

import com.rtiming.client.AbstractDoubleField;
import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractCodeBox;
import com.rtiming.client.settings.currency.CurrencyForm.MainBox.CancelButton;
import com.rtiming.client.settings.currency.CurrencyForm.MainBox.ExchangeRateField;
import com.rtiming.client.settings.currency.CurrencyForm.MainBox.OkButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateCurrencyPermission;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;

@FormData(value = CurrencyFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class CurrencyForm extends AbstractForm {

  private Long currencyUid;

  public CurrencyForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Currency");
  }

  @FormData
  public Long getCurrencyUid() {
    return currencyUid;
  }

  @FormData
  public void setCurrencyUid(Long currencyNr) {
    this.currencyUid = currencyNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new CurrencyForm.ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new CurrencyForm.NewHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public ExchangeRateField getExchangeRateField() {
    return getFieldByClass(ExchangeRateField.class);
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
    public class ExchangeRateField extends AbstractDoubleField {

      @Override
      protected int getConfiguredFractionDigits() {
        return 6;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("ExchangeRate");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxFractionDigits() {
        return 6;
      }

      @Override
      protected Double getConfiguredMaxValue() {
        return 1000D;
      }

      @Override
      protected int getConfiguredMinFractionDigits() {
        return 6;
      }

      @Override
      protected Double getConfiguredMinValue() {
        return 0.000001D;
      }
    }

    @Order(20.0)
    public class CodeBox extends AbstractCodeBox {

      @Override
      protected boolean getConfiguredBorderVisible() {
        return false;
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
      ICurrencyProcessService service = BEANS.get(ICurrencyProcessService.class);
      CurrencyFormData formData = new CurrencyFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateCurrencyPermission());

      // default currency, exchange rate not enabled, always 1.000000
      Long defaultCurrencyUid = BEANS.get(IDefaultProcessService.class).getDefaultCurrencyUid();
      if (CompareUtility.equals(defaultCurrencyUid, formData.getCurrencyUid())) {
        getExchangeRateField().setEnabled(false);
      }

    }

    @Override
    public void execStore() throws ProcessingException {
      ICurrencyProcessService service = BEANS.get(ICurrencyProcessService.class);
      CurrencyFormData formData = new CurrencyFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      ICurrencyProcessService service = BEANS.get(ICurrencyProcessService.class);
      CurrencyFormData formData = new CurrencyFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      ICurrencyProcessService service = BEANS.get(ICurrencyProcessService.class);
      CurrencyFormData formData = new CurrencyFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }
}
