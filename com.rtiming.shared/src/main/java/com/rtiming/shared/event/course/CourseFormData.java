package com.rtiming.shared.event.course;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class CourseFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public CourseFormData() {
  }

  public CourseNrProperty getCourseNrProperty() {
    return getPropertyByClass(CourseNrProperty.class);
  }

  /**
   * access method for property CourseNr.
   */
  public Long getCourseNr() {
    return getCourseNrProperty().getValue();
  }

  /**
   * access method for property CourseNr.
   */
  public void setCourseNr(Long courseNr) {
    getCourseNrProperty().setValue(courseNr);
  }

  public Climb getClimb() {
    return getFieldByClass(Climb.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public Length getLength() {
    return getFieldByClass(Length.class);
  }

  public Shortcut getShortcut() {
    return getFieldByClass(Shortcut.class);
  }

  public class CourseNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public CourseNrProperty() {
    }
  }

  public static class Climb extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Climb() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Event extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Event() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Length extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Length() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Shortcut extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Shortcut() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
