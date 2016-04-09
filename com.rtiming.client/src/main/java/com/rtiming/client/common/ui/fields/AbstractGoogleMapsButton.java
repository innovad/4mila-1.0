package com.rtiming.client.common.ui.fields;

import java.awt.Desktop;
import java.net.URI;

import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.shared.Texts;

public abstract class AbstractGoogleMapsButton extends AbstractButton {

  @Override
  protected int getConfiguredDisplayStyle() {
    return DISPLAY_STYLE_LINK;
  }

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("ShowPositionOnGoogleMaps");
  }

  @Override
  protected void execClickAction() throws ProcessingException {
    try {
      String loc = getY() + "," + getX();
      String label = "";
      if (!StringUtility.isNullOrEmpty(getNumber())) {
        label = String.valueOf(getNumber().charAt(0));
      }
      URI uri = new URI("http://maps.google.com/maps/api/" +
          "staticmap?center=" + loc + "" +
          "&markers=color:blue%7Clabel:" + label + "%7C" + loc +
          "&zoom=12&size=1000x1000&sensor=false");
      Desktop.getDesktop().browse(uri);
    }
    catch (Exception e) {
      throw new ProcessingException(e.getMessage());
    }
  }

  protected abstract Double getX();

  protected abstract Double getY();

  protected abstract String getNumber();

}
