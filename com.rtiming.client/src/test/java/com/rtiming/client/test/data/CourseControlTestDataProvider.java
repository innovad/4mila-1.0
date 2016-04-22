package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.Assert;

import com.rtiming.client.event.course.ControlForm;
import com.rtiming.client.event.course.CourseControlForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.IControlProcessService;

public class CourseControlTestDataProvider extends AbstractTestDataProvider<CourseControlForm> {

  private final Long eventNr;
  private final Long courseNr;
  private final Long typeUid;
  private final Long sortCode;
  private final String controlNo;
  private final Long loopMasterCourseControlNr;
  private final String loopVariantCode;
  private final Long loopTypeUid;

  private ControlForm control;

  public CourseControlTestDataProvider(Long eventNr, Long courseNr, Long typeUid, Long sortCode, String controlNo) throws ProcessingException {
    this(eventNr, courseNr, typeUid, sortCode, controlNo, null, null, null);
  }

  public CourseControlTestDataProvider(Long eventNr, Long courseNr, Long typeUid, Long sortCode, String controlNo, Long loopTypeUid, Long loopMasterCourseControlNr, String loopVariantCode) throws ProcessingException {
    this.eventNr = eventNr;
    this.courseNr = courseNr;
    this.typeUid = typeUid;
    this.sortCode = sortCode;
    this.controlNo = controlNo;
    this.loopMasterCourseControlNr = loopMasterCourseControlNr;
    this.loopVariantCode = loopVariantCode;
    this.loopTypeUid = loopTypeUid;
    callInitializer();
  }

  @Override
  protected CourseControlForm createForm() throws ProcessingException {

    Assert.assertNotNull(eventNr);
    Assert.assertNotNull(courseNr);
    Assert.assertNotNull(typeUid);

    // Control
    ControlFormData controlFormData = BEANS.get(IControlProcessService.class).find(controlNo, eventNr);
    Long controlNr = controlFormData.getControlNr();
    if (controlFormData.getControlNr() == null) {
      control = new ControlForm();
      control.getEventField().setValue(eventNr);
      control.startNew();
      FormTestUtility.fillFormFields(control, new FieldValue(ControlForm.MainBox.NumberField.class, controlNo));
      control.getTypeField().setValue(typeUid); // force type due to control no intelligence
      control.doOk();
      controlNr = control.getControlNr();
    }
    Assert.assertNotNull("Control loaded or created", controlNr);

    // Start Course
    CourseControlForm courseControl = new CourseControlForm();
    courseControl.startNew();
    courseControl.setEventNr(eventNr);
    courseControl.getCourseField().setValue(courseNr);
    courseControl.getControlField().setValue(controlNr);
    courseControl.getSortCodeField().setValue(sortCode);
    courseControl.getForkTypeField().setValue(loopTypeUid);
    courseControl.getForkMasterCourseControlField().setValue(loopMasterCourseControlNr);
    courseControl.getForkVariantCodeField().setValue(loopVariantCode);
    courseControl.doOk();

    return courseControl;
  }

  @Override
  public void remove() throws ProcessingException {

  }

  public String getControlNo() {
    return controlNo;
  }

}
