package com.rtiming.client.setup;

import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.imagefield.AbstractImageField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.wizard.IWizard;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;

import com.rtiming.client.setup.WelcomeForm.MainBox.ImageField;
import com.rtiming.client.setup.WelcomeForm.MainBox.PasswordField;
import com.rtiming.client.setup.WelcomeForm.MainBox.UsernameField;
import com.rtiming.shared.Texts;

public class WelcomeForm extends AbstractForm {

  private final IWizard enclosingWizard;

  public WelcomeForm(IWizard enclosingWizard) throws ProcessingException {
    super();
    this.enclosingWizard = enclosingWizard;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Welcome");
  }

  public void startNew() throws ProcessingException {
    startInternal(new WelcomeForm.NewHandler());
  }

  public UsernameField getUsernameField() {
    return getFieldByClass(UsernameField.class);
  }

  public ImageField getImageField() {
    return getFieldByClass(ImageField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public PasswordField getPasswordField() {
    return getFieldByClass(PasswordField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected String getConfiguredBackgroundColor() {
      return "FFFFFF";
    }

    @Override
    protected int getConfiguredGridColumnCount() {
      return 3;
    }

    private void handleLoginButton() {
      if (!StringUtility.isNullOrEmpty(getUsernameField().getValue()) && !StringUtility.isNullOrEmpty(getPasswordField().getValue())) {
        enclosingWizard.getContainerForm().getWizardPreviousStepButton().setEnabled(true);
      }
      else {
        enclosingWizard.getContainerForm().getWizardPreviousStepButton().setEnabled(false);
      }
    }

    @Order(10.0)
    public class ImageField extends AbstractImageField {

      @Override
      protected int getConfiguredGridH() {
        return 6;
      }

      @Override
      protected int getConfiguredGridW() {
        return 3;
      }

      @Override
      protected boolean getConfiguredLabelVisible() {
        return false;
      }
    }

    @Order(20.0)
    public class UsernameField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("Username");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }

      @Override
      protected boolean getConfiguredUpdateDisplayTextOnModify() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        handleLoginButton();
      }

    }

    @Order(30.0)
    public class PasswordField extends AbstractStringField {

      @Override
      protected boolean getConfiguredInputMasked() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }

      @Override
      protected boolean getConfiguredUpdateDisplayTextOnModify() {
        return true;
      }

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("Password");
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        handleLoginButton();
      }

    }

    @Order(10.0)
    public class EnterKeyStroke extends AbstractKeyStroke {

      @Override
      protected String getConfiguredKeyStroke() {
        return "enter";
      }

      @Override
      protected void execAction() throws ProcessingException {
        // TODO MIG enclosingWizard.getContainerForm().getWizardPreviousStepButton().doClick();
      }
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() throws ProcessingException {
//      TODO MIG      
//      IClientSession clientSession = ClientSession.get();
//      IconSpec iconSpec = clientSession.getIconLocator().getIconSpec("application_logo_large.png");
//      getImageField().setImage(iconSpec.getContent());
    }

  }
}
