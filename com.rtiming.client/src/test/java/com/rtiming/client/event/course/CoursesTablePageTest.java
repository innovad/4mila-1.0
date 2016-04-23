package com.rtiming.client.event.course;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.event.course.CoursesTablePage.Table.NewCoursesWithSelectedVariantsMenu;
import com.rtiming.client.event.course.variant.CourseVariantsTablePage;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.TestingRunnable;
import com.rtiming.client.test.data.CourseControlTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.ICourseProcessService;
import com.rtiming.shared.services.code.CourseForkTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class CoursesTablePageTest extends AbstractTablePageTest<CoursesTablePage> {

  @Override
  protected CoursesTablePage getTablePage() {
    return new CoursesTablePage(null);
  }

  @Test
  public void testNewCoursesWithSelectedVariantsMenu() throws Exception {
    EventWithIndividualClassTestDataProvider event = new EventWithIndividualClassTestDataProvider();
    CoursesTablePage page = new CoursesTablePage(event.getEventNr());
    page.nodeAddedNotify();
    page.loadChildren();
    Assert.assertEquals("1 Row", 1, page.getTable().getRowCount());

    createCourseControl(event.getEventNr(), event.getCourseNr(), 1L, "S");
    createCourseControl(event.getEventNr(), event.getCourseNr(), 2L, "31");

    CourseFormData course = new CourseFormData();
    course.setCourseNr(event.getCourseNr());
    course = BEANS.get(ICourseProcessService.class).load(course);
    final Long length = course.getLength().getValue();
    final Long climb = course.getClimb().getValue();

    CourseForm course2 = FMilaClientTestUtility.createCourse(event.getEventNr());
    page.nodeAddedNotify();
    page.loadChildren();
    Assert.assertEquals("2 Rows", 2, page.getTable().getRowCount());

    createCourseControl(event.getEventNr(), course2.getCourseNr(), 1L, "S");
    createCourseControl(event.getEventNr(), course2.getCourseNr(), 2L, "32");

    page.getTable().selectAllRows();

    TestingRunnable runnable = new TestingRunnable() {
      @Override
      protected void runTest() throws ProcessingException {
        // Create Course with Variants
        CourseForm courseForm = FMilaClientTestUtility.findLastAddedForm(CourseForm.class);
        Assert.assertEquals("Length", length, courseForm.getLengthField().getValue());
        Assert.assertEquals("Climb", climb, courseForm.getClimbField().getValue());
        courseForm.getShortcutField().setValue("MERGE");
        courseForm.doOk();
        Assert.assertNotNull("Course saved", courseForm.getCourseNr());

        // Test Course Controls of new course
        CourseControlsTablePage newCourse = new CourseControlsTablePage(courseForm.getCourseNr(), null);
        newCourse.nodeAddedNotify();
        newCourse.loadChildren();
        Assert.assertEquals("3 Rows", 3, newCourse.getTable().getRowCount());
        Assert.assertEquals("S", newCourse.getTable().getControlColumn().getValue(0));
        Assert.assertEquals("31", newCourse.getTable().getControlColumn().getValue(1));
        Assert.assertEquals("32", newCourse.getTable().getControlColumn().getValue(2));
        Assert.assertEquals(CourseForkTypeCodeType.ForkCode.ID, newCourse.getTable().getForkTypeColumn().getValue(0));
        Assert.assertEquals("S - 1", newCourse.getTable().getForkMasterCourseControlColumn().getValue(1));
        Assert.assertEquals("S - 1", newCourse.getTable().getForkMasterCourseControlColumn().getValue(2));

        CourseVariantsTablePage newVariants = new CourseVariantsTablePage(courseForm.getCourseNr());
        newVariants.nodeAddedNotify();
        newVariants.loadChildren();
        Assert.assertEquals("2 Rows", 2, newVariants.getTable().getRowCount());
      }
    };
    FMilaClientTestUtility.runBlockingMenu(page.getTable(), NewCoursesWithSelectedVariantsMenu.class, runnable);

    event.remove();
  }

  public void createCourseControl(Long eventNr, Long courseNr, Long sortCode, String controlNo) throws ProcessingException {
    new CourseControlTestDataProvider(eventNr, courseNr, ControlTypeCodeType.ControlCode.ID, sortCode, controlNo);
  }

}
