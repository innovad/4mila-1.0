package com.rtiming.client.dataexchange.swiss;

import java.net.URL;

import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardFinishButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardNextStepButton;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.Assert;

import com.rtiming.client.dataexchange.DataExchangeFinalizationForm;
import com.rtiming.client.dataexchange.DataExchangePreviewForm;
import com.rtiming.client.dataexchange.DataExchangeStartForm;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;

public final class InterfaceTestUtility {

  private InterfaceTestUtility() {
  }

  public static String doImport(Long importerUid, Long eventNr, String importFilePath, Long expectedRowCount, Long expectedErrorCount) throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(importerUid);
    start.getEventField().setValue(eventNr);
    URL file = FMilaUtility.findFileLocation(importFilePath, "");
    start.getFileField().setValue(FMilaUtility.createBinaryResource(file.getPath()));
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Preview Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangePreviewForm);
    DataExchangePreviewForm preview = (DataExchangePreviewForm) wizard.getWizardForm();
    Assert.assertEquals(expectedRowCount.longValue(), preview.getPreviewDataField().getTable().getRowCount());
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Finish Form
    DataExchangeFinalizationForm finish = (DataExchangeFinalizationForm) wizard.getWizardForm();
    String result = finish.getInfoField().getValue();
    System.out.println(result);
    String expectedText = Texts.get("Error") + ": " + expectedErrorCount;
    Assert.assertTrue("Result should contain " + expectedText, result.contains(expectedText));

    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeFinalizationForm);
    ((WizardFinishButton) wizard.getContainerForm().getWizardFinishButton()).doClick();

    Assert.assertFalse(wizard.isOpen());
    return result;
  }

}
