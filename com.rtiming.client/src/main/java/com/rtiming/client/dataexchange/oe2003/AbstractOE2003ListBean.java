package com.rtiming.client.dataexchange.oe2003;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.dataexchange.CSVElement;
import com.rtiming.shared.dataexchange.oe2003.AbstractOE2003Bean;

public abstract class AbstractOE2003ListBean extends AbstractOE2003Bean {

  private static final long serialVersionUID = 1L;

  public AbstractOE2003ListBean(Long primaryKeyNr) {
    super(primaryKeyNr);
  }

  @CSVElement(value = 39, title = "OE2003_CourseNr")
  private String courseNumber;

  @CSVElement(value = 40, title = "OE2003_Course")
  private String courseName;

  @CSVElement(value = 41, title = "OE2003_km")
  private String courseLength;

  @CSVElement(value = 42, title = "OE2003_Hm")
  private String courseClimb;

  @CSVElement(value = 43, title = "OE2003_ControlCount")
  private String courseNoOfControls;

  @Override
  public List<Object> getData() {
    List<Object> list = super.getData();
    list.add(courseNumber);
    list.add(courseName);
    list.add(courseLength);
    list.add(courseClimb);
    list.add(courseNoOfControls);
    return list;
  }

  @Override
  public void setData(List<Object> data) {
    courseNumber = (String) data.get(38);
    courseName = (String) data.get(39);
    courseLength = (String) data.get(40);
    courseClimb = (String) data.get(41);
    courseNoOfControls = (String) data.get(42);
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {

  }

  public void setCourseNumber(String courseNumber) {
    this.courseNumber = courseNumber;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public void setCourseLength(String courseLength) {
    this.courseLength = courseLength;
  }

  public void setCourseClimb(String courseClimb) {
    this.courseClimb = courseClimb;
  }

  public void setCourseNoOfControls(String courseNoOfControls) {
    this.courseNoOfControls = courseNoOfControls;
  }

}
