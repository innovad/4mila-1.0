package com.rtiming.server.race.validation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;

import org.junit.Assert;

public class LevenshteinUtilityTest {

  @Test(expected = IllegalArgumentException.class)
  public void testMatrixNull1() throws Exception {
    LevenshteinUtility.calculateMatrix(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMatrixNull2() throws Exception {
    List<RaceControlBean> planned = new ArrayList<RaceControlBean>();
    LevenshteinUtility.calculateMatrix(planned, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMatrixNull3() throws Exception {
    List<RaceControlBean> punched = new ArrayList<RaceControlBean>();
    LevenshteinUtility.calculateMatrix(null, punched);
  }

  @Test
  public void testMatrix() throws Exception {
    List<RaceControlBean> planned = new ArrayList<RaceControlBean>();
    planned.add(new RaceControlBean());
    List<RaceControlBean> punched = new ArrayList<RaceControlBean>();
    punched.add(new RaceControlBean());
    int[][] matrix = LevenshteinUtility.calculateMatrix(planned, punched);

    Assert.assertEquals("Matrix size", 2, matrix.length);
    Assert.assertEquals("Matrix size", 2, matrix[0].length);
  }

  @Test
  public void testManualControl1() throws Exception {
    LevenshteinResult result = doManualControlStatusTestNoPunch(true, false, ControlStatusCodeType.MissingCode.ID);
    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, result.getRaceStatusUid());
  }

  @Test
  public void testManualControl2() throws Exception {
    LevenshteinResult result = doManualControlStatusTestNoPunch(true, true, ControlStatusCodeType.MissingCode.ID);
    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, result.getRaceStatusUid());
  }

  @Test
  public void testManualControl3() throws Exception {
    LevenshteinResult result = doManualControlStatusTestNoPunch(true, true, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
  }

  @Test
  public void testManualControl4() throws Exception {
    LevenshteinResult result = doManualControlStatusTestNoPunch(true, false, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, result.getRaceStatusUid());
  }

  @Test
  public void testManualControl5() throws Exception {
    LevenshteinResult result = doManualControlStatusTestWithPunch("31", "31", true, false, null);
    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
  }

  @Test
  public void testManualControl6() throws Exception {
    LevenshteinResult result = doManualControlStatusTestWithPunch("31", "31", true, true, ControlStatusCodeType.MissingCode.ID);
    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, result.getRaceStatusUid());
  }

  @Test
  public void testManualControl7() throws Exception {
    LevenshteinResult result = doManualControlStatusTestWithPunch("31", "31", true, true, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
  }

  @Test
  public void testManualControl8() throws Exception {
    LevenshteinResult result = doManualControlStatusTestWithPunch("31", "31", true, false, null);
    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
  }

  @Test
  public void testManualControl9() throws Exception {
    LevenshteinResult result = doManualControlStatusTestWithPunch("31", "32", true, true, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
  }

  @Test
  public void testManualControl10() throws Exception {
    LevenshteinResult result = doManualControlStatusTestWithPunch("31", "32", true, false, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, result.getRaceStatusUid());
  }

  @Test
  public void testManualControl11() throws Exception {
    LevenshteinResult result = doManualControlStatusTestWithPunch("31", "32", true, true, ControlStatusCodeType.MissingCode.ID);
    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, result.getRaceStatusUid());
  }

  private LevenshteinResult doManualControlStatusTestNoPunch(boolean mandatory, boolean manualStatus, Long controlStatusUid) {
    List<RaceControlBean> planned = new ArrayList<RaceControlBean>();
    List<RaceControlBean> punched = new ArrayList<RaceControlBean>();
    RaceControlBean control = new RaceControlBean();
    control.setMandatory(mandatory);
    control.setManualStatus(manualStatus);
    control.setControlStatusUid(controlStatusUid);
    planned.add(control);
    int[][] matrix = LevenshteinUtility.calculateMatrix(planned, punched);
    LevenshteinResult result = LevenshteinUtility.backtrace(planned, punched, matrix);
    return result;
  }

  private LevenshteinResult doManualControlStatusTestWithPunch(String controlNo1, String controlNo2, boolean mandatory, boolean manualStatus, Long controlStatusUid) {
    List<RaceControlBean> planned = new ArrayList<RaceControlBean>();
    List<RaceControlBean> punched = new ArrayList<RaceControlBean>();

    RaceControlBean control1 = new RaceControlBean();
    control1.setMandatory(mandatory);
    control1.setControlNo(controlNo1);
    control1.setManualStatus(manualStatus);
    control1.setControlStatusUid(controlStatusUid);
    control1.setTypeUid(ControlTypeCodeType.ControlCode.ID);
    planned.add(control1);

    RaceControlBean control2 = new RaceControlBean();
    control2.setControlNo(controlNo2);
    control2.setTypeUid(ControlTypeCodeType.ControlCode.ID);
    punched.add(control2);

    int[][] matrix = LevenshteinUtility.calculateMatrix(planned, punched);
    LevenshteinResult result = LevenshteinUtility.backtrace(planned, punched, matrix);
    return result;
  }

}
