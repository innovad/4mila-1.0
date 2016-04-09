package com.rtiming.server.ecard.download;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateCourseAndClassFromECardPermission;
import com.rtiming.shared.common.security.permission.ReadCourseAndClassFromECardPermission;
import com.rtiming.shared.ecard.download.CourseAndClassFromECardFormData;
import com.rtiming.shared.ecard.download.ICourseAndClassFromECardProcessService;
import com.rtiming.shared.ecard.download.PunchFormData;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.IControlProcessService;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.event.course.ICourseProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;

public class CourseAndClassFromECardProcessService  implements ICourseAndClassFromECardProcessService {

  @Override
  public CourseAndClassFromECardFormData prepareCreate(CourseAndClassFromECardFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCourseAndClassFromECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getEvent().setValue(BEANS.get(IDefaultProcessService.class).getDefaultEventNr());

    return formData;
  }

  @Override
  public CourseAndClassFromECardFormData create(CourseAndClassFromECardFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCourseAndClassFromECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // Course
    CourseFormData course = new CourseFormData();
    course = BEANS.get(ICourseProcessService.class).prepareCreate(course);
    course.getEvent().setValue(formData.getEvent().getValue());
    course.getShortcut().setValue(formData.getCourse().getValue());
    course = BEANS.get(ICourseProcessService.class).create(course);

    // Class
    EventClassFormData clazz = new EventClassFormData();
    clazz.getEvent().setValue(formData.getEvent().getValue());
    clazz = BEANS.get(IEventClassProcessService.class).prepareCreate(clazz);
    clazz.getCourse().setValue(course.getCourseNr());
    clazz.getClazz().setValue(formData.getClazz().getValue());
    clazz.getType().setValue(ClassTypeCodeType.IndividualEventCode.ID);
    clazz.setStartlistSettingNr(null);
    clazz = BEANS.get(IEventClassProcessService.class).create(clazz);

    // Start
    createAndAssignControl("S", ControlTypeCodeType.StartCode.ID, formData.getEvent().getValue(), course.getCourseNr());

    // Control
    for (PunchFormData punch : formData.getControls()) {
      createAndAssignControl(punch.getControlNo().getValue(), ControlTypeCodeType.ControlCode.ID, formData.getEvent().getValue(), course.getCourseNr());
    }

    // Finish
    createAndAssignControl("F", ControlTypeCodeType.FinishCode.ID, formData.getEvent().getValue(), course.getCourseNr());

    return formData;
  }

  @Override
  public CourseAndClassFromECardFormData load(CourseAndClassFromECardFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadCourseAndClassFromECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    return formData;
  }

  @Override
  public CourseAndClassFromECardFormData store(CourseAndClassFromECardFormData formData) throws ProcessingException {
    return formData;
  }

  private void createAndAssignControl(String controlNo, long id, Long eventNr, Long courseNr) throws ProcessingException {
    ControlFormData start = BEANS.get(IControlProcessService.class).find(controlNo, eventNr);
    if (start.getControlNr() == null) {
      start.getType().setValue(id);
      start.getActive().setValue(true);
      start = BEANS.get(IControlProcessService.class).create(start);
    }

    CourseControlFormData sc = new CourseControlFormData();
    sc.setEventNr(eventNr);
    sc.getCourse().setValue(courseNr);
    sc = BEANS.get(ICourseControlProcessService.class).prepareCreate(sc);
    if (id == ControlTypeCodeType.StartCode.ID) {
      sc.getSortCode().setValue(0L);
    }
    sc.getControl().setValue(start.getControlNr());
    sc = BEANS.get(ICourseControlProcessService.class).create(sc);
  }

}
