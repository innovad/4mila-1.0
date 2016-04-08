package com.rtiming.shared.race;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class RaceControlFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public RaceControlFormData() {
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

  public LegStartTimeProperty getLegStartTimeProperty() {
    return getPropertyByClass(LegStartTimeProperty.class);
  }

  /**
   * access method for property LegStartTime.
   */
  public Long getLegStartTime() {
    return getLegStartTimeProperty().getValue();
  }

  /**
   * access method for property LegStartTime.
   */
  public void setLegStartTime(Long legStartTime) {
    getLegStartTimeProperty().setValue(legStartTime);
  }

  public RaceControlNrProperty getRaceControlNrProperty() {
    return getPropertyByClass(RaceControlNrProperty.class);
  }

  /**
   * access method for property RaceControlNr.
   */
  public Long getRaceControlNr() {
    return getRaceControlNrProperty().getValue();
  }

  /**
   * access method for property RaceControlNr.
   */
  public void setRaceControlNr(Long raceControlNr) {
    getRaceControlNrProperty().setValue(raceControlNr);
  }

  public Control getControl() {
    return getFieldByClass(Control.class);
  }

  public ControlStatus getControlStatus() {
    return getFieldByClass(ControlStatus.class);
  }

  public ManualStatus getManualStatus() {
    return getFieldByClass(ManualStatus.class);
  }

  public Race getRace() {
    return getFieldByClass(Race.class);
  }

  public ShiftTime getShiftTime() {
    return getFieldByClass(ShiftTime.class);
  }

  public SortCode getSortCode() {
    return getFieldByClass(SortCode.class);
  }

  public Time getTime() {
    return getFieldByClass(Time.class);
  }

  public ZeroTime getZeroTime() {
    return getFieldByClass(ZeroTime.class);
  }

  public class CourseControlNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public CourseControlNrProperty() {
    }
  }

  public class LegStartTimeProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public LegStartTimeProperty() {
    }
  }

  public class RaceControlNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RaceControlNrProperty() {
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

  public static class ControlStatus extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ControlStatus() {
    }

    /**
     * list of derived validation rules.
     */
  }

  public static class ManualStatus extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public ManualStatus() {
    }
  }

  public static class Race extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Race() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class ShiftTime extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ShiftTime() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class SortCode extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public SortCode() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class Time extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public Time() {
    }
  }

  public static class ZeroTime extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public ZeroTime() {
    }

    /**
     * list of derived validation rules.
     */

  }
}
