package com.rtiming.client.dataexchange.iof3;

import java.net.URL;
import java.util.List;

import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardCancelButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardFinishButton;
import org.eclipse.scout.rt.client.ui.wizard.DefaultWizardContainerForm.MainBox.WizardNextStepButton;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm;
import com.rtiming.client.dataexchange.DataExchangePreviewForm;
import com.rtiming.client.dataexchange.DataExchangeStartForm;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.client.event.course.ControlsTablePage;
import com.rtiming.client.event.course.CoursesTablePage;
import com.rtiming.client.event.course.EventClassesTablePage;
import com.rtiming.client.map.MapsTablePage;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.event.course.ICourseProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class IOF300CourseImporterTest {

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
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard300CourseDataCode.ID);

    Assert.assertFalse(start.getLanguageField().isEnabled());
    Assert.assertTrue(start.getEventField().isEnabled());

    Assert.assertFalse(start.getImportExportGroup().isEnabled());
    Assert.assertTrue(start.getImportExportGroup().getValue());
    // Assert.assertTrue(start.getFileField().isTypeLoad());

    ((WizardCancelButton) wizard.getContainerForm().getWizardCancelButton()).doClick();
  }

  @Test
  public void testImport() throws ProcessingException {
    doImport("iof300course.xml");
    assertControlCount(1, 6, 1, 1, 0);
    assertCourse();
    assertClasses();
  }

  @Test
  public void testImportTwice() throws ProcessingException {
    doImport("iof300course.xml");
    doImport("iof300course.xml");
    assertControlCount(1, 6, 1, 1, 0);
    assertCourse();
    assertClasses();
  }

  private void assertClasses() throws ProcessingException {
    EventClassesTablePage clazzesTablePage = new EventClassesTablePage(event.getEventNr(), null);
    clazzesTablePage.nodeAddedNotify();
    clazzesTablePage.loadChildren();

    Assert.assertEquals("Clazz Name", "Men Open", clazzesTablePage.getTable().getClazzColumn().getDisplayText(clazzesTablePage.getTable().getRow(0)));
    Assert.assertEquals("Clazz Name", "Women Open", clazzesTablePage.getTable().getClazzColumn().getDisplayText(clazzesTablePage.getTable().getRow(1)));
  }

  private void assertCourse() throws ProcessingException {
    CourseFormData course = BEANS.get(ICourseProcessService.class).find("Open long", event.getEventNr());
    Assert.assertNotNull("Course exists", course.getCourseNr());
    List<List<CourseControlRowData>> variantList = BEANS.get(ICourseControlProcessService.class).getCourses(course.getCourseNr());
    Assert.assertEquals("2 Variants", 2, variantList.size());

    assertCourseVariants(variantList, new String[][]{{"S", "31", "34", "35", "31", "32", "33", "31", "100", "F"}, {"S", "31", "32", "33", "31", "34", "35", "31", "100", "F"}
    });
  }

  private void assertCourseVariants(List<List<CourseControlRowData>> variantList, String[][] codes) throws ComparisonFailure {
    int successCounter = 0;
    ComparisonFailure lastException = null;
    for (List<CourseControlRowData> course : variantList) {
      for (String[] codeList : codes) {
        try {
          assertCourse(course, codeList);
          successCounter++;
        }
        catch (ComparisonFailure e) {
          lastException = e;
        }
      }
    }
    if (successCounter != variantList.size()) {
      throw lastException;
    }
  }

  private void assertCourse(List<CourseControlRowData> course, String... codes) {
    Assert.assertEquals("Course control count", course.size(), codes.length);
    for (int k = 0; k < course.size(); k++) {
      Assert.assertEquals("Course control", codes[k], course.get(k).getControlNo());
    }
  }

  private void assertControlCount(int start, int control, int finish, int course, int map) throws ProcessingException {
    // Check Data
    ControlsTablePage controls = new ControlsTablePage(event.getEventNr());
    controls.nodeAddedNotify();
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
    maps.loadChildren();
    Assert.assertEquals("Map Count", map, maps.getTable().getRowCount());
  }

  private void doImport(String fileName) throws ProcessingException {
    DataExchangeWizard wizard = new DataExchangeWizard();
    wizard.start();
    Assert.assertTrue(wizard.getWizardForm() instanceof DataExchangeStartForm);

    // Start Form
    DataExchangeStartForm start = (DataExchangeStartForm) wizard.getWizardForm();
    start.getFormatField().setValue(ImportExportFormatCodeType.IOFDataStandard300CourseDataCode.ID);
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
