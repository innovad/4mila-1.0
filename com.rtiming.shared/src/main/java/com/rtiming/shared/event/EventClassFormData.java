package com.rtiming.shared.event;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class EventClassFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public EventClassFormData() {
  }

  public ClientNrProperty getClientNrProperty() {
    return getPropertyByClass(ClientNrProperty.class);
  }

  /**
   * access method for property ClientNr.
   */
  public Long getClientNr() {
    return getClientNrProperty().getValue();
  }

  /**
   * access method for property ClientNr.
   */
  public void setClientNr(Long clientNr) {
    getClientNrProperty().setValue(clientNr);
  }

  public StartlistSettingNrProperty getStartlistSettingNrProperty() {
    return getPropertyByClass(StartlistSettingNrProperty.class);
  }

  /**
   * access method for property StartlistSettingNr.
   */
  public Long getStartlistSettingNr() {
    return getStartlistSettingNrProperty().getValue();
  }

  /**
   * access method for property StartlistSettingNr.
   */
  public void setStartlistSettingNr(Long startlistSettingNr) {
    getStartlistSettingNrProperty().setValue(startlistSettingNr);
  }

  public Clazz getClazz() {
    return getFieldByClass(Clazz.class);
  }

  public Course getCourse() {
    return getFieldByClass(Course.class);
  }

  public CourseGenerationType getCourseGenerationType() {
    return getFieldByClass(CourseGenerationType.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public FeeGroup getFeeGroup() {
    return getFieldByClass(FeeGroup.class);
  }

  public Parent getParent() {
    return getFieldByClass(Parent.class);
  }

  public SortCode getSortCode() {
    return getFieldByClass(SortCode.class);
  }

  public TeamSizeMax getTeamSizeMax() {
    return getFieldByClass(TeamSizeMax.class);
  }

  public TeamSizeMin getTeamSizeMin() {
    return getFieldByClass(TeamSizeMin.class);
  }

  public TimePrecision getTimePrecision() {
    return getFieldByClass(TimePrecision.class);
  }

  public Type getType() {
    return getFieldByClass(Type.class);
  }

  public class ClientNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public ClientNrProperty() {
    }
  }

  public class StartlistSettingNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public StartlistSettingNrProperty() {
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

  public static class Course extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Course() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class CourseGenerationType extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public CourseGenerationType() {
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

  public static class FeeGroup extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public FeeGroup() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Parent extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Parent() {
    }

  }

  public static class SortCode extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public SortCode() {
    }
  }

  public static class TeamSizeMax extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public TeamSizeMax() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class TeamSizeMin extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public TeamSizeMin() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class TimePrecision extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public TimePrecision() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Type extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Type() {
    }

    /**
     * list of derived validation rules.
     */
 
  }
}
