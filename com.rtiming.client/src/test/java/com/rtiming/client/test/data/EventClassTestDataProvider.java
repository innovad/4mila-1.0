package com.rtiming.client.test.data;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.event.EventClassForm;
import com.rtiming.client.event.EventClassForm.MainBox.ClazzField;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

public class EventClassTestDataProvider extends AbstractTestDataProvider<EventClassForm> {

  private Long eventNr;
  private Long classUid;
  private Long courseNr;
  private Long typeUid;
  private Long parentUid;
  private Long sortCode;
  private List<FieldValue> fieldValues;
  private Long courseGenerationUid;

  public EventClassTestDataProvider(Long eventNr, Long classUid, Long courseNr, Long typeUid, Long parentUid, Long sortCode, Long courseGenerationUid) throws ProcessingException {
    super();
    this.eventNr = eventNr;
    this.classUid = classUid;
    this.courseNr = courseNr;
    this.typeUid = typeUid;
    this.parentUid = parentUid;
    this.sortCode = sortCode;
    this.courseGenerationUid = courseGenerationUid;
    callInitializer();
  }

  public EventClassTestDataProvider(Long eventNr, Long classUid, List<FieldValue> fieldValues) throws ProcessingException {
    super();
    this.eventNr = eventNr;
    this.classUid = classUid;
    this.fieldValues = fieldValues;
    callInitializer();
  }

  @Override
  protected EventClassForm createForm() throws ProcessingException {
    EventClassForm eventClass = new EventClassForm();

    eventClass.getEventField().setValue(eventNr);
    eventClass.getParentField().setValue(parentUid);
    eventClass.startNew();
    if (fieldValues != null) {
      fieldValues.add(new FieldValue(ClazzField.class, classUid));
      FormTestUtility.fillFormFields(eventClass, fieldValues.toArray(new FieldValue[fieldValues.size()]));
    }
    else {
      eventClass.getClazzField().setValue(classUid);
      eventClass.getCourseField().setValue(courseNr);
      eventClass.getTypeField().setValue(typeUid);
      if (sortCode != null) {
        eventClass.getSortCodeField().setValue(sortCode);
      }
    }
    if (courseGenerationUid != null) {
      eventClass.getCourseGenerationTypeField().setValue(courseGenerationUid);
    }
    else {
      eventClass.getCourseGenerationTypeField().setValue(CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);
    }
    eventClass.doOk();

    return eventClass;
  }

  @Override
  public void remove() throws ProcessingException {
  }

}
