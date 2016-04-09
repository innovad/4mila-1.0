package com.rtiming.client.setup;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.scout.rt.client.ui.form.fields.GridData;
import org.eclipse.scout.rt.client.ui.wizard.AbstractWizard;
import org.eclipse.scout.rt.client.ui.wizard.IWizardContainerForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ClientInfoUtility;
import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientSyncJob;
import com.rtiming.client.dataexchange.AbstractFMilaWizardStep;
import com.rtiming.client.settings.account.AccountForm;
import com.rtiming.client.setup.SetupForm.MainBox.UserBox.UserField.Table;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.ILocalAccountService;
import com.rtiming.shared.settings.ISettingsOutlineService;
import com.rtiming.shared.settings.account.AccountFormData;

public class SetupWizard extends AbstractWizard {

  public SetupWizard() {
    super();
  }

  private final Map<String, String> plainTextPasswords = new HashMap<String, String>();

// TODO MIG  
//  @Override
//  protected boolean getConfiguredModal() {
//    return true;
//  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Welcome");
  }

  @Order(10.0)
  public class WelcomeStep extends AbstractFMilaWizardStep {

    @Override
    protected String getConfiguredTitle() {
      return Texts.get("Welcome");
    }

    @Override
    protected String getDescriptionText() {
      return TEXTS.get("WelcomeStepDescription");
    }

    @Override
    protected void execActivate(int stepKind) throws ProcessingException {
      WelcomeForm welcome = new WelcomeForm(getWizard());
      welcome.startWizardStep(this, WelcomeForm.NewHandler.class);
      setWizardForm(welcome);

      // Login Button
      getContainerForm().getWizardPreviousStepButton().setVisible(true);
      getContainerForm().getWizardPreviousStepButton().setLabel(TEXTS.get("Login"));
      getContainerForm().getWizardPreviousStepButton().setTooltipText(TEXTS.get("Login"));
      // TODO MIG getContainerForm().getWizardPreviousStepButton().setIconId(AbstractIcons.ArrowRight);
      getContainerForm().getWizardPreviousStepButton().setEnabled(false);

      // Quit
      getContainerForm().getWizardSuspendButton().setVisible(true);
      getContainerForm().getWizardSuspendButton().setLabel(TEXTS.get("Exit"));
      getContainerForm().getWizardSuspendButton().setTooltipText(TEXTS.get("Exit"));

      // New Account
      getContainerForm().getWizardNextStepButton().setLabel(TEXTS.get("CreateNewAccount"));
    }

    @Override
    protected void execDeactivate(int stepKind) throws ProcessingException {
      // Create new user
      getWizardForm().doCancel();
      getContainerForm().getWizardPreviousStepButton().setVisible(false);
      getContainerForm().getWizardPreviousStepButton().setLabel(TEXTS.get("WizardBackButton"));
      getContainerForm().getWizardPreviousStepButton().setTooltipText(TEXTS.get("WizardBackButtonTooltip"));
      // TODO MIG getContainerForm().getWizardPreviousStepButton().setIconId(AbstractIcons.WizardBackButton);
      getContainerForm().getWizardSuspendButton().setVisible(false);
    }

  }

  @Override
  protected void execSuspend() throws ProcessingException {
    // Exit the application
    ClientSession.get().stop();
  }

  @Override
  protected void execRefreshButtonPolicy() throws ProcessingException {
    super.execRefreshButtonPolicy();
    if (getPreviousStep() instanceof SetupStep) {
      getContainerForm().getWizardPreviousStepButton().setEnabled(false);
    }
    if (getActiveStep() instanceof SetupStep) {
      getContainerForm().getWizardPreviousStepButton().setEnabled(false);
    }
  }

  @Override
  public void doPreviousStep() throws ProcessingException {
    if (getActiveStep().getClass().equals(WelcomeStep.class)) {

      WelcomeForm form = (WelcomeForm) getWizardForm();

      AccountFormData account = new AccountFormData();
      account.getUsername().setValue(form.getUsernameField().getValue());
      account.getPassword().setValue(form.getPasswordField().getValue());
      account = BEANS.get(ILocalAccountService.class).loginOnline(account);

      if (account != null) {
        getWizardForm().doCancel();
        BEANS.get(ILocalAccountService.class).createLocalClient(account.getClientNr(), true);
        getContainerForm().getWizardPreviousStepButton().setEnabled(false);
        activateStep(getStepByClassName(SetupStep.class.getName()), true, true);
        getContainerForm().getWizardPreviousStepButton().setEnabled(false);
        getContainerForm().getWizardPreviousStepButton().setLabel(ScoutTexts.get("WizardBackButton"));
        // TODO MIG getContainerForm().getWizardPreviousStepButton().setIconId(AbstractIcons.WizardBackButton);
      }
      else {
        throw new VetoException(TEXTS.get("LoginError"));
      }
    }
    else {
      if (getWizardForm() != null) {
        getWizardForm().doCancel();
      }
      super.doPreviousStep();
    }
  }

  @Order(20.0)
  public class NewAccountStep extends AbstractFMilaWizardStep {

    @Override
    protected boolean getConfiguredEnabled() {
      return false;
    }

    @Override
    protected String getConfiguredTitle() {
      return Texts.get("NewAccount");
    }

    @Override
    protected void execActivate(int stepKind) throws ProcessingException {
      getContainerForm().getWizardNextStepButton().setLabel(ScoutTexts.get("WizardNextButton"));
      setEnabled(true);
      final AccountForm user = new AccountForm();
      user.startWizardStep(this, AccountForm.NewHandler.class);
      setWizardForm(user);

      // use a job to request the focus, no other solution found
      FMilaClientSyncJob job = new FMilaClientSyncJob("focus", ClientSession.get()) {
        @Override
        protected void runVoid() throws Exception {
          user.getEMailField().requestFocus();
        }
      };
      // TODO MIG job.schedule(20);
    }

    @Override
    protected void execDeactivate(int stepKind) throws ProcessingException {
      if (getWizardForm() != null) {
        getWizardForm().doOk();
      }
    }

    @Override
    protected String getDescriptionText() {
      return TEXTS.get("NewAccountStepDescription");
    }

  }

  @Order(30.0)
  public class SetupStep extends AbstractFMilaWizardStep {

    @Override
    protected String getConfiguredTitle() {
      return Texts.get("Setup");
    }

    @Override
    protected void execActivate(int stepKind) throws ProcessingException {
      // do initial data load
      BEANS.get(ISettingsOutlineService.class).intialDataLoad();

      getContainerForm().getWizardNextStepButton().setLabel(ScoutTexts.get("WizardNextButton"));

      SetupForm setup = new SetupForm();
      setup.startWizardStep(this, SetupForm.NewHandler.class);
      setWizardForm(setup);
    }

    @Override
    protected void execDeactivate(int stepKind) throws ProcessingException {
      getWizardForm().doOk();

      // save plaintext passwords for info sheet
      plainTextPasswords.clear();
      SetupForm form = (SetupForm) getWizardForm();
      Table table = form.getUserField().getTable();
      for (int k = 0; k < table.getRowCount(); k++) {
        plainTextPasswords.put(table.getUsernameColumn().getValue(k), table.getPasswordColumn().getValue(k));
      }
    }

    @Override
    protected String getDescriptionText() {
      return TEXTS.get("SetupStepDescription");
    }

  }

  @Order(40.0)
  public class FinalizationStep extends AbstractFMilaWizardStep {

    @Override
    protected String getConfiguredTitle() {
      return Texts.get("Finalization");
    }

    @Override
    protected void execActivate(int stepKind) throws ProcessingException {
      SetupWizardFinalizationForm finalInfo = new SetupWizardFinalizationForm();
      finalInfo.startWizardStep(this, SetupWizardFinalizationForm.NewHandler.class);
      setWizardForm(finalInfo);

      String installSummary = ClientInfoUtility.buildInstallationInfo(plainTextPasswords);
      finalInfo.getInfoField().setValue(installSummary);
    }

    @Override
    protected void execDeactivate(int stepKind) throws ProcessingException {

    }

    @Override
    protected String getDescriptionText() {
      return TEXTS.get("FinalizationStepDescription");
    }

  }

  @Override
  protected IWizardContainerForm execCreateContainerForm() throws ProcessingException {

    SetupWizardContainerForm containerForm = new SetupWizardContainerForm(this);
    containerForm.getMainBox().setBackgroundColor("ffffff");

    // TODO MIG containerForm.setDisplayHint(getDisplayHint());

    GridData data = containerForm.getRootGroupBox().getGridData();
    data.widthInPixel = 950;
    data.heightInPixel = 475;
    containerForm.getRootGroupBox().setGridDataInternal(data);

    // TODO MIG containerForm.setDisplayViewId(getDisplayViewId());
    // TODO MIG containerForm.setModal(isModal());

    return containerForm;
  }

}
