package com.rtiming.client.dataexchange.iof;

import java.net.URL;

import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardCancelButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardFinishButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardNextStepButton;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm;
import com.rtiming.client.dataexchange.DataExchangePreviewForm;
import com.rtiming.client.dataexchange.DataExchangeStartForm;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.client.event.course.ControlsTablePage;
import com.rtiming.client.event.course.CourseControlsTablePage;
import com.rtiming.client.event.course.CoursesTablePage;
import com.rtiming.client.map.MapsTablePage;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestClientSession.class)
public class IOF203CourseImporterTest {

  private EventTestDataProvider event;

  @Before
  public void before() throws ProcessingException {
    event = new EventTestDataProvider();
  }

  @Test
  public void testDialog() throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard203CourseDataCode.ID);

    Assert.assertFalse(start.getLanguageField().isEnabled());
    Assert.assertTrue(start.getEventField().isEnabled());

    Assert.assertFalse(start.getImportExportGroup().isEnabled());
    Assert.assertTrue(start.getImportExportGroup().getValue());
    // Assert.assertTrue(start.getFileField().isTypeLoad());

    ((WizardCancelButton) wizard.getContainerForm().getWizardCancelButton()).doClick();
  }

  @Test
  public void testImport() throws ProcessingException {
    doImport("iof203course.xml");
    assertControlCount(1, 69, 1, 16, 1);
  }

  @Test
  public void testRepeatingImport() throws ProcessingException {
    doImport("iof203course.xml");
    assertHD10(12);
    assertControlCount(1, 69, 1, 16, 1);
    doImport("iof203course2.xml");
    assertHD10(4);
  }

  private void assertHD10(int count) throws ProcessingException {
    CoursesTablePage courses = new CoursesTablePage(event.getEventNr());
    courses.nodeAddedNotify();
    courses.loadChildren();

    Integer dh10row = null;
    for (int i = 0; i < courses.getTable().getRowCount(); i++) {
      if ("D10  H10".equalsIgnoreCase(courses.getTable().getShortcutColumn().getValue(i))) {
        dh10row = i;
        break;
      }
    }

    Long courseNr = courses.getTable().getCourseNrColumn().getValue(dh10row);
    Assert.assertNotNull("Course Found", courseNr);

    CourseControlsTablePage page = new CourseControlsTablePage(courseNr, null);
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("Number of controls", count, page.getTable().getRowCount());
  }

  @Test
  public void testImportByteOrderMarkFile() throws ProcessingException {
    doImport("iof203courseBOM.xml");
    assertControlCount(1, 27, 1, 5, 1);
  }

  private void assertControlCount(int start, int control, int finish, int course, int map) throws ProcessingException {
    // Check Data
    ControlsTablePage controls = new ControlsTablePage(event.getEventNr());
    controls.loadChildren();
    Assert.assertEquals(control + start + finish, controls.getTable().getRowCount());

    int numStart = 0;
    int numFinish = 0;
    int numControls = 0;
    for (Long typeUid : controls.getTable().getTypeColumn().getValues()) {
      if (typeUid == ControlTypeCodeType.StartCode.ID) {
        numStart++;
      }
      else if (typeUid == ControlTypeCodeType.ControlCode.ID) {
        numControls++;
      }
      else if (typeUid == ControlTypeCodeType.FinishCode.ID) {
        numFinish++;
      }
    }
    Assert.assertEquals("Start count", start, numStart);
    Assert.assertEquals("Control count", control, numControls);
    Assert.assertEquals("Finish count", finish, numFinish);

    CoursesTablePage courses = new CoursesTablePage(event.getEventNr());
    courses.loadChildren();
    Assert.assertEquals("Course Count", course, courses.getTable().getRowCount());

    MapsTablePage maps = new MapsTablePage(event.getEventNr());
    maps.nodeAddedNotify();
    maps.loadChildren();
    Assert.assertEquals("Map Count", map, maps.getTable().getRowCount());
  }

  private void doImport(String fileName) throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard203CourseDataCode.ID);
    start.getEventField().setValue(event.getEventNr());
    URL file = FMilaUtility.findFileLocation("resources//dataexchange//" + fileName, "");
    start.getFileField().setValue(FMilaUtility.createBinaryResource(file.getPath()));
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Preview Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangePreviewForm);
    ((WizardNextStepButton) wizard.getContainerForm().getWizardNextStepButton()).doClick();

    // Preview Form
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeFinalizationForm);
    DataExchangeFinalizationForm finish = (DataExchangeFinalizationForm) wizard.getWizardForm();
    ((WizardFinishButton) wizard.getContainerForm().getWizardFinishButton()).doClick();

    Assert.assertFalse(wizard.isOpen());
    String result = finish.getInfoField().getValue();
    Assert.assertTrue(result.contains(Texts.get("Error") + ": 0"));
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

}
