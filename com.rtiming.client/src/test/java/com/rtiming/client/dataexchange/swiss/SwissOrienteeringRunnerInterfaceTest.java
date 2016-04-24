package com.rtiming.client.dataexchange.swiss;

import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardFinishButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardNextStepButton;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.TestClientSession;
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm;
import com.rtiming.client.dataexchange.DataExchangePreviewForm;
import com.rtiming.client.dataexchange.DataExchangeStartForm;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.client.dataexchange.cache.DataExchangeFormUtility;
import com.rtiming.client.runner.RunnerForm.MainBox.FirstNameField;
import com.rtiming.client.runner.RunnerForm.MainBox.LastNameField;
import com.rtiming.client.test.data.RunnerTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestClientSession.class)
public class SwissOrienteeringRunnerInterfaceTest {

  private RunnerTestDataProvider runner;

  @Before
  public void before() throws ProcessingException {
    DataExchangeFormUtility.cleanPreferences();
    List<FieldValue> data = new ArrayList<FieldValue>();
    data.add(new FieldValue(LastNameField.class, "LAST_NAME"));
    data.add(new FieldValue(FirstNameField.class, "FIRST_NAME"));
    runner = new RunnerTestDataProvider(data);
  }

  @Test
  public void testImportWithoutHeaderRow() throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.SwissOrienteeringRunnerDatabaseCode.ID);
    Assert.assertNull(start.getEventField().getValue());
    Assert.assertTrue(start.getIgnoreHeaderRowField().getValue());
    URL file = FMilaUtility.findFileLocation("resources//dataexchange//swissorienteering.csv", "");
    start.getFileField().setValue(FMilaUtility.createBinaryResource(file.getPath()));
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Preview Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangePreviewForm);
    DataExchangePreviewForm preview = (DataExchangePreviewForm) wizard.getWizardForm();
    Assert.assertEquals(6, preview.getPreviewDataField().getTable().getRowCount()); // without header
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Finish Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeFinalizationForm);
    DataExchangeFinalizationForm finish = (DataExchangeFinalizationForm) wizard.getWizardForm();
    ((WizardFinishButton) wizard.getContainerForm().getWizardFinishButton()).doClick();

    Assert.assertFalse(wizard.isOpen());
    String result = finish.getInfoField().getValue();
    System.out.println("Result: " + result);
    Assert.assertTrue(result.contains(Texts.get("Error") + ": 0"));
  }

  @Test
  public void testImportWithHeaderRow() throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.SwissOrienteeringRunnerDatabaseCode.ID);
    start.getIgnoreHeaderRowField().setValue(false);
    Assert.assertNull(start.getEventField().getValue());
    URL file = FMilaUtility.findFileLocation("resources//dataexchange//swissorienteering.csv", "");
    start.getFileField().setValue(FMilaUtility.createBinaryResource(file.getPath()));
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Preview Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangePreviewForm);
    DataExchangePreviewForm preview = (DataExchangePreviewForm) wizard.getWizardForm();
    Assert.assertEquals(7, preview.getPreviewDataField().getTable().getRowCount()); // with header
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Finish Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeFinalizationForm);
    DataExchangeFinalizationForm finish = (DataExchangeFinalizationForm) wizard.getWizardForm();
    ((WizardFinishButton) wizard.getContainerForm().getWizardFinishButton()).doClick();

    Assert.assertFalse(wizard.isOpen());
    String result = finish.getInfoField().getValue();
    System.out.println("Result: " + result);
    Assert.assertTrue(result.contains(Texts.get("Error") + ": 1")); // Header Row
  }

  @Test
  public void testExport() throws ProcessingException {

    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.SwissOrienteeringRunnerDatabaseCode.ID);
    start.getImportExportGroup().setValue(false);
    Assert.assertFalse(start.getImportExportGroup().getValue());
    // TODO MIG Assert.assertFalse(start.getFileField().isTypeLoad());
    String file = IOUtility.getTempFileName(".csv");
    start.getFileField().setValue(FMilaUtility.createBinaryResource(file));
    Assert.assertFalse(start.getEventField().isEnabled());
    Assert.assertNull(start.getEventField().getValue());
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Preview Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangePreviewForm);
    Assert.assertTrue(wizard.getContainerForm().getWizardNextStepButton().isEnabled());
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Finalization Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeFinalizationForm);
    DataExchangeFinalizationForm finish = (DataExchangeFinalizationForm) wizard.getWizardForm();

    ((WizardFinishButton) wizard.getContainerForm().getWizardFinishButton()).doClick();

    Assert.assertFalse(wizard.isOpen());
    String result = finish.getInfoField().getValue();
    Assert.assertTrue(result.contains(Texts.get("Error") + ": 0"));

    try {
      String exportedData = IOUtility.getContent(new FileReader(file));
      Assert.assertTrue(exportedData.length() > 0);
      FMilaClientUtility.openDocument(file);
      System.out.println("Result: " + result);
      System.out.println("File path: " + file);
      System.out.println("File content: " + exportedData);
      Assert.assertTrue("Export should contain last name", exportedData.contains("LAST_NAME"));
      Assert.assertTrue("Export should contain first name", exportedData.contains("FIRST_NAME"));
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ProcessingException("File error: " + e.getMessage());
    }

  }

  @After
  public void after() throws ProcessingException {
    runner.remove();
  }

}
