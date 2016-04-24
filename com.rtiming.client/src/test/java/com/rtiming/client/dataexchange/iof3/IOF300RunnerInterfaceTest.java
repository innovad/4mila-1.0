package com.rtiming.client.dataexchange.iof3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.net.URL;
import java.util.List;

import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardCancelButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardFinishButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardNextStepButton;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
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
import com.rtiming.client.dataexchange.XMLUtility;
import com.rtiming.client.dataexchange.cache.DataExchangeFormUtility;
import com.rtiming.client.test.data.CountryTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;
import com.rtiming.shared.dataexchange.iof3.IOF300Utility;
import com.rtiming.shared.dataexchange.iof3.xml.CompetitorList;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.runner.RunnerRowData;
import com.rtiming.shared.runner.RunnersSearchFormData;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestClientSession.class)
public class IOF300RunnerInterfaceTest {

  private CountryTestDataProvider country;

  @Before
  public void before() throws ProcessingException {
    DataExchangeFormUtility.cleanPreferences();
    country = new CountryTestDataProvider();
    BEANS.get(IDefaultProcessService.class).setDefaultCountryUid(country.getCountryUid());
  }

  @Test
  public void testDialog() throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard300RunnerCode.ID);

    assertFalse(start.getLanguageField().isEnabled());
    assertFalse(start.getEventField().isEnabled());

    assertTrue(start.getImportExportGroup().isEnabled());
    assertTrue(start.getImportExportGroup().getValue());
    // TODO MIG assertTrue(start.getFileField().isTypeLoad());

    start.getImportExportGroup().setValue(false);
    assertFalse(start.getLanguageField().isEnabled());
    assertFalse(start.getEventField().isEnabled());
    // TODO MIG assertFalse(start.getFileField().isTypeLoad());

    ((WizardCancelButton) wizard.getContainerForm().getWizardCancelButton()).doClick();
  }

  @Test
  public void testImport() throws ProcessingException {

    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard300RunnerCode.ID);
    URL file = FMilaUtility.findFileLocation("resources//dataexchange//iof300runner.xml", "");
    start.getFileField().setValue(FMilaUtility.createBinaryResource(file.getPath()));
    assertTrue(start.getImportExportGroup().getValue());
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Preview Form
    assertTrue(wizard.getWizardForm() instanceof DataExchangePreviewForm);
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Finalization Form
    assertTrue(wizard.getWizardForm() instanceof DataExchangeFinalizationForm);
    DataExchangeFinalizationForm finish = (DataExchangeFinalizationForm) wizard.getWizardForm();
    ((WizardFinishButton) wizard.getContainerForm().getWizardFinishButton()).doClick();

    assertFalse(wizard.isOpen());
    String result = finish.getInfoField().getValue();
    assertTrue(result.contains(Texts.get("Error") + ": 0"));

    // Check Data
    Long runner1Nr = BEANS.get(IRunnerProcessService.class).findRunner("IOF300-1");
    assertNotNull(runner1Nr);
    Long runner2Nr = BEANS.get(IRunnerProcessService.class).findRunner("IOF300-2");
    assertNotNull(runner2Nr);

    RunnersSearchFormData searchFormData = new RunnersSearchFormData();
    searchFormData.getRunnerBox().getExtKey().setValue("IOF300-1");
    List<RunnerRowData> result1 = BEANS.get(IEventsOutlineService.class).getRunnerTableData(searchFormData);
    boolean found = false;
    for (RunnerRowData row : result1) {
      if (row.getExtKey().equals("IOF300-1")) {
        found = true;
        assertEquals(SexCodeType.WomanCode.ID, row.getSexUid().longValue());
        assertEquals("Mary", row.getFirstName());
        assertEquals("West", row.getLastName());
        assertEquals(DateUtility.parse("1972-12-16", "yyyy-MM-dd"), row.getEvtBirthdate());
        assertEquals("Year", 1972, row.getYear().longValue());
        assertEquals("GBR", row.getNation());
        assertEquals("13 Long Road", row.getStreet());
        assertEquals("A1 1AA", row.getZip());
        assertEquals("Smalltown", row.getCity());
        CountryFormData country = BEANS.get(ICountryProcessService.class).find(null, null, "GBR");
        assertEquals("Country ID", country.getCountryUid(), row.getCountryUid());
        assertEquals("mary.west@example.com", row.getEmail());
        assertEquals("+44 12 3456 7890", row.getPhone());
        assertEquals("Unintentional Navigators", row.getClub());
        Assert.assertEquals("12345", row.geteCard());
        break;
      }
    }
    assertTrue("imported data found", found);

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
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard300RunnerCode.ID);
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
      FileReader reader = new FileReader(file);
      String exportedData = IOUtility.getContent(reader);
      reader.close();
      Assert.assertTrue(exportedData.length() > 0);
      Object xml = XMLUtility.unmarshal(file, new CompetitorList(), true);
      Assert.assertTrue("CompetitorList", xml instanceof CompetitorList);
      CompetitorList list = (CompetitorList) xml;
      Assert.assertEquals("Version", IOF300Utility.getIOFVersion(), list.getIofVersion());
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
