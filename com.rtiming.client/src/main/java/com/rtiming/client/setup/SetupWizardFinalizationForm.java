package com.rtiming.client.setup;

import java.io.File;

import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.IOUtility;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.setup.SetupWizardFinalizationForm.MainBox.InfoField;
import com.rtiming.client.setup.SetupWizardFinalizationForm.MainBox.SaveAsButton;
import com.rtiming.shared.Texts;

public class SetupWizardFinalizationForm extends AbstractForm {

  public SetupWizardFinalizationForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Finalization");
  }

  public void startNew() throws ProcessingException {
    startInternal(new SetupWizardFinalizationForm.NewHandler());
  }

  public InfoField getInfoField() {
    return getFieldByClass(InfoField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public SaveAsButton getOpenFileButton() {
    return getFieldByClass(SaveAsButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class InfoField extends AbstractStringField {

      @Override
      protected int getConfiguredGridH() {
        return 15;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return Integer.MAX_VALUE;
      }

      @Override
      protected boolean getConfiguredMultilineText() {
        return true;
      }

      @Override
      protected boolean getConfiguredLabelVisible() {
        return false;
      }

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

    }

    @Order(20.0)
    public class SaveAsButton extends AbstractButton {

      @Override
      protected int getConfiguredDisplayStyle() {
        return DISPLAY_STYLE_LINK;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("OpenFile");
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        File temp = IOUtility.createTempFile(IOUtility.getTempFileName(".txt"), getInfoField().getValue().getBytes());
        FMilaClientUtility.openDocument(temp.getAbsolutePath());
      }
    }

  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
    }

    @Override
    public void execStore() throws ProcessingException {
    }
  }

}
