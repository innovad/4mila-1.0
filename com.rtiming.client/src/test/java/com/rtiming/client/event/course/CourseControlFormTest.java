package com.rtiming.client.event.course;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.event.EventForm;
import com.rtiming.client.event.EventForm.MainBox.FinishTimeField;
import com.rtiming.client.event.EventForm.MainBox.ZeroTimeField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.FormTestUtility.OrderedFieldPair;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;
import com.rtiming.shared.event.IEventProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class CourseControlFormTest extends AbstractFormTest<CourseControlForm> {

  private ControlForm control;
  private CourseForm course;
  private EventForm event;

  @Override
  public void setUpForm() throws ProcessingException {
    // event
    event = new EventForm();
    OrderedFieldPair orderedFieldPairs = new OrderedFieldPair(ZeroTimeField.class, FinishTimeField.class);
    List<OrderedFieldPair> list = new ArrayList<OrderedFieldPair>();
    list.add(orderedFieldPairs);
    FormTestUtility.fillFormFields(event, new MaxFormFieldValueProvider(), list);
    event.startNew();
    event.touch();
    event.doOk();

    // control
    control = new ControlForm();
    FormTestUtility.fillFormFields(control, new MaxFormFieldValueProvider());
    control.getEventField().setValue(event.getEventNr());
    control.startNew();
    control.touch();
    control.doOk();

    // course
    course = new CourseForm();
    FormTestUtility.fillFormFields(course, new MaxFormFieldValueProvider());
    course.getEventField().setValue(event.getEventNr());
    course.startNew();
    course.touch();
    course.doOk();

    super.setUpForm();
  }

  @Override
  protected CourseControlForm getStartedForm() throws ProcessingException {
    CourseControlForm form = new CourseControlForm();
    form.getCourseField().setValue(course.getCourseNr());
    form.startNew();
    return form;
  }

  @Override
  protected CourseControlForm getModifyForm() throws ProcessingException {
    CourseControlForm form = new CourseControlForm();
    form.setCourseControlNr(getForm().getCourseControlNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    BEANS.get(IEventProcessService.class).delete(event.getEventNr(), true);
  }

}
