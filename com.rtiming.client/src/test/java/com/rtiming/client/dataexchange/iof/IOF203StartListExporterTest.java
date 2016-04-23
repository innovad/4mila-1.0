package com.rtiming.client.dataexchange.iof;

import java.io.FileReader;

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

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm;
import com.rtiming.client.dataexchange.DataExchangePreviewForm;
import com.rtiming.client.dataexchange.DataExchangeStartForm;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.client.dataexchange.XMLUtility;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;
import com.rtiming.shared.dataexchange.iof203.xml.ClassStart;
import com.rtiming.shared.dataexchange.iof203.xml.Event;
import com.rtiming.shared.dataexchange.iof203.xml.StartList;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class IOF203StartListExporterTest {

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
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard203StartListCode.ID);

    Assert.assertFalse(start.getLanguageField().isEnabled());
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
      String[] check = exportedData.split("StartList");
      Assert.assertEquals(3, check.length); // only start and finish tag should exist
      FMilaClientUtility.openDocument(file);
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
      FileReader reader = new FileReader(file);
      String exportedData = IOUtility.getContent(reader);
      reader.close();
      Assert.assertTrue(exportedData.length() > 0);
      String[] check = exportedData.split("StartList");
      Assert.assertEquals(3, check.length); // only start and finish tag should exist
      Assert.assertTrue(exportedData.contains(entry.getForm().getLastNameField().getValue()));

      Object result = XMLUtility.unmarshal(file, new StartList(), false);
      Assert.assertTrue(result instanceof StartList);
      StartList startList = (StartList) result;
      Assert.assertEquals("Version", IOF203Utility.getIOFVersion203().getVersion(), startList.getIOFVersion().getVersion());
      Assert.assertEquals("1 Class", 1, startList.getClassStart().size());
      ClassStart classStart = startList.getClassStart().get(0);
      Assert.assertEquals("1 Entry", 1, classStart.getPersonStartOrTeamStart().size());
      Event iofEvent = (Event) startList.getEventIdOrEvent().get(0);
      Assert.assertEquals("Event", "" + event.getEventNr(), iofEvent.getEventId().getvalue());

      FMilaClientUtility.openDocument(file);
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
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard203StartListCode.ID);
    Assert.assertFalse(start.getImportExportGroup().getValue());
    // TODO MIG Assert.assertFalse(start.getFileField().isTypeLoad());
    String file = IOUtility.getTempFileName(".xml");
    start.getFileField().setValue(FMilaUtility.createBinaryResource((file)));
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
