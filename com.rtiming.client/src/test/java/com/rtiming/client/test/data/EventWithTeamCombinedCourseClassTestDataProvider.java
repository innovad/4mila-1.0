package com.rtiming.client.test.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.event.EventClassForm.MainBox.CourseField;
import com.rtiming.client.event.EventClassForm.MainBox.TeamSizeBox.TeamSizeMaxField;
import com.rtiming.client.event.EventClassForm.MainBox.TeamSizeBox.TeamSizeMinField;
import com.rtiming.client.event.EventClassForm.MainBox.TypeField;
import com.rtiming.client.event.EventForm;
import com.rtiming.client.event.course.CourseForm;
import com.rtiming.client.settings.CodeForm;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.event.course.ClassTypeCodeType;

public class EventWithTeamCombinedCourseClassTestDataProvider extends EventTestDataProvider {

  private CodeForm clazz;
  private CourseForm course;
  private EventClassTestDataProvider eventClass;
  private Long teamMin;
  private Long teamMax;

  public EventWithTeamCombinedCourseClassTestDataProvider(Long teamMin, Long teamMax) throws ProcessingException {
    super(false);
    this.teamMin = teamMin;
    this.teamMax = teamMax;
    callInitializer();
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
    List<FieldValue> fieldValues = new ArrayList<FieldValue>();
    fieldValues.add(new FieldValue(CourseField.class, course.getCourseNr()));
    fieldValues.add(new FieldValue(TypeField.class, ClassTypeCodeType.TeamCombinedCourseCode.ID));
    fieldValues.add(new FieldValue(TeamSizeMinField.class, teamMin));
    fieldValues.add(new FieldValue(TeamSizeMaxField.class, teamMax));
    eventClass = new EventClassTestDataProvider(form.getEventNr(), clazz.getCodeUid(), fieldValues);

    return form;
  }

  @Override
  public void remove() throws ProcessingException {
    super.remove();
    eventClass.remove();
  }

  public Long getClassUid() {
    return clazz.getCodeUid();
  }

  public Long getCourseNr() {
    return course.getCourseNr();
  }

}
