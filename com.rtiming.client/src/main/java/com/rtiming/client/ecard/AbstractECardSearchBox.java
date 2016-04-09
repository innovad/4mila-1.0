package com.rtiming.client.ecard;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.DefaultSubtypeSdkCommand;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractRadioButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBox;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.shared.Texts;
import com.rtiming.shared.ecard.AbstractECardSearchBoxData;
import com.rtiming.shared.ecard.ECardTypeCodeType;

@FormData(value = AbstractECardSearchBoxData.class, defaultSubtypeSdkCommand = DefaultSubtypeSdkCommand.CREATE, sdkCommand = SdkCommand.CREATE)
public abstract class AbstractECardSearchBox extends AbstractGroupBox {

  @Override
  public String getConfiguredLabel() {
    return Texts.get("ECards");
  }

  @Order(10.0)
  public class NumberField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Number");
    }
  }

  @Order(20.0)
  public class RentalCardGroup extends AbstractRadioButtonGroup<Boolean> {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("RentalCard");
    }

    @Order(10.0)
    public class YesButton extends AbstractRadioButton {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("Yes");
      }

      @Override
      protected Object getConfiguredRadioValue() {
        return true;
      }

    }

    @Order(20.0)
    public class NoButton extends AbstractRadioButton {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("No");
      }

      @Override
      protected Object getConfiguredRadioValue() {
        return false;
      }

    }

    @Order(30.0)
    public class AllButton extends AbstractRadioButton {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("All");
      }
    }
  }

  @Order(30.0)
  public class ECardTypeField extends AbstractListBox<Long> {

    @Override
    protected int getConfiguredGridH() {
      return 3;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("ECardType");
    }

    @Override
    protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
      return ECardTypeCodeType.class;
    }
  }
}
