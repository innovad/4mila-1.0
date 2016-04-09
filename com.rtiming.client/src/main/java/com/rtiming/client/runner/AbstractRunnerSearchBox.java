package com.rtiming.client.runner;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.DefaultSubtypeSdkCommand;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractRadioButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.ScoutTexts;

import com.rtiming.shared.Texts;
import com.rtiming.shared.runner.AbstractRunnerSearchBoxData;

@FormData(value = AbstractRunnerSearchBoxData.class, defaultSubtypeSdkCommand = DefaultSubtypeSdkCommand.CREATE, sdkCommand = SdkCommand.CREATE)
public abstract class AbstractRunnerSearchBox extends AbstractGroupBox {

  @Override
  public String getConfiguredLabel() {
    return Texts.get("Runners");
  }

  @Order(20.0)
  public class NameField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return ScoutTexts.get("Name");
    }
  }

  @Order(30.0)
  public class ExtKeyField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Number");
    }
  }

  @Order(40.0)
  public class ActiveGroup extends AbstractRadioButtonGroup<Boolean> {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Active");
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
}
