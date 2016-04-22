package com.rtiming.client.event.course;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.ICourseProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class CourseFormTest extends AbstractFormTest<CourseForm> {

  private EventTestDataProvider event;

  @Override
  public void setUpForm() throws ProcessingException {
    event = new EventTestDataProvider();
    super.setUpForm();
  }

  @Override
  protected CourseForm getStartedForm() throws ProcessingException {
    CourseForm form = new CourseForm();
    form.getEventField().setValue(event.getEventNr());
    form.startNew();
    return form;
  }

  @Override
  protected CourseForm getModifyForm() throws ProcessingException {
    CourseForm form = new CourseForm();
    form.getEventField().setValue(event.getEventNr());
    form.setCourseNr(getForm().getCourseNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    event.remove();
  }

  @Test
  public void testFind() throws ProcessingException {
    FormTestUtility.fillFormFields(getForm());
    getForm().getShortcutField().setValue("COURSE0001JUnit");
    getForm().getEventField().setValue(event.getEventNr());
    getForm().doOk();

    CourseFormData formData = BEANS.get(ICourseProcessService.class).find("COURSE0001JUnit", event.getEventNr());
    Assert.assertEquals(getForm().getCourseNr(), formData.getCourseNr());
    Assert.assertEquals(getForm().getEventField().getValue(), formData.getEvent().getValue());
    Assert.assertEquals(getForm().getClimbField().getValue(), formData.getClimb().getValue());
    Assert.assertEquals(getForm().getLengthField().getValue(), formData.getLength().getValue());
  }

  @Test
  public void testFindNoResult() throws ProcessingException {
    CourseFormData formData = BEANS.get(ICourseProcessService.class).find("COURSE0001JUnitNoResult", event.getEventNr());
    Assert.assertNull(formData.getCourseNr());
    Assert.assertEquals("COURSE0001JUnitNoResult", formData.getShortcut().getValue());
  }

}
