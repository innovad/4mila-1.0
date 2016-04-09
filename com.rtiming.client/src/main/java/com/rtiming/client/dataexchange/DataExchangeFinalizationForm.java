package com.rtiming.client.dataexchange;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.htmlfield.AbstractHtmlField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm.MainBox.InfoField;
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm.MainBox.OpenFileButton;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.DataExchangeFinalizationFormData;

@FormData(value = DataExchangeFinalizationFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class DataExchangeFinalizationForm extends AbstractForm {

  private String m_fullPathName;

  public DataExchangeFinalizationForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Finalization");
  }

  public void startNew() throws ProcessingException {
    startInternal(new DataExchangeFinalizationForm.NewHandler());
  }

  public InfoField getInfoField() {
    return getFieldByClass(InfoField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OpenFileButton getOpenFileButton() {
    return getFieldByClass(OpenFileButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class InfoField extends AbstractHtmlField {

      @Override
      protected int getConfiguredGridH() {
        return 15;
      }

      @Override
      protected boolean getConfiguredLabelVisible() {
        return false;
      }

      @Override
      protected boolean getConfiguredScrollBarEnabled() {
        return true;
      }
    }

    @Order(20.0)
    public class OpenFileButton extends AbstractButton {

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
        FMilaClientUtility.openDocument(getFullPathName());
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

  @FormData
  public String getFullPathName() {
    return m_fullPathName;
  }

  @FormData
  public void setFullPathName(String fullPathName) {
    m_fullPathName = fullPathName;
  }
}
