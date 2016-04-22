package com.rtiming.server.race.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.server.race.RaceTimeUtility;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.race.TimePrecisionCodeType;

/**
 * @author amo
 */
public class RaceTimeUtilityTest {

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateRaceControlTime1() throws Exception {
    RaceTimeUtility.updateRaceControlTime(null, null, null);
  }

  @Test
  public void testUpdateRaceControlTime2() throws Exception {
    RaceControlBean bean = new RaceControlBean();
    RaceTimeUtility.updateRaceControlTime(bean, 4L, 5L);
    Assert.assertEquals("Overall Time", 4, bean.getOverallTime().longValue());
    Assert.assertEquals("Leg Time", 5, bean.getLegTime().longValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTimeCalculationRace1() throws Exception {
    RaceTimeUtility.calculateTimes(null, null, null, 0, null);
  }

  @Test
  public void testTimeCalculationRace2() throws Exception {
    List<RaceControlBean> plannedControls = new ArrayList<RaceControlBean>();
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, null, null, 0, TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Race Status", RaceStatusCodeType.NoStartTimeCode.ID, result.getStatusUid());
  }

  @Test
  public void testTimeCalculationRaceStatus1() throws Exception {
    List<RaceControlBean> plannedControls = new ArrayList<RaceControlBean>();
    Long startTime = 0L;
    Long finishTime = 1000L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
  }

  @Test
  public void testTimeCalculationRaceStatus2() throws Exception {
    List<RaceControlBean> plannedControls = new ArrayList<RaceControlBean>();
    Long startTime = null;
    Long finishTime = 1000L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Race Status", RaceStatusCodeType.NoStartTimeCode.ID, result.getStatusUid());
  }

  @Test
  public void testTimeCalculationRaceStatus3() throws Exception {
    List<RaceControlBean> plannedControls = new ArrayList<RaceControlBean>();
    Long startTime = null;
    Long finishTime = 1000L;
    long raceStatusUid = RaceStatusCodeType.MissingPunchCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Race Status", RaceStatusCodeType.NoStartTimeCode.ID, result.getStatusUid());
  }

  @Test
  public void testTimeCalculationRaceStatus4() throws Exception {
    List<RaceControlBean> plannedControls = new ArrayList<RaceControlBean>();
    Long startTime = 0L;
    Long finishTime = 1000L;
    long raceStatusUid = RaceStatusCodeType.MissingPunchCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());
  }

  @Test
  public void testTimeCalculationRaceStatus5() throws Exception {
    List<RaceControlBean> plannedControls = new ArrayList<RaceControlBean>();
    Long startTime = 0L;
    Long finishTime = 1000L;
    long raceStatusUid = RaceStatusCodeType.DidNotFinishCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Race Status", RaceStatusCodeType.DidNotFinishCode.ID, result.getStatusUid());
  }

  @Test
  public void testTimeCalculation1() throws Exception {
    List<RaceControlBean> plannedControls = new ArrayList<RaceControlBean>();
    Long startTime = 500L;
    Long finishTime = 1800L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Leg Time", 1300, result.getLegTime().longValue());
  }

  @Test(expected = VetoException.class)
  public void testTimeCalculation2() throws Exception {
    List<RaceControlBean> plannedControls = new ArrayList<RaceControlBean>();
    Long startTime = 1800L;
    Long finishTime = 500L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1sCode.ID);
  }

  @Test
  public void testTimeCalculation3() throws Exception {
    List<RaceControlBean> plannedControls = new ArrayList<RaceControlBean>();
    RaceControlBean control1 = new RaceControlBean();
    control1.setTypeUid(ControlTypeCodeType.ControlCode.ID);
    control1.setCountLeg(true);
    control1.setPunchTime(700L);
    control1.setControlStatusUid(ControlStatusCodeType.OkCode.ID);
    plannedControls.add(control1);

    Long startTime = 500L;
    Long finishTime = 1800L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1000sCode.ID);

