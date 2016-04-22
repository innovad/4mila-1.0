package com.rtiming.client.test.data;

import java.util.Date;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.event.EventForm;
import com.rtiming.client.event.course.CourseForm;
import com.rtiming.client.settings.CodeForm;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

public class EventWithIndividualClassTestDataProvider extends EventTestDataProvider {

  private CodeForm clazz;
  private CourseForm course;
  private Long courseGenerationTypeUid;

  public EventWithIndividualClassTestDataProvider() throws ProcessingException {
    super();
  }

  protected EventWithIndividualClassTestDataProvider(boolean callInitializer, Long courseGenerationTypeUid) throws ProcessingException {
    super(callInitializer);
    this.courseGenerationTypeUid = courseGenerationTypeUid;
  }

  public EventWithIndividualClassTestDataProvider(Date evtZero, Date evtFinish) throws ProcessingException {
    super(evtZero, evtFinish);
  }

  @Override
  protected EventForm createForm() throws ProcessingException {
    EventForm form = super.createForm();

    // create class, course, runner, entry here
    // Class
    clazz = FMilaClientTestUtility.createClass();

    // Course
    course = FMilaClientTestUtility.createCourse(form.getEventNr());

    // Event Class
    if (courseGenerationTypeUid == null) {
      courseGenerationTypeUid = CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID;
    }
    FMilaClientTestUtility.createEventClass(form.getEventNr(), clazz.getCodeUid(), course.getCourseNr(), ClassTypeCodeType.IndividualEventCode.ID, courseGenerationTypeUid);

    return form;
  }

  @Override
  public void remove() throws ProcessingException {
    super.remove();
  }

  public Long getClassUid() {
    return clazz.getCodeUid();
  }

  public Long getCourseNr() {
    return course.getCourseNr();
  }

}
