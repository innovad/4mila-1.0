package com.rtiming.client.dataexchange.oe2003;

import java.io.FileReader;
import java.util.Date;

import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardCancelButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardFinishButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardNextStepButton;
import org.eclipse.scout.rt.platform.BEANS;
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

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm;
import com.rtiming.client.dataexchange.DataExchangePreviewForm;
import com.rtiming.client.dataexchange.DataExchangeStartForm;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.client.ecard.download.PunchForm;
import com.rtiming.client.test.data.CourseControlTestDataProvider;
import com.rtiming.client.test.data.DownloadedECardTestDataProvider;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.IRaceService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class OE2003ResultListExporterTest {

  private EventWithIndividualClassTestDataProvider event;

  @Before
  public void before() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
  }

  @Test
  public void testDialog() throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.OE2003ResultListCode.ID);

    Assert.assertTrue(start.getLanguageField().isEnabled());
    Assert.assertTrue(start.getEventField().isEnabled());

    Assert.assertFalse(start.getImportExportGroup().isEnabled());
    Assert.assertFalse(start.getImportExportGroup().getValue());
    // TODO MIG Assert.assertFalse(start.getFileField().isTypeLoad());

    ((WizardCancelButton) wizard.getContainerForm().getWizardCancelButton()).doClick();
  }

  @Test
  public void testExportWithNoEntries() throws ProcessingException {

    String file = doExport();

    try {
      String exportedData = IOUtility.getContent(new FileReader(file));
      Assert.assertTrue(exportedData.length() > 0);
      FMilaClientUtility.openDocument(file);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ProcessingException("File error: " + e.getMessage());
    }
  }

  @Test
  public void testExportClassWithoutCourse() throws ProcessingException {

    String file = doExport();

    try {
      String exportedData = IOUtility.getContent(new FileReader(file));
      Assert.assertTrue(exportedData.length() > 0);
      FMilaClientUtility.openDocument(file);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ProcessingException("File error: " + e.getMessage());
    }

  }

  @Test
  public void testExportWithEntries() throws ProcessingException {

    // Control
    CourseControlTestDataProvider startControl = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.StartCode.ID, 1L, "S");
    CourseControlTestDataProvider control = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.ControlCode.ID, 2L, "31");
    CourseControlTestDataProvider finishControl = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.FinishCode.ID, 3L, "Z");
    Assert.assertNotNull(startControl.getControlNo());
    Assert.assertNotNull(control.getControlNo());
    Assert.assertNotNull(finishControl.getControlNo());

    // Entry
    EntryTestDataProvider entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    // E-Card Station
    ECardStationTestDataProvider eCardStation = new ECardStationTestDataProvider();

    // Punch Session including Times
    DownloadedECardTestDataProvider download = new DownloadedECardTestDataProvider(event.getEventNr(), entry.getRaceNr(), entry.getECardNr(), eCardStation.getECardStationNr());
    Assert.assertNotNull(download.getForm().getPunchSessionNr());
    Date start = download.getForm().getStartField().getValue();

    // Punch Form
    PunchForm punch = new PunchForm();
    punch.startNew();
    punch.getPunchSessionField().setValue(download.getForm().getPunchSessionNr());
    punch.getControlNoField().setValue(control.getControlNo());
    punch.getEventField().setValue(event.getEventNr());
    punch.getTimeField().setValue(start);
    punch.getSortCodeField().setValue(1L);
    punch.doOk();

    // Validate (OK)
    BEANS.get(IRaceService.class).validateAndPersistRace(entry.getRaceNr());

    String file = doExport();

    try {
      String exportedData = IOUtility.getContent(new FileReader(file));
      Assert.assertTrue(exportedData.length() > 0);
      FMilaClientUtility.openDocument(file);
      Assert.assertTrue(exportedData.contains(entry.getForm().getLastNameField().getValue()));
      Assert.assertFalse("Undefinierter Text!", exportedData.contains("undefined text"));
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ProcessingException("File error: " + e.getMessage());
    }

    download.remove();
  }

  private String doExport() throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.OE2003ResultListCode.ID);
    Assert.assertFalse(start.getImportExportGroup().getValue());
    // TODO MIG Assert.assertFalse(start.getFileField().isTypeLoad());
    String file = IOUtility.getTempFileName(".csv");
    start.getFileField().setValue(FMilaUtility.createBinaryResource(file));
    Assert.assertTrue(start.getEventField().isEnabled());
    start.getEventField().setValue(event.getEventNr());
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

    return file;
  }

  @Test
  public void testExportSplitTimes() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"}, 0, 2000, new Integer[]{500});
    String file = doExport();
    String content = IOUtility.getContent(new FileReader(file));

    Assert.assertTrue("Split Time exists", content.endsWith(";8:20" + FMilaUtility.LINE_SEPARATOR)); // 500 seconds
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

}
