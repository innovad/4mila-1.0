package com.rtiming.client.test.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.event.EventClassForm;
import com.rtiming.client.event.EventForm;
import com.rtiming.client.event.course.CourseForm;
import com.rtiming.client.settings.CodeForm;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

public class EventWithRelayClassTestDataProvider extends EventTestDataProvider {

  private final int legCount;
  private final CodeForm[] legs;
  private final CourseForm[] courses;

  private CodeForm clazz;

  public EventWithRelayClassTestDataProvider(int legCount) throws ProcessingException {
    super(false);
    this.legCount = legCount;
    legs = new CodeForm[legCount];
    courses = new CourseForm[legCount];
    callInitializer();
  }

  public EventWithRelayClassTestDataProvider() throws ProcessingException {
    this(3);
  }

  @Override
  protected EventForm createForm() throws ProcessingException {
    EventForm form = super.createForm();

    // create class, course, runner, entry here
    // Class
    clazz = FMilaClientTestUtility.createClass();
    for (int i = 0; i < legCount; i++) {
      legs[i] = FMilaClientTestUtility.createClass();
    }

    // Course
    for (int i = 0; i < legCount; i++) {
      courses[i] = FMilaClientTestUtility.createCourse(form.getEventNr());
    }

    // Event Class
    EventClassForm eventClass = FMilaClientTestUtility.createEventClass(form.getEventNr(), clazz.getCodeUid(), null, ClassTypeCodeType.RelayCode.ID, CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);
    for (int i = 0; i < legCount; i++) {
      FMilaClientTestUtility.createEventClassLeg(form.getEventNr(), legs[i].getCodeUid(), courses[i].getCourseNr(), ClassTypeCodeType.RelayLegCode.ID, eventClass.getClazzField().getValue(), Long.valueOf(i), CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);
    }

    return form;
  }

  @Override
  public void remove() throws ProcessingException {
    super.remove();
  }

  public Long getParentUid() {
    return clazz.getCodeUid();
  }

  public Long getLegUid(int leg) {
    return legs[leg].getCodeUid();
  }

  public Long[] getLegUids() {
    List<Long> result = new ArrayList<Long>();
    for (CodeForm leg : legs) {
      result.add(leg.getCodeUid());
    }
    return result.toArray(new Long[result.size()]);
  }

  public Long getCourseNr(int leg) {
    return courses[leg].getCourseNr();
  }

}
