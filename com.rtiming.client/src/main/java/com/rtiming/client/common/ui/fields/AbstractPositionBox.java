package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.DefaultSubtypeSdkCommand;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.Texts;
import com.rtiming.shared.map.AbstractPositionBoxData;

/**
 *
 */
@FormData(value = AbstractPositionBoxData.class, sdkCommand = SdkCommand.CREATE, defaultSubtypeSdkCommand = DefaultSubtypeSdkCommand.CREATE)
public abstract class AbstractPositionBox extends AbstractGroupBox {

  @Override
  protected boolean getConfiguredBorderVisible() {
    return false;
  }

  @Override
  protected void execInitField() throws ProcessingException {
    getXField().setLabel(getConfiguredLabel() + " " + getXField().getLabel());
    getYField().setLabel(getConfiguredLabel() + " " + getYField().getLabel());
  }

  public XField getXField() {
    return getFieldByClass(XField.class);
  }

  public YField getYField() {
    return getFieldByClass(YField.class);
  }

  public abstract void handleExecChangedValue();

  public boolean isFieldEmpty() {
    return (getXField().getValue() == null || getYField().getValue() == null);
  }

  @Order(10.0)
  public class XField extends AbstractLongLatField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Longitude");
    }

    @Override
    protected void execChangedValue() throws ProcessingException {
      handleExecChangedValue();
    }
  }

  @Order(20.0)
  public class YField extends AbstractLongLatField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Latitude");
    }

    @Override
    protected void execChangedValue() throws ProcessingException {
      handleExecChangedValue();
    }
  }
}
