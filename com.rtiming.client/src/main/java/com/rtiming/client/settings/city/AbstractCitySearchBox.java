package com.rtiming.client.settings.city;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.DefaultSubtypeSdkCommand;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.shared.Texts;
import com.rtiming.shared.settings.city.AbstractCitySearchBoxData;
import com.rtiming.shared.settings.city.AreaCodeType;
import com.rtiming.shared.settings.city.CountryCodeType;

@FormData(value = AbstractCitySearchBoxData.class, defaultSubtypeSdkCommand = DefaultSubtypeSdkCommand.CREATE, sdkCommand = SdkCommand.CREATE)
public abstract class AbstractCitySearchBox extends AbstractGroupBox {

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Cities");
  }

  @Order(10.0)
  public class ZipField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Zip");
    }
  }

  @Order(20.0)
  public class CityField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("City");
    }
  }

  @Order(30.0)
  public class AreaField extends AbstractSmartField<Long> {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Area");
    }

    @Override
    protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
      return AreaCodeType.class;
    }
  }

  @Order(40.0)
  public class RegionField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Region");
    }
  }

  @Order(50.0)
  public class CountryField extends AbstractSmartField<Long> {

    @Override
    protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
      return CountryCodeType.class;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Country");
    }
  }
}
