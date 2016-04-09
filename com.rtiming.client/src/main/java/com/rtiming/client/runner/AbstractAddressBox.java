package com.rtiming.client.runner;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;

import com.rtiming.client.common.ui.fields.AbstractEmailField;
import com.rtiming.client.settings.city.AbstractCityField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.settings.city.AbstractAddressBoxData;

@FormData(value = AbstractAddressBoxData.class, sdkCommand = SdkCommand.CREATE)
public abstract class AbstractAddressBox extends AbstractGroupBox {

  public CityField getCityField() {
    return getFieldByClass(CityField.class);
  }

  public EMailField getEMailField() {
    return getFieldByClass(EMailField.class);
  }

  public FaxField getFaxField() {
    return getFieldByClass(FaxField.class);
  }

  public MobileField getMobileField() {
    return getFieldByClass(MobileField.class);
  }

  public PhoneField getPhoneField() {
    return getFieldByClass(PhoneField.class);
  }

  public StreetField getStreetField() {
    return getFieldByClass(StreetField.class);
  }

  public UrlField getUrlField() {
    return getFieldByClass(UrlField.class);
  }

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Address");
  }

  @Order(10.0)
  public class StreetField extends AbstractStringField {

    @Override
    protected int getConfiguredGridH() {
      return 2;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Street");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return 250;
    }

    @Override
    protected boolean getConfiguredMultilineText() {
      return true;
    }
  }

  @Order(20.0)
  public class CityField extends AbstractCityField {

  }

  @Order(30.0)
  public class PhoneField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Phone");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return 250;
    }
  }

  @Order(40.0)
  public class FaxField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Fax");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return 250;
    }
  }

  @Order(50.0)
  public class MobileField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("MobilePhone");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return 250;
    }
  }

  @Order(60.0)
  public class EMailField extends AbstractEmailField {

  }

  @Order(70.0)
  public class UrlField extends AbstractStringField {

// TODO MIG    
//    @Override
//    protected boolean getConfiguredDecorationLink() {
//      return true;
//    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("www");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return 250;
    }
  }

}