    Assert.assertEquals("Leg Time", 1300, result.getLegTime().longValue());
    Assert.assertEquals("Ignored Leg Time", 0, result.getIgnoredLegsTimeSum());
    Assert.assertEquals("Time", 700L, control1.getPunchTime().longValue());
    Assert.assertEquals("Overall Time", 200L, control1.getOverallTime().longValue());
    Assert.assertEquals("Leg Time", 200L, control1.getLegTime().longValue());
  }

  @Test
  public void testTimeCalculationIgnoredLegTime1() throws Exception {
    List<RaceControlBean> plannedControls = createControl(ControlTypeCodeType.ControlCode.ID, false, 700L);

    Long startTime = 500L;
    Long finishTime = 1800L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1000sCode.ID);

    Assert.assertEquals("Leg Time", 1100, result.getLegTime().longValue());
    Assert.assertEquals("Ignored Leg Time", 200, result.getIgnoredLegsTimeSum());
  }

  @Test
  public void testTimeCalculationIgnoredLegTime2() throws Exception {
    List<RaceControlBean> plannedControls = createControl(ControlTypeCodeType.ControlCode.ID, true, 700L);

    Long startTime = 500L;
    Long finishTime = 1800L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1000sCode.ID);

    Assert.assertEquals("Leg Time", 1300, result.getLegTime().longValue());
    Assert.assertEquals("Ignored Leg Time", 0, result.getIgnoredLegsTimeSum());
  }

  @Test
  public void testTimeCalculationIgnoredLegTime3() throws Exception {
    List<RaceControlBean> plannedControls = createControl(ControlTypeCodeType.ControlCode.ID, false, null);

    Long startTime = 500L;
    Long finishTime = 1800L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1000sCode.ID);

    Assert.assertEquals("Leg Time", 1300, result.getLegTime().longValue());
    Assert.assertEquals("Ignored Leg Time", 0, result.getIgnoredLegsTimeSum());
  }

  @Test
  public void testTimeCalculationIgnoredLegTime4() throws Exception {
    List<RaceControlBean> plannedControls = createControl(ControlTypeCodeType.ControlCode.ID, true, null);

    Long startTime = 500L;
    Long finishTime = 1800L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1000sCode.ID);

    Assert.assertEquals("Leg Time", 1300, result.getLegTime().longValue());
    Assert.assertEquals("Ignored Leg Time", 0, result.getIgnoredLegsTimeSum());
  }

  @Test
  public void testTimeCalculationIgnoredLegTimeFinish1() throws Exception {
    List<RaceControlBean> plannedControls = createControl(ControlTypeCodeType.FinishCode.ID, false, 1600L);

    Long startTime = 500L;
    Long finishTime = 1800L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1000sCode.ID);

    Assert.assertEquals("Leg Time", 0, result.getLegTime().longValue());
    Assert.assertEquals("Ignored Leg Time", 1300, result.getIgnoredLegsTimeSum());
  }

  @Test
  public void testTimeCalculationIgnoredLegTimeFinish2() throws Exception {
    List<RaceControlBean> plannedControls = createControl(ControlTypeCodeType.FinishCode.ID, false, null);

    Long startTime = 500L;
    Long finishTime = 1800L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1000sCode.ID);

    Assert.assertEquals("Leg Time", 0, result.getLegTime().longValue());
    Assert.assertEquals("Ignored Leg Time", 1300, result.getIgnoredLegsTimeSum());
  }

  @Test
  public void testTimeCalculationIgnoredLegTimeFinish3() throws Exception {
    List<RaceControlBean> plannedControls = createControl(ControlTypeCodeType.FinishCode.ID, true, null);

    Long startTime = 500L;
    Long finishTime = 1800L;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceValidationResult result = RaceTimeUtility.calculateTimes(plannedControls, startTime, finishTime, raceStatusUid, TimePrecisionCodeType.Precision1000sCode.ID);

    Assert.assertEquals("Leg Time", 1300, result.getLegTime().longValue());
    Assert.assertEquals("Ignored Leg Time", 0, result.getIgnoredLegsTimeSum());
  }

  private List<RaceControlBean> createControl(Long typeUid, boolean countLeg, Long time) {
    List<RaceControlBean> plannedControls = new ArrayList<RaceControlBean>();
    RaceControlBean control1 = new RaceControlBean();
    control1.setTypeUid(typeUid);
    control1.setCountLeg(countLeg);
    control1.setPunchTime(time);
    control1.setControlStatusUid(ControlStatusCodeType.OkCode.ID);
    plannedControls.add(control1);
    return plannedControls;
  }

}
