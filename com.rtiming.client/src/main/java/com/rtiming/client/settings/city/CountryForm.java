package com.rtiming.client.settings.city;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractCodeBox;
import com.rtiming.client.settings.city.CountryForm.MainBox.CancelButton;
import com.rtiming.client.settings.city.CountryForm.MainBox.CodeBox;
import com.rtiming.client.settings.city.CountryForm.MainBox.CountryCodeField;
import com.rtiming.client.settings.city.CountryForm.MainBox.NationField;
import com.rtiming.client.settings.city.CountryForm.MainBox.OkButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateCountryPermission;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;

@FormData(value = CountryFormData.class, sdkCommand = SdkCommand.CREATE)
public class CountryForm extends AbstractForm {

  private Long countryUid;

  public CountryForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Country");
  }

  @FormData
  public Long getCountryUid() {
    return countryUid;
  }

  @FormData
  public void setCountryUid(Long countryNr) {
    this.countryUid = countryNr;
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

  public CountryCodeField getCountryCodeField() {
    return getFieldByClass(CountryCodeField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NationField getNationField() {
    return getFieldByClass(NationField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public CodeBox getCodeBox() {
    return getFieldByClass(CodeBox.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(20.0)
    public class CountryCodeField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("CountryCode");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 2;
      }
    }

    @Order(30.0)
    public class NationField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Nation");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 3;
      }
    }

    @Order(35.0)
    @FormData(value = CountryFormData.class, sdkCommand = SdkCommand.CREATE)
    public class CodeBox extends AbstractCodeBox {

      @Override
      protected boolean getConfiguredBorderVisible() {
        return false;
      }

      @Override
      protected void execInitField() throws ProcessingException {
        getFieldByClass(ShortcutField.class).setVisible(false);
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
      ICountryProcessService service = BEANS.get(ICountryProcessService.class);
      CountryFormData formData = new CountryFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateCountryPermission());
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      String countryCode = StringUtility.lowercase(getCountryCodeField().getValue());
// TODO MIG      
//      IconSpec icon = BEANS.get(IconProviderService.class).getIconSpec(countryCode);
//      if (icon != null) {
//        getCountryCodeField().setEnabled(false);
//      }
    }

    @Override
    public void execStore() throws ProcessingException {
      ICountryProcessService service = BEANS.get(ICountryProcessService.class);
      CountryFormData formData = new CountryFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      ICountryProcessService service = BEANS.get(ICountryProcessService.class);
      CountryFormData formData = new CountryFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      ICountryProcessService service = BEANS.get(ICountryProcessService.class);
      CountryFormData formData = new CountryFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }
}
