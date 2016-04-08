package com.rtiming.client.settings.city;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.settings.city.CityForm.MainBox.AreaField;
import com.rtiming.client.settings.city.CityForm.MainBox.CancelButton;
import com.rtiming.client.settings.city.CityForm.MainBox.CityField;
import com.rtiming.client.settings.city.CityForm.MainBox.CountryField;
import com.rtiming.client.settings.city.CityForm.MainBox.OkButton;
import com.rtiming.client.settings.city.CityForm.MainBox.RegionField;
import com.rtiming.client.settings.city.CityForm.MainBox.ZipField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateCityPermission;
import com.rtiming.shared.settings.city.AreaCodeType;
import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.ICityProcessService;

@FormData(value = CityFormData.class, sdkCommand = SdkCommand.CREATE)
public class CityForm extends AbstractForm {

  private Long cityNr;

  public CityForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.AngleDoubleDown; // TODO MIG
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("City");
  }

  public AreaField getAreaField() {
    return getFieldByClass(AreaField.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getCityNr() {
    return cityNr;
  }

  @FormData
  public void setCityNr(Long cityNr) {
    this.cityNr = cityNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public CityField getCityField() {
    return getFieldByClass(CityField.class);
  }

  public CountryField getCountryField() {
    return getFieldByClass(CountryField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public RegionField getRegionField() {
    return getFieldByClass(RegionField.class);
  }

  public ZipField getZipField() {
    return getFieldByClass(ZipField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class ZipField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Zip");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 60;
      }
    }

    @Order(20.0)
    public class CityField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("City");
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

    @Order(30.0)
    public class AreaField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Area");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return AreaCodeType.class;
      }
    }

    @Order(40.0)
    public class RegionField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Region");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }
    }

    @Order(50.0)
    public class CountryField extends AbstractCountryField {

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

    }

    @Order(58.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(60.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(70.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      ICityProcessService service = BEANS.get(ICityProcessService.class);
      CityFormData formData = new CityFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateCityPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      ICityProcessService service = BEANS.get(ICityProcessService.class);
      CityFormData formData = new CityFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      ICityProcessService service = BEANS.get(ICityProcessService.class);
      CityFormData formData = new CityFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      ICityProcessService service = BEANS.get(ICityProcessService.class);
      CityFormData formData = new CityFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }
}
