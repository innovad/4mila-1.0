package com.rtiming.client.dataexchange.iof3;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.IControlProcessService;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.event.course.ICourseProcessService;
import com.rtiming.shared.map.IMapProcessService;
import com.rtiming.shared.map.MapFormData;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;

public class CourseDataAccess {

  private final ICourseProcessService courseService;
  private final ICourseControlProcessService courseControlService;
  private final IControlProcessService controlService;
  private final IEventProcessService eventService;
  private final IMapProcessService mapService;
  private final ICodeProcessService codeService;
  private final IEventClassProcessService eventClassService;
  private final Long eventNr;

  public CourseDataAccess(Long eventNr) {
    super();
    this.eventNr = eventNr;
    this.courseService = BEANS.get(ICourseProcessService.class);
    this.courseControlService = BEANS.get(ICourseControlProcessService.class);
    this.controlService = BEANS.get(IControlProcessService.class);
    this.eventService = BEANS.get(IEventProcessService.class);
    this.mapService = BEANS.get(IMapProcessService.class);
    this.codeService = BEANS.get(ICodeProcessService.class);
    this.eventClassService = BEANS.get(IEventClassProcessService.class);
  }

  public CourseDataAccess(Long eventNr, ICourseProcessService courseService, ICourseControlProcessService courseControlService, IControlProcessService controlService, IEventProcessService eventService, IMapProcessService mapService, ICodeProcessService codeService, IEventClassProcessService eventClassService) {
    super();
    this.eventNr = eventNr;
    this.courseService = courseService;
    this.courseControlService = courseControlService;
    this.controlService = controlService;
    this.eventService = eventService;
    this.mapService = mapService;
    this.codeService = codeService;
    this.eventClassService = eventClassService;
  }

  public CourseFormData findCourse(String courseFamily) throws ProcessingException {
    return courseService.find(courseFamily, eventNr);
  }

  public CourseFormData createCourse(CourseFormData course) throws ProcessingException {
    return courseService.create(course);
  }

  public void deleteCourse(CourseFormData course, boolean includingCourse) throws ProcessingException {
    courseService.delete(course, includingCourse);
  }

  public CourseControlFormData createCourseControl(CourseControlFormData courseControlFormData) throws ProcessingException {
    return courseControlService.create(courseControlFormData);
  }

  public ControlFormData findControl(String controlNo) throws ProcessingException {
    return controlService.find(controlNo, eventNr);
  }

  public ControlFormData storeControl(ControlFormData control) throws ProcessingException {
    return controlService.store(control);
  }

  public ControlFormData createControl(ControlFormData control) throws ProcessingException {
    return controlService.create(control);
  }

  public CourseControlFormData findCourseControl(Long courseNr, Long controlNr, Long sortCode) throws ProcessingException {
    return courseControlService.find(courseNr, controlNr, sortCode);
  }

  public void storeCourse(CourseFormData course) throws ProcessingException {
    courseService.store(course);
  }

  public void georeferenceControlFromLocalPosition(Long[] controlNrs) throws ProcessingException {
    controlService.georeferenceFromLocalPosition(controlNrs, eventNr);
  }

  public MapFormData findMap(Long sessionClientNr) throws ProcessingException {
    return BeanUtility.mapBean2FormData(mapService.findMap(eventNr, sessionClientNr));
  }

  public CodeFormData findClass(String className) throws ProcessingException {
    return codeService.find(className, ClassCodeType.ID);
  }

  public CodeFormData createCode(CodeFormData code) throws ProcessingException {
    return codeService.create(code);
  }

  public EventClassFormData prepareCreateEventClass(EventClassFormData formData) throws ProcessingException {
    return eventClassService.prepareCreate(formData);
  }

  public EventClassFormData loadEventClass(EventClassFormData formData) throws ProcessingException {
    return eventClassService.load(formData);
  }

  public EventClassFormData createEventClass(EventClassFormData formData) throws ProcessingException {
    return eventClassService.create(formData);
  }

  public EventClassFormData storeEventClass(EventClassFormData formData) throws ProcessingException {
    return eventClassService.store(formData);
  }

  public EventBean loadEvent() throws ProcessingException {
    EventBean event = new EventBean();
    event.setEventNr(eventNr);
    return eventService.load(event);
  }

  public Long getEventNr() {
    return eventNr;
  }

}
