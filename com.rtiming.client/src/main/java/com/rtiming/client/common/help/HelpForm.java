package com.rtiming.client.common.help;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.browserfield.AbstractBrowserField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.common.help.HelpForm.MainBox.HelpField;

public class HelpForm extends AbstractForm {

  public HelpForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Help");
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public HelpField getHelpField() {
    return getFieldByClass(HelpField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected int getConfiguredWidthInPixel() {
      return 1000;
    }

    @Override
    protected int getConfiguredHeightInPixel() {
      return 700;
    }

    @Order(10.0)
    public class HelpField extends AbstractBrowserField {

      @Override
      protected int getConfiguredGridH() {
        return 15;
      }

      @Override
      protected int getConfiguredGridW() {
        return 15;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Help");
      }
    }
  }

  @Override
  protected int getConfiguredModalityHint() {
    return IForm.MODALITY_HINT_MODAL;
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  public class NewHandler extends AbstractFormHandler {
  }
}
