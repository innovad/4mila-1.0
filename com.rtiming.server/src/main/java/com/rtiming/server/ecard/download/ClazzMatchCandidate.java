package com.rtiming.server.ecard.download;

import java.util.List;

import com.rtiming.shared.event.course.CourseControlRowData;

public class ClazzMatchCandidate {

  private final Long classUid;
  private final Long courseNr;
  private final List<List<CourseControlRowData>> courses;

  public ClazzMatchCandidate(Long classUid, Long courseNr, List<List<CourseControlRowData>> courses) {
    super();
    this.classUid = classUid;
    this.courseNr = courseNr;
    this.courses = courses;
  }

  public List<List<CourseControlRowData>> getCourses() {
    return courses;
  }

  public Long getClassUid() {
    return classUid;
  }

  public Long getCourseNr() {
    return courseNr;
  }

}
