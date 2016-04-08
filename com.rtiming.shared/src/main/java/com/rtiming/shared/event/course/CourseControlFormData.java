package com.rtiming.shared.event.course;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class CourseControlFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public CourseControlFormData() {
  }

  public CourseControlNrProperty getCourseControlNrProperty() {
    return getPropertyByClass(CourseControlNrProperty.class);
  }

  /**
   * access method for property CourseControlNr.
   */
  public Long getCourseControlNr() {
    return getCourseControlNrProperty().getValue();
  }

  /**
   * access method for property CourseControlNr.
   */
  public void setCourseControlNr(Long courseControlNr) {
    getCourseControlNrProperty().setValue(courseControlNr);
  }

  public EventNrProperty getEventNrProperty() {
    return getPropertyByClass(EventNrProperty.class);
  }

  /**
   * access method for property EventNr.
   */
  public Long getEventNr() {
    return getEventNrProperty().getValue();
  }

  /**
   * access method for property EventNr.
   */
  public void setEventNr(Long eventNr) {
    getEventNrProperty().setValue(eventNr);
  }

  public Control getControl() {
    return getFieldByClass(Control.class);
  }

  public CountLeg getCountLeg() {
    return getFieldByClass(CountLeg.class);
  }

  public Course getCourse() {
    return getFieldByClass(Course.class);
  }

  public ForkMasterCourseControl getForkMasterCourseControl() {
    return getFieldByClass(ForkMasterCourseControl.class);
  }

  public ForkType getForkType() {
    return getFieldByClass(ForkType.class);
  }

  public ForkVariantCode getForkVariantCode() {
    return getFieldByClass(ForkVariantCode.class);
  }

  public Mandatory getMandatory() {
    return getFieldByClass(Mandatory.class);
  }

  public SortCode getSortCode() {
    return getFieldByClass(SortCode.class);
  }

  public class CourseControlNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public CourseControlNrProperty() {
    }
  }

  public class EventNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public EventNrProperty() {
    }
  }

  public static class Control extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Control() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class CountLeg extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public CountLeg() {
    }
  }

  public static class Course extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Course() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class ForkMasterCourseControl extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ForkMasterCourseControl() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class ForkType extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ForkType() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class ForkVariantCode extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public ForkVariantCode() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Mandatory extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public Mandatory() {
    }
  }

  public static class SortCode extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public SortCode() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
