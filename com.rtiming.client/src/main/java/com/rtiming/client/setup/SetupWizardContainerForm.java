package com.rtiming.client.setup;

import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm;
import org.eclipse.scout.rt.client.ui.wizard.IWizard;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.setup.SetupWizard.FinalizationStep;

public class SetupWizardContainerForm extends DefaultWizardContainerForm {

  public SetupWizardContainerForm(IWizard w) throws ProcessingException {
    super(w);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void handleEnterKey() throws ProcessingException {
    if (getWizard().getActiveStep().equals(getWizard().getStep(FinalizationStep.class))) {
      getWizard().doFinish();
    }
    else {
      super.handleEnterKey();
    }
  }

  @Override
  protected void handleEscapeKey(boolean kill) throws ProcessingException {
    // nop
  }

  @Override
  protected void execFormActivated() throws ProcessingException {
    IForm form = getWizard().getWizardForm();
    if (form != null && form instanceof WelcomeForm) {
      ((WelcomeForm) form).getUsernameField().requestFocus();
    }
  }

}
