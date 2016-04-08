package com.rtiming.client.dataexchange.iof3;

import java.util.HashSet;

import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseFormData;

/**
 * 
 */
public class CourseImportResult {

  private CourseFormData courseFormData;
  private final HashSet<CourseControlFormData> courseControls = new HashSet<>();

  public CourseFormData getCourseFormData() {
    return courseFormData;
  }

  public void setCourseFormData(CourseFormData courseFormData) {
    this.courseFormData = courseFormData;
  }

  public boolean addCourseControl(CourseControlFormData formData) {
    return courseControls.add(formData);
  }

  public HashSet<CourseControlFormData> getCourseControls() {
    return courseControls;
  }

}
