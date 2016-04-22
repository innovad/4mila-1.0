package com.rtiming.client.dataexchange.iof;

import java.io.FileReader;
import java.net.URL;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardCancelButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardFinishButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardNextStepButton;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm;
import com.rtiming.client.dataexchange.DataExchangePreviewForm;
import com.rtiming.client.dataexchange.DataExchangeStartForm;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.client.dataexchange.cache.DataExchangeFormUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;
import com.rtiming.shared.runner.IRunnerProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class IOF203RunnerInterfaceTest {

  @Before
  public void before() throws ProcessingException {
    DataExchangeFormUtility.cleanPreferences();
  }

  @Test
  public void testDialog() throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard203RunnerCode.ID);

    Assert.assertFalse(start.getLanguageField().isEnabled());
    Assert.assertFalse(start.getEventField().isEnabled());

    Assert.assertTrue(start.getImportExportGroup().isEnabled());
    Assert.assertTrue(start.getImportExportGroup().getValue());
    // Assert.assertTrue(start.getFileField().isTypeLoad());

    start.getImportExportGroup().setValue(false);
    Assert.assertFalse(start.getLanguageField().isEnabled());
    Assert.assertFalse(start.getEventField().isEnabled());
    // TODO MIG Assert.assertFalse(start.getFileField().isTypeLoad());

    ((WizardCancelButton) wizard.getContainerForm().getWizardCancelButton()).doClick();
  }

  @Test
  public void testImport() throws ProcessingException {

    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard203RunnerCode.ID);
    URL file = FMilaUtility.findFileLocation("resources//dataexchange//iof203runner.xml", "");
    start.getFileField().setValue(FMilaUtility.createBinaryResource(file.getPath()));
    Assert.assertTrue(start.getImportExportGroup().getValue());
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Preview Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangePreviewForm);
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Finalization Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeFinalizationForm);
    DataExchangeFinalizationForm finish = (DataExchangeFinalizationForm) wizard.getWizardForm();
    ((WizardFinishButton) wizard.getContainerForm().getWizardFinishButton()).doClick();

    Assert.assertFalse(wizard.isOpen());
    String result = finish.getInfoField().getValue();
    Assert.assertTrue(result.contains(Texts.get("Error") + ": 0"));

    // Check Data
    Long runner1Nr = BEANS.get(IRunnerProcessService.class).findRunner("UNITTEST1");
    Assert.assertNotNull(runner1Nr);
    Long runner2Nr = BEANS.get(IRunnerProcessService.class).findRunner("UNITTEST2");
    Assert.assertNotNull(runner2Nr);

    // Delete
    deleteRunner(runner1Nr);
    deleteRunner(runner2Nr);
  }

  @Test
  public void testExport() throws ProcessingException {

    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard203RunnerCode.ID);
    start.getImportExportGroup().setValue(false);
    // TODO MIG Assert.assertFalse(start.getFileField().isTypeLoad());
    String file = IOUtility.getTempFileName(".xml");
    start.getFileField().setValue(FMilaUtility.createBinaryResource(file));
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
      String[] check = exportedData.split("CompetitorList");
      Assert.assertEquals(3, check.length); // only start and finish tag should exist
      FMilaClientUtility.openDocument(file);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ProcessingException("File error: " + e.getMessage());
    }
  }

  private void deleteRunner(Long runnerNr) throws ProcessingException {
    RunnerBean bean = new RunnerBean();
    bean.setRunnerNr(runnerNr);
    BEANS.get(IRunnerProcessService.class).delete(bean);
  }

}
