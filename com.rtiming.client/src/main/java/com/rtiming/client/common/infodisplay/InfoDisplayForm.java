package com.rtiming.client.common.infodisplay;

import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.browserfield.AbstractBrowserField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.infodisplay.InfoDisplayForm.MainBox.InfoField;
import com.rtiming.client.common.ui.desktop.Desktop.ToolsMenu.InfoDisplayMenu;

public class InfoDisplayForm extends AbstractForm {

  public InfoDisplayForm() throws ProcessingException {
    super();
  }

  @Override
  protected boolean getConfiguredCacheBounds() {
    return true;
  }

  @Override
  protected void execInitForm() throws ProcessingException {
    setTitle("4mila " + getConfiguredTitle());
  }

  @Override
  protected void execDisposeForm() throws ProcessingException {
    InfoDisplayUtility.setActive(false);
    getDesktop().getMenu(InfoDisplayMenu.class).setText(TEXTS.get("StartInfoDisplay"));
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  @Override
  protected int getConfiguredModalityHint() {
    return IForm.MODALITY_HINT_MODELESS;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Info");
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public InfoField getInfoField() {
    return getFieldByClass(InfoField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected boolean getConfiguredBorderVisible() {
      return false;
    }

    @Order(10.0)
    public class InfoField extends AbstractBrowserField {

      @Override
      protected int getConfiguredGridH() {
        return 10;
      }

      @Override
      protected int getConfiguredGridW() {
        return 10;
      }

      @Override
      protected boolean getConfiguredScrollBarEnabled() {
        return false;
      }

    }

    @Order(10.0)
    public class CloseKeyStroke extends AbstractKeyStroke {

      @Override
      protected String getConfiguredKeyStroke() {
        return "f8";
      }

      @Override
      protected void execAction() throws ProcessingException {
        InfoDisplayUtility.closeWindow();
      }

    }

    @Order(20.0)
    public class ResetKeyStroke extends AbstractKeyStroke {

      @Override
      protected String getConfiguredKeyStroke() {
        return "f9";
      }

      @Override
      protected void execAction() throws ProcessingException {
        new InfoDisplayIdleJob(ClientSession.get()).schedule();
      }

    }

  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() throws ProcessingException {
      new InfoDisplayIdleJob(ClientSession.get()).schedule();
    }

  }
}
