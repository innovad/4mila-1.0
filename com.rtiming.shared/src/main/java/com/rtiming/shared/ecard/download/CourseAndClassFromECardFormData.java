package com.rtiming.shared.ecard.download;

import java.util.List;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class CourseAndClassFromECardFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public CourseAndClassFromECardFormData() {
  }

  public ControlsProperty getControlsProperty() {
    return getPropertyByClass(ControlsProperty.class);
  }

  /**
   * access method for property Controls.
   */
  public List<PunchFormData> getControls() {
    return getControlsProperty().getValue();
  }

  /**
   * access method for property Controls.
   */
  public void setControls(List<PunchFormData> controls) {
    getControlsProperty().setValue(controls);
  }

  public Clazz getClazz() {
    return getFieldByClass(Clazz.class);
  }

  public Course getCourse() {
    return getFieldByClass(Course.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public class ControlsProperty extends AbstractPropertyData<List<PunchFormData>> {
    private static final long serialVersionUID = 1L;

    public ControlsProperty() {
    }
  }

  public static class Clazz extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Clazz() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Course extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Course() {
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
}
