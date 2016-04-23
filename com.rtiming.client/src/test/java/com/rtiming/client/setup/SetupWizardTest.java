package com.rtiming.client.setup;

import java.util.Date;

import org.eclipse.scout.rt.client.ui.form.fields.button.IButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardNextStepButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardPreviousStepButton;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.settings.account.AccountForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.test.helper.ITestingJPAService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class SetupWizardTest {

  @Test
  public void testWelcomeStep() throws Exception {
    SetupWizard wizard = new SetupWizard();
    wizard.start();

    Assert.assertNotNull(wizard.getWizardForm());
    Assert.assertTrue(wizard.getWizardForm() instanceof WelcomeForm);
    // TODO MIG Assert.assertTrue(wizard.isModal());

    WelcomeForm welcome = (WelcomeForm) wizard.getWizardForm();

    Assert.assertFalse(welcome.getUsernameField().isMandatory());
    Assert.assertFalse(welcome.getPasswordField().isMandatory());

    wizard.doCancel();
  }

  @Test
  public void testWelcomeStepLoginButton() throws Exception {
    SetupWizard wizard = new SetupWizard();
    wizard.start();
    WelcomeForm welcome = (WelcomeForm) wizard.getWizardForm();

    // Login Button should be enabled when user name and password are filled
    ScoutClientAssert.assertDisabled((IButton) wizard.getContainerForm().getWizardPreviousStepButton());
    Assert.assertEquals(TEXTS.get("Login"), wizard.getContainerForm().getWizardPreviousStepButton().getLabel());

    welcome.getUsernameField().setValue("ABC");
    ScoutClientAssert.assertDisabled((IButton) wizard.getContainerForm().getWizardPreviousStepButton());

    welcome.getPasswordField().setValue("DEF");
    ScoutClientAssert.assertEnabled((IButton) wizard.getContainerForm().getWizardPreviousStepButton());

    welcome.getUsernameField().setValue(null);
    ScoutClientAssert.assertDisabled((IButton) wizard.getContainerForm().getWizardPreviousStepButton());

    // Next Step Button: New Account
    Assert.assertEquals(Texts.get("CreateNewAccount"), wizard.getContainerForm().getWizardNextStepButton().getLabel());
    ScoutClientAssert.assertEnabled((IButton) wizard.getContainerForm().getWizardNextStepButton());

    wizard.doCancel();
  }

  @Test
  public void testWelcomeStepQuitButton() throws Exception {
    SetupWizard wizard = new SetupWizard();
    wizard.start();

    Assert.assertEquals(Texts.get("Exit"), wizard.getContainerForm().getWizardSuspendButton().getLabel());
    ScoutClientAssert.assertEnabled((IButton) wizard.getContainerForm().getWizardSuspendButton());
    ScoutClientAssert.assertInvisible((IButton) wizard.getContainerForm().getWizardCancelButton());

    wizard.doCancel();
  }

  @Test(expected = VetoException.class)
  public void testLoginOnline() throws Exception {
    SetupWizard wizard = new SetupWizard();
    wizard.start();

    WelcomeForm welcome = (WelcomeForm) wizard.getWizardForm();
    welcome.getUsernameField().setValue("ABC");
    welcome.getPasswordField().setValue("DEF");

    ScoutClientAssert.assertEnabled((IButton) wizard.getContainerForm().getWizardPreviousStepButton());
    ((WizardPreviousStepButton) wizard.getContainerForm().getWizardPreviousStepButton()).doClick();
  }

  @Test
  public void testNewAccountStep() throws Exception {
    SetupWizard wizard = new SetupWizard();
    wizard.start();
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    Assert.assertNotNull(wizard.getWizardForm());
    Assert.assertTrue(wizard.getWizardForm() instanceof AccountForm);
    // TODO MIG Assert.assertTrue(wizard.isModal());

    ScoutClientAssert.assertInvisible((IButton) wizard.getContainerForm().getWizardCancelButton());
    ScoutClientAssert.assertInvisible((IButton) wizard.getContainerForm().getWizardSuspendButton());

    ScoutClientAssert.assertEnabled((IButton) wizard.getContainerForm().getWizardPreviousStepButton());
    ScoutClientAssert.assertVisible((IButton) wizard.getContainerForm().getWizardPreviousStepButton());
    ((WizardPreviousStepButton) wizard.getContainerForm().getWizardPreviousStepButton()).doClick();

    Assert.assertNotNull(wizard.getWizardForm());
    Assert.assertTrue(wizard.getWizardForm() instanceof WelcomeForm);

    ScoutClientAssert.assertInvisible((IButton) wizard.getContainerForm().getWizardCancelButton());
    ScoutClientAssert.assertVisible((IButton) wizard.getContainerForm().getWizardSuspendButton());
    ScoutClientAssert.assertVisible((IButton) wizard.getContainerForm().getWizardPreviousStepButton());
  }

  @Test(expected = VetoException.class)
  public void testNewAccountVeto() throws Exception {
    SetupWizard wizard = new SetupWizard();
    wizard.start();
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();
    Assert.assertTrue(wizard.getWizardForm() instanceof AccountForm);
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();
  }

  @Test
  public void testNewAccountCreation() throws Exception {
    BEANS.get(ITestingJPAService.class).cleanupAccounts();
    BEANS.get(ITestingJPAService.class).cleanupCountries();

    SetupWizard wizard = new SetupWizard();
    wizard.start();
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();
    Assert.assertTrue(wizard.getWizardForm() instanceof AccountForm);

    AccountForm form = (AccountForm) wizard.getWizardForm();
    FormTestUtility.fillFormFields(form);
    String unique = "" + (new Date()).getTime();
    form.getPasswordField().setValue(unique);
    form.getRepeatPasswordField().setValue(unique);
    form.getUsernameField().setValue(unique);
    form.getEMailField().setValue(unique + "@w123456.com");

    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();
    Assert.assertNotNull(form.getAccountNr());

    BEANS.get(ITestingJPAService.class).deleteAccount(form.getAccountNr());
  }

}
