package com.rtiming.client.dataexchange.oe2003;

import java.io.FileReader;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardCancelButton;
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
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm;
import com.rtiming.client.dataexchange.DataExchangePreviewForm;
import com.rtiming.client.dataexchange.DataExchangeStartForm;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.client.event.EventClassForm;
import com.rtiming.client.settings.CodeForm;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class OE2003StartlistExporterTest {

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
    start.getFormatField().setValue(ImportExportFormatCodeType.OE2003StartListCode.ID);

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

    CodeForm clazz = FMilaClientTestUtility.createClass();
    EventClassForm eventClass = FMilaClientTestUtility.createEventClass(event.getEventNr(), clazz.getCodeUid(), null, ClassTypeCodeType.IndividualEventCode.ID, CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);
    Assert.assertNull(eventClass.getCourseField().getValue());

    EntryTestDataProvider entry = new EntryTestDataProvider(event.getEventNr(), eventClass.getClazzField().getValue());

    String file = doExport();

    try {
      String exportedData = IOUtility.getContent(new FileReader(file));
      Assert.assertTrue(exportedData.length() > 0);
      FMilaClientUtility.openDocument(file);
      Assert.assertTrue(exportedData.contains(entry.getForm().getLastNameField().getValue()));
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ProcessingException("File error: " + e.getMessage());
    }

  }

  @Test
  public void testExportWithEntries() throws ProcessingException {

    EntryTestDataProvider entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    String file = doExport();

    try {
      String exportedData = IOUtility.getContent(new FileReader(file));
      Assert.assertTrue(exportedData.length() > 0);
      FMilaClientUtility.openDocument(file);
      Assert.assertTrue(exportedData.contains(entry.getForm().getLastNameField().getValue()));
      Assert.assertEquals("Row count", 2, exportedData.split(FMilaUtility.LINE_SEPARATOR).length);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ProcessingException("File error: " + e.getMessage());
    }
  }

  private String doExport() throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.OE2003StartListCode.ID);
    start.getCharacterSetField().setValue("UTF-8");
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

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

}
