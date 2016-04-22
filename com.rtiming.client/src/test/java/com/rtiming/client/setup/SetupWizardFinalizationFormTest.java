package com.rtiming.client.setup;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.test.FMilaClientTestUtility;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class SetupWizardFinalizationFormTest {

  @Test
  public void testNew() throws Exception {
    SetupWizardFinalizationForm form = new SetupWizardFinalizationForm();
    form.startNew();
    FMilaClientTestUtility.testFormFields(form);
  }

  @Test
  public void testOpen() throws Exception {
    SetupWizardFinalizationForm form = new SetupWizardFinalizationForm();
    form.startNew();
    form.getInfoField().setValue("INFO");
    form.getOpenFileButton().doClick();
  }

}
