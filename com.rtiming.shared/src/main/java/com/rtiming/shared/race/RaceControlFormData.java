package com.rtiming.shared.race;

import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.race.RaceControlForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class RaceControlFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public Control getControl() {
    return getFieldByClass(Control.class);
  }

  public ControlStatus getControlStatus() {
    return getFieldByClass(ControlStatus.class);
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

  public CourseControlNrProperty getCourseControlNrProperty() {
    return getPropertyByClass(CourseControlNrProperty.class);
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

  public LegStartTimeProperty getLegStartTimeProperty() {
    return getPropertyByClass(LegStartTimeProperty.class);
  }

  public ManualStatus getManualStatus() {
    return getFieldByClass(ManualStatus.class);
  }

  public Race getRace() {
    return getFieldByClass(Race.class);
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

  public RaceControlNrProperty getRaceControlNrProperty() {
    return getPropertyByClass(RaceControlNrProperty.class);
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

  public static class Control extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class ControlStatus extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class CourseControlNrProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class LegStartTimeProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class ManualStatus extends AbstractValueFieldData<Boolean> {

    private static final long serialVersionUID = 1L;
  }

  public static class Race extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class RaceControlNrProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class ShiftTime extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class SortCode extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class Time extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class ZeroTime extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }
}
