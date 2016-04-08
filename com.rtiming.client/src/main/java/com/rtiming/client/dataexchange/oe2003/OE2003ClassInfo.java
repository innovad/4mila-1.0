package com.rtiming.client.dataexchange.oe2003;

import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.settings.CodeFormData;

public class OE2003ClassInfo {

  private EventClassFormData clazz;
  private CodeFormData clazzCode;
  private CourseFormData course;
  private Long controlCount;
  private Long languageUid;

  public EventClassFormData getClazz() {
    return clazz;
  }

  public void setClazz(EventClassFormData clazz) {
    this.clazz = clazz;
  }

  public CodeFormData getClazzCode() {
    return clazzCode;
  }

  public void setClazzCode(CodeFormData clazzCode) {
    this.clazzCode = clazzCode;
  }

  public CourseFormData getCourse() {
    return course;
  }

  public void setCourse(CourseFormData course) {
    this.course = course;
  }

  public Long getControlCount() {
    return controlCount;
  }

  public void setControlCount(Long controlCount) {
    this.controlCount = controlCount;
  }

  public void setLanguageUid(Long languageUid) {
    this.languageUid = languageUid;
  }

  public Long getLanguageUid() {
    return languageUid;
  }

}
