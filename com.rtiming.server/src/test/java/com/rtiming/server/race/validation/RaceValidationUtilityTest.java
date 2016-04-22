package com.rtiming.server.race.validation;

import static com.rtiming.server.race.validation.ValidationTestUtility.buildControlList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.server.race.RaceSettings;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.race.TimePrecisionCodeType;

public class RaceValidationUtilityTest {

  @Test
  public void testAreEqualNull1() throws Exception {
    boolean equal = RaceValidationUtility.areEqual(null, null);
    Assert.assertFalse("Equal", equal);
  }

  @Test
  public void testAreEqualNull2() throws Exception {
    RaceControlBean punched = new RaceControlBean();
    boolean equal = RaceValidationUtility.areEqual(punched, null);
    Assert.assertFalse("Equal", equal);
  }

  @Test
  public void testAreEqual() throws Exception {
    RaceControlBean punched = new RaceControlBean();
    punched.setControlNo("31");
    RaceControlBean planned = new RaceControlBean();
    planned.setControlNo("31");
    boolean equal = RaceValidationUtility.areEqual(punched, planned);
    Assert.assertTrue("Equal", equal);
  }

  @Test
  public void testAreNotEqual() throws Exception {
    RaceControlBean punched = new RaceControlBean();
    punched.setControlNo("32");
    RaceControlBean planned = new RaceControlBean();
    planned.setControlNo("31");
    boolean equal = RaceValidationUtility.areEqual(punched, planned);
    Assert.assertFalse("Equal", equal);
  }

  @Test
  public void testAreEqualReplacement1() throws Exception {
    RaceControlBean punched = new RaceControlBean();
    punched.setControlNo("32");
    RaceControlBean planned = new RaceControlBean();
    planned.setControlNo("31");
    setReplacementControls(planned, "32,33");
    boolean equal = RaceValidationUtility.areEqual(punched, planned);
    Assert.assertTrue("Equal", equal);
  }

  @Test
  public void testAreEqualReplacement2() throws Exception {
    RaceControlBean punched = new RaceControlBean();
    punched.setControlNo("34");
    RaceControlBean planned = new RaceControlBean();
    planned.setControlNo("31");
    setReplacementControls(planned, "32,33");
    boolean equal = RaceValidationUtility.areEqual(punched, planned);
    Assert.assertFalse("Equal", equal);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateNull1() throws Exception {
    RaceValidationUtility.validateControls(null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateNull2() throws Exception {
    List<RaceControlBean> planned = new ArrayList<RaceControlBean>();
    RaceValidationUtility.validateControls(planned, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateNull3() throws Exception {
    List<RaceControlBean> punched = new ArrayList<RaceControlBean>();
    RaceValidationUtility.validateControls(null, punched, null, null, null);
  }

  @Test
  public void testValidateEmpty1() throws Exception {
    List<RaceControlBean> planned = new ArrayList<RaceControlBean>();
    List<RaceControlBean> punched = new ArrayList<RaceControlBean>();
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateEmpty2() throws Exception {
    List<RaceControlBean> planned = new ArrayList<RaceControlBean>();
    List<RaceControlBean> punched = buildControlList("31");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateEmpty3() throws Exception {
    List<RaceControlBean> planned = buildControlList("31");
    List<RaceControlBean> punched = new ArrayList<RaceControlBean>();
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid);
  }

  @Test
  public void testValidate1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31");
    List<RaceControlBean> punched = buildControlList("31");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidate2() throws Exception {
    List<RaceControlBean> planned = buildControlList("31");
    List<RaceControlBean> punched = buildControlList("32");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid);
  }

  @Test
  public void testValidate3() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32");
    List<RaceControlBean> punched = buildControlList("32", "31");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid);
  }

  @Test
  public void testValidate4() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32");
    List<RaceControlBean> punched = buildControlList("31", "32");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateNoStartTime1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31");
    List<RaceControlBean> punched = buildControlList("31");
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, null, 1005L, null);
    Assert.assertEquals("Race Status", RaceStatusCodeType.NoStartTimeCode.ID, result.getStatusUid());
  }

  @Test
  public void testValidateNoStartTime2() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32");
    List<RaceControlBean> punched = buildControlList("31", "32");
    setTimes(punched, 1000, 2000);
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, null, 3000L, null);
    Assert.assertNull("No Overall Time", planned.get(0).getOverallTime());
    Assert.assertNull("No Overall Time", planned.get(1).getOverallTime());
    Assert.assertNull("Leg Time", planned.get(0).getLegTime());
    Assert.assertEquals("Leg Time", 1000, planned.get(1).getLegTime().longValue());

    Assert.assertEquals("Race Status", RaceStatusCodeType.NoStartTimeCode.ID, result.getStatusUid());
    Assert.assertNull("Result Leg Time", result.getLegTime());
  }

  @Test
  public void testValidateButterfly1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "31");
    List<RaceControlBean> punched = buildControlList("31", "31");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateButterfly2() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "31");
    List<RaceControlBean> punched = buildControlList("31", "32", "31");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateButterfly3() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "33", "31", "41", "42", "31", "99");
    List<RaceControlBean> punched = buildControlList("31", "32", "33", "31", "41", "42", "31", "99");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateButterfly4() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "31");
    List<RaceControlBean> punched = buildControlList("31", "32", "33");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateButterfly5() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "33", "31", "41", "42", "31", "99");
    List<RaceControlBean> punched = buildControlList("31", "41", "42", "31", "32", "33", "31", "99");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateFreeorder1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "33");
    planned.get(1).setSortcode(10L);
    planned.get(2).setSortcode(10L);
    List<RaceControlBean> punched = buildControlList("31", "33", "32");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateFreeorder2() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "33");
    planned.get(1).setSortcode(10L);
    planned.get(2).setSortcode(10L);
    List<RaceControlBean> punched = buildControlList("31", "32", "33");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateFreeorder3() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "33", "34");
    planned.get(1).setSortcode(10L);
    planned.get(2).setSortcode(10L);
    planned.get(3).setSortcode(11L);
    List<RaceControlBean> punched = buildControlList("31", "33", "32", "34");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateFreeorder4() throws Exception {
    // (32,33) are freeorder, (34,35) are ordered
    List<RaceControlBean> planned = buildControlList("31", "32", "33", "34", "35");
    planned.get(1).setSortcode(10L);
    planned.get(2).setSortcode(10L);
    List<RaceControlBean> punched = buildControlList("31", "33", "32", "35", "34");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateFreeorder5() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32");
    planned.get(0).setSortcode(10L);
    planned.get(1).setSortcode(10L);
    List<RaceControlBean> punched = buildControlList();
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid);
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, planned.get(0).getControlStatusUid().longValue());
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, planned.get(1).getControlStatusUid().longValue());
  }

  @Test
  public void testValidateFreeorder6() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32");
    planned.get(0).setSortcode(10L);
    planned.get(1).setSortcode(10L);
    planned.get(0).setControlStatusUid(ControlStatusCodeType.InitialStatusCode.ID);
    planned.get(1).setControlStatusUid(ControlStatusCodeType.InitialStatusCode.ID);
    List<RaceControlBean> punched = buildControlList();
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid);
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, planned.get(0).getControlStatusUid().longValue());
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, planned.get(1).getControlStatusUid().longValue());
  }

  @Test
  public void testValidateMultipleFreeorder1() throws Exception {
    // (32,33) are freeorder, (35, 36) are freeorder
    List<RaceControlBean> planned = buildControlList("31", "32", "33", "34", "35", "36", "99");
    planned = ValidationTestUtility.setSortCodes(planned, 0L, 10L, 10L, 20L, 30L, 30L, 40L);
    List<RaceControlBean> punched = buildControlList("31", "35", "36", "34", "32", "33", "99");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateMultipleFreeorder2() throws Exception {
    // (32,33) are freeorder, (35, 36) are freeorder
    List<RaceControlBean> planned = buildControlList(false, "31", "32", "33", "34", "35", "36", "99");
    planned = ValidationTestUtility.setSortCodes(planned, 0L, 10L, 10L, 20L, 30L, 30L, 40L);
    List<RaceControlBean> punched = buildControlList("31", "33", "32", "34", "36", "35", "99");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateWithReplacement1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32");
    setReplacementControls(planned.get(1), "33,34");
    List<RaceControlBean> punched = buildControlList("31", "33");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateWithReplacement2() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32");
    setReplacementControls(planned.get(1), "34,35");
    List<RaceControlBean> punched = buildControlList("31", "33");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateWithReplacement3() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32");
    setReplacementControls(planned.get(1), "38,33");
    List<RaceControlBean> punched = buildControlList("31", "33");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateCorrectionByRunner1() throws Exception {
    // runner makes an order mistake, but corrects it
    List<RaceControlBean> planned = buildControlList("31", "32", "33");
    List<RaceControlBean> punched = buildControlList("31", "33", "32", "33");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Control Count", 4, planned.size());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 2));
    Assert.assertEquals("Control Status", ControlStatusCodeType.WrongCode.ID, getControlStatus(planned, 3));
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateCorrectionByRunner2() throws Exception {
    // runner makes an order mistake, but corrects it
    List<RaceControlBean> planned = buildControlList("31", "32", "33", "34");
    List<RaceControlBean> punched = buildControlList("31", "33", "34", "32", "33", "34");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Control Count", 6, planned.size());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 2));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 3));
    Assert.assertEquals("Control Status", ControlStatusCodeType.WrongCode.ID, getControlStatus(planned, 4));
    Assert.assertEquals("Control Status", ControlStatusCodeType.WrongCode.ID, getControlStatus(planned, 5));
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
  }

  @Test
  public void testValidateCorrectionByRunner3Wrong() throws Exception {
    // runner makes an order mistake, but corrects it wrong
    List<RaceControlBean> planned = buildControlList("31", "32", "33", "34");
    List<RaceControlBean> punched = buildControlList("31", "33", "34", "32", "34", "33");
    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Control Count", 7, planned.size());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 2));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 3));
    Assert.assertEquals("Control Status", ControlStatusCodeType.WrongCode.ID, getControlStatus(planned, 4));
    Assert.assertEquals("Control Status", ControlStatusCodeType.WrongCode.ID, getControlStatus(planned, 5));
    Assert.assertEquals("Control Status", ControlStatusCodeType.WrongCode.ID, getControlStatus(planned, 6));
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid);
  }

  @Test
  public void testControlStatusOk1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32");
    List<RaceControlBean> punched = buildControlList("31", "32");
    RaceValidationUtility.validateControls(planned, punched, null, null, null);
    Assert.assertEquals("Control Count", 2, planned.size());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
  }

  @Test
  public void testControlStatusOk2() throws Exception {
    List<RaceControlBean> planned = buildControlList("99", "99");
    List<RaceControlBean> punched = buildControlList("99", "99");
    RaceValidationUtility.validateControls(planned, punched, null, null, null);
    Assert.assertEquals("Control Count", 2, planned.size());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
  }

  @Test
  public void testControlStatusFreeorderOk1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "33");
    planned.get(0).setSortcode(0L);
    planned.get(1).setSortcode(0L);
    List<RaceControlBean> punched = buildControlList("32", "31", "33");
    RaceValidationUtility.validateControls(planned, punched, null, null, null);
    Assert.assertEquals("Control Count", 3, planned.size());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 2));
  }

  @Test
  public void testControlStatusWrongOrder1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32");
    List<RaceControlBean> punched = buildControlList("31", "33");
    RaceValidationUtility.validateControls(planned, punched, null, null, null);
    Assert.assertEquals("Control Count", 3, planned.size());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.WrongCode.ID, getControlStatus(planned, 2));
  }

  @Test
  public void testControlStatusMissing1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31");
    List<RaceControlBean> punched = buildControlList();
    Assert.assertEquals("Control Count", 1, planned.size());
    RaceValidationUtility.validateControls(planned, punched, null, null, null);
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 0));
  }

  @Test
  public void testTime1() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "32", "33");
    List<RaceControlBean> punched = buildControlList("S", "31", "32", "33");
    setTimes(punched, 0, 1000, 2000, 3000);
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 10000L, null);

    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 1).longValue());
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 2).longValue());
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 3).longValue());
    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Overall Time", 1000, getOverallTime(planned, 1).longValue());
    Assert.assertEquals("Overall Time", 2000, getOverallTime(planned, 2).longValue());
    Assert.assertEquals("Overall Time", 3000, getOverallTime(planned, 3).longValue());

    Assert.assertEquals("Total Leg Time", 10000L, result.getLegTime().longValue());
  }

  @Test(expected = VetoException.class)
  public void testFinishBeforeStart() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "32", "Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    planned.get(3).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    List<RaceControlBean> punched = buildControlList("31", "32");
    setTimes(punched, 1000, 2000);
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 8000L, 4000L, null);

    Assert.assertNull("Total Leg Time", result.getLegTime());
  }

  @Test
  public void testTimeNull1() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "32", "33");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    List<RaceControlBean> punched = buildControlList(false, "S", "31", "32", "33");
    punched.get(2).setPunchTime(2000L);
    RaceValidationUtility.validateControls(planned, punched, 0L, 10000L, null);
    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertNull("Leg Time", getLegTime(planned, 1));
    Assert.assertEquals("Leg Time", 2000, getLegTime(planned, 2).longValue());
    Assert.assertNull("Leg Time", getLegTime(planned, 3));
    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertNull("Overall Time", getLegTime(planned, 1));
    Assert.assertEquals("Overall Time", 2000, getOverallTime(planned, 2).longValue());
    Assert.assertNull("Overall Time", getLegTime(planned, 3));
  }

  @Test
  public void testTimeNull2() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "32", "Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    planned.get(3).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    List<RaceControlBean> punched = buildControlList(false, "S", "31", "32", "Z");
    punched.get(2).setPunchTime(2000L);
    RaceValidationUtility.validateControls(planned, punched, 0L, 10000L, null);

    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertNull("Leg Time", getLegTime(planned, 1));
    Assert.assertEquals("Leg Time", 2000, getLegTime(planned, 2).longValue());
    Assert.assertEquals("Leg Time", 8000, getLegTime(planned, 3).longValue());

    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertNull("Overall Time", getLegTime(planned, 1));
    Assert.assertEquals("Overall Time", 2000, getOverallTime(planned, 2).longValue());
    Assert.assertEquals("Overall Time", 10000, getOverallTime(planned, 3).longValue());
  }

  @Test
  public void testTimeCountLeg1() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "32", "33");
    planned.get(2).setCountLeg(false);
    List<RaceControlBean> punched = buildControlList("S", "31", "32", "33");
    setTimes(punched, 0, 1000, 2000, 3000);
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 100000L, null);

    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 1).longValue());
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 2).longValue());
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 3).longValue());
    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Overall Time", 1000, getOverallTime(planned, 1).longValue());
    Assert.assertEquals("Overall Time", 1000, getOverallTime(planned, 2).longValue());
    Assert.assertEquals("Overall Time", 2000, getOverallTime(planned, 3).longValue());

    Assert.assertEquals("Count Leg Minus", 1000, result.getIgnoredLegsTimeSum());
    Assert.assertEquals("Total Leg Time", 99000L, result.getLegTime().longValue());
  }

  @Test
  public void testTimeCountLeg2() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "32", "33");
    planned.get(0).setCountLeg(false);
    planned.get(1).setCountLeg(false);
    planned.get(2).setCountLeg(false);
    planned.get(3).setCountLeg(false);
    List<RaceControlBean> punched = buildControlList("S", "31", "32", "33");
    setTimes(punched, 0, 1000, 2000, 3000);
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 3520L, null);

    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 1).longValue());
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 2).longValue());
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 3).longValue());
    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 1).longValue());
    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 2).longValue());
    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 3).longValue());

    Assert.assertEquals("Count Leg Minus", 3000, result.getIgnoredLegsTimeSum());
    Assert.assertEquals("Total Leg Time", 520L, result.getLegTime().longValue());
  }

  @Test
  public void testTimeCountLegFinish() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "32", "Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    planned.get(3).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    planned.get(3).setCountLeg(false);
    List<RaceControlBean> punched = buildControlList("31", "32");
    setTimes(punched, 500, 1000);
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 8000L, null);

    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 500, getLegTime(planned, 1).longValue());
    Assert.assertEquals("Leg Time", 500, getLegTime(planned, 2).longValue());
    Assert.assertEquals("Leg Time", 7000, getLegTime(planned, 3).longValue());
    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Overall Time", 500, getOverallTime(planned, 1).longValue());
    Assert.assertEquals("Overall Time", 1000, getOverallTime(planned, 2).longValue());
    Assert.assertEquals("Overall Time", 1000, getOverallTime(planned, 3).longValue());

    Assert.assertEquals("Count Leg Minus", 7000, result.getIgnoredLegsTimeSum());
  }

  @Test
  public void testTimeCountLegStart() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "32", "Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    planned.get(0).setCountLeg(false);
    planned.get(3).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    List<RaceControlBean> punched = buildControlList("31", "32");
    setTimes(punched, 500, 1000);
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 200L, 8000L, null);

    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 300, getLegTime(planned, 1).longValue());
    Assert.assertEquals("Leg Time", 500, getLegTime(planned, 2).longValue());
    Assert.assertEquals("Leg Time", 7000, getLegTime(planned, 3).longValue());
    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Overall Time", 300, getOverallTime(planned, 1).longValue());
    Assert.assertEquals("Overall Time", 800, getOverallTime(planned, 2).longValue());
    Assert.assertEquals("Overall Time", 7800, getOverallTime(planned, 3).longValue());

    Assert.assertEquals("Count Leg Minus", 0, result.getIgnoredLegsTimeSum());
  }

  @Test
  public void testTimeControlsWithWrongTime1() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "32", "33");
    List<RaceControlBean> punched = buildControlList("S", "31", "32", "33");
    setTimes(punched, 0, 2000, 1000, 3000);
    RaceValidationUtility.validateControls(planned, punched, 0L, Long.MAX_VALUE, null);
    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 2000, getLegTime(planned, 1).longValue());
    Assert.assertNull("Leg Time", getLegTime(planned, 2));
    Assert.assertEquals("Leg Time", 2000, getLegTime(planned, 3).longValue());
    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Overall Time", 2000, getOverallTime(planned, 1).longValue());
    Assert.assertEquals("Overall Time", 1000, getOverallTime(planned, 2).longValue());
    Assert.assertEquals("Overall Time", 3000, getOverallTime(planned, 3).longValue());
  }

  @Test
  public void testTimeMissing1() throws Exception {
    List<RaceControlBean> planned = buildControlList(false, "31", "32");
    List<RaceControlBean> punched = buildControlList(false, "31");
    setTimes(punched, 5000);
    RaceValidationUtility.validateControls(planned, punched, 0L, Long.MAX_VALUE, null);
    Assert.assertEquals("Leg Time", 5000, getLegTime(planned, 0).longValue());
    Assert.assertNull("Leg Time", getLegTime(planned, 1));
    Assert.assertEquals("Overall Time", 5000, getOverallTime(planned, 0).longValue());
    Assert.assertNull("Overall Time", getOverallTime(planned, 1));
  }

  @Test
  public void testTimeMissing2() throws Exception {
    List<RaceControlBean> planned = buildControlList(false, "31", "32");
    List<RaceControlBean> punched = buildControlList(false, "32");
    setTimes(punched, 5000);
    RaceValidationUtility.validateControls(planned, punched, 0L, Long.MAX_VALUE, null);
    Assert.assertNull("Leg Time", getLegTime(planned, 0));
    Assert.assertEquals("Leg Time", 5000, getLegTime(planned, 1).longValue());
    Assert.assertNull("Overall Time", getOverallTime(planned, 0));
    Assert.assertEquals("Overall Time", 5000, getOverallTime(planned, 1).longValue());
  }

  @Test
  public void testTimeMissing3() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32");
    List<RaceControlBean> punched = buildControlList();
    RaceValidationUtility.validateControls(planned, punched, null, null, null);
    Assert.assertNull("Leg Time", getLegTime(planned, 0));
    Assert.assertNull("Leg Time", getLegTime(planned, 1));
    Assert.assertNull("Overall Time", getOverallTime(planned, 0));
    Assert.assertNull("Overall Time", getOverallTime(planned, 1));
  }

  @Test
  public void testTimeWrongOrder1() throws Exception {
    List<RaceControlBean> planned = buildControlList(false, "31", "32");
    List<RaceControlBean> punched = buildControlList(false, "32", "31");
    setTimes(punched, 1000, 2000);
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, Long.MAX_VALUE, null);
    Assert.assertEquals("Missing Punch", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());
    Assert.assertEquals("3 planned controls", planned.size(), 3);
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.WrongCode.ID, getControlStatus(planned, 2));
    Assert.assertNull("Leg Time", getLegTime(planned, 0));
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 1).longValue());
    Assert.assertNull("Leg Time", getLegTime(planned, 2));
    Assert.assertNull("Overall Time", getOverallTime(planned, 0));
    Assert.assertEquals("Overall Time", 1000, getOverallTime(planned, 1).longValue());
    Assert.assertEquals("Overall Time", 2000, getOverallTime(planned, 2).longValue());
  }

  @Test
  public void testStartFinish1() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    planned.get(1).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    List<RaceControlBean> punched = buildControlList();
    RaceValidationUtility.validateControls(planned, punched, null, null, null);
    Assert.assertEquals("Control Count", 2, planned.size());
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 1));
  }

  @Test
  public void testStartFinish2() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "Z");
    List<RaceControlBean> punched = buildControlList("S", "Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    planned.get(1).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    RaceValidationUtility.validateControls(planned, punched, 500L, 1005L, null);
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));

    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 505, getLegTime(planned, 1).longValue());

    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Overall Time", 505, getOverallTime(planned, 1).longValue());
  }

  @Test
  public void testStartFinish3() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    planned.get(2).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    List<RaceControlBean> punched = buildControlList("31");
    punched.get(0).setPunchTime(1000L);
    RaceValidationUtility.validateControls(planned, punched, 200L, 1005L, null);

    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 2));

    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 800, getLegTime(planned, 1).longValue());
    Assert.assertEquals("Leg Time", 5, getLegTime(planned, 2).longValue());

    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Overall Time", 800, getOverallTime(planned, 1).longValue());
    Assert.assertEquals("Overall Time", 805, getOverallTime(planned, 2).longValue());
  }

  @Test
  public void testStartFinishNoStartTime() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    planned.get(2).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    List<RaceControlBean> punched = buildControlList("31");
    punched.get(0).setPunchTime(1000L);
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, null, 1005L, null);

    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));

    Assert.assertNull("Leg Time", getLegTime(planned, 0));
    Assert.assertNull("Leg Time", getLegTime(planned, 1));
    Assert.assertNull("Leg Time", getLegTime(planned, 2));

    Assert.assertNull("Overall Time", getOverallTime(planned, 0));
    Assert.assertNull("Overall Time", getOverallTime(planned, 1));
    Assert.assertNull("Overall Time", getOverallTime(planned, 2));

    Assert.assertEquals("Race Status", RaceStatusCodeType.NoStartTimeCode.ID, result.getStatusUid());
  }

  @Test
  public void testStartFinishNoFinishTime() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    planned.get(2).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    List<RaceControlBean> punched = buildControlList("31");
    punched.get(0).setPunchTime(1000L);
    RaceValidationUtility.validateControls(planned, punched, 10L, null, null);

    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 2));

    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 990, getLegTime(planned, 1).longValue());
    Assert.assertNull("Leg Time", getLegTime(planned, 2));

    Assert.assertEquals("Overall Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Overall Time", 990, getOverallTime(planned, 1).longValue());
    Assert.assertNull("Overall Time", getOverallTime(planned, 2));
  }

  @Test
  public void testFinishOnly() throws Exception {
    List<RaceControlBean> planned = buildControlList("Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    List<RaceControlBean> punched = buildControlList();
    RaceValidationUtility.validateControls(planned, punched, 10L, 5000L, null);

    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Leg Time", 4990, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Overall Time", 4990, getOverallTime(planned, 0).longValue());
  }

  @Test
  public void testShiftTime() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "31", "32", "33", "Z");
    List<RaceControlBean> punched = buildControlList("S", "31", "32", "33", "Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    punched.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    planned.get(4).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    punched.get(4).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    setTimes(punched, 0, 1000, 2000, 3000, 4000);
    planned.get(0).setShiftTime(-60L);
    planned.get(1).setShiftTime(-400L);
    planned.get(2).setShiftTime(151L);
    planned.get(4).setShiftTime(1L);
    RaceValidationUtility.validateControls(planned, punched, 0L, 100000L, null);

    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 660, getLegTime(planned, 1).longValue());
    Assert.assertEquals("Leg Time", 1551, getLegTime(planned, 2).longValue());
    Assert.assertEquals("Leg Time", 849, getLegTime(planned, 3).longValue());
    Assert.assertEquals("Leg Time", 97001, getLegTime(planned, 4).longValue());

    Assert.assertEquals("Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Time", 660, getOverallTime(planned, 1).longValue());
    Assert.assertEquals("Time", 2211, getOverallTime(planned, 2).longValue());
    Assert.assertEquals("Time", 3060, getOverallTime(planned, 3).longValue());
    Assert.assertEquals("Time", 100061, getOverallTime(planned, 4).longValue());
  }

  @Test
  public void testShiftTimeStartFinish() throws Exception {
    List<RaceControlBean> planned = buildControlList("S", "Z");
    List<RaceControlBean> punched = buildControlList("S", "Z");
    planned.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    punched.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    planned.get(1).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    punched.get(1).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    setTimes(punched, 0, 1000);
    planned.get(0).setShiftTime(500L);
    planned.get(1).setShiftTime(-500L);
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 100000L, null);

    Assert.assertEquals("Leg Time", 0, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 99000, getLegTime(planned, 1).longValue());

    Assert.assertEquals("Time", 0, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Time", 99000, getOverallTime(planned, 1).longValue());

    Assert.assertEquals("Total", 99000, result.getLegTime().longValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateRaceControlStatus1() throws Exception {
    RaceValidationUtility.updateRaceControlStatus(null, null, null, ControlStatusCodeType.OkCode.ID);
  }

  @Test
  public void testUpdateRaceControlStatus2() throws Exception {
    RaceControlBean bean = new RaceControlBean();
    RaceValidationUtility.updateRaceControlStatus(bean, null, null, ControlStatusCodeType.OkCode.ID);
  }

  @Test
  public void testUpdateRaceControlStatusAutomatic1() throws Exception {
    RaceControlBean bean = new RaceControlBean();
    bean.setManualStatus(false);
    Long previousRaceControlStatusUid = RaceStatusCodeType.OkCode.ID;
    Long plannedRaceControlStatusUid = RaceStatusCodeType.MissingPunchCode.ID;
    Long resultStatusUid = RaceValidationUtility.updateRaceControlStatus(bean, previousRaceControlStatusUid, plannedRaceControlStatusUid, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Result Status", plannedRaceControlStatusUid, resultStatusUid);
  }

  @Test
  public void testUpdateRaceControlStatusAutomatic2() throws Exception {
    RaceControlBean bean = new RaceControlBean();
    bean.setManualStatus(false);
    Long previousRaceControlStatusUid = RaceStatusCodeType.OkCode.ID;
    Long plannedRaceControlStatusUid = RaceStatusCodeType.OkCode.ID;
    Long resultStatusUid = RaceValidationUtility.updateRaceControlStatus(bean, previousRaceControlStatusUid, plannedRaceControlStatusUid, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Result Status", plannedRaceControlStatusUid, resultStatusUid);
  }

  @Test
  public void testUpdateRaceControlStatusAutomatic3() throws Exception {
    RaceControlBean bean = new RaceControlBean();
    bean.setManualStatus(false);
    Long previousRaceControlStatusUid = RaceStatusCodeType.MissingPunchCode.ID;
    Long plannedRaceControlStatusUid = RaceStatusCodeType.MissingPunchCode.ID;
    Long resultStatusUid = RaceValidationUtility.updateRaceControlStatus(bean, previousRaceControlStatusUid, plannedRaceControlStatusUid, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Result Status", plannedRaceControlStatusUid, resultStatusUid);
  }

  @Test
  public void testUpdateRaceControlStatusManual1() throws Exception {
    RaceControlBean bean = createManualRaceControlBeanWithStatus(ControlStatusCodeType.OkCode.ID);
    Long previousRaceControlStatusUid = RaceStatusCodeType.OkCode.ID;
    Long plannedRaceControlStatusUid = RaceStatusCodeType.OkCode.ID;
    Long resultStatusUid = RaceValidationUtility.updateRaceControlStatus(bean, previousRaceControlStatusUid, plannedRaceControlStatusUid, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Result Status", previousRaceControlStatusUid, resultStatusUid);
  }

  @Test
  public void testUpdateRaceControlStatusManual2() throws Exception {
    RaceControlBean bean = createManualRaceControlBeanWithStatus(ControlStatusCodeType.MissingCode.ID);
    Long previousRaceControlStatusUid = RaceStatusCodeType.OkCode.ID;
    Long plannedRaceControlStatusUid = RaceStatusCodeType.OkCode.ID;
    Long resultStatusUid = RaceValidationUtility.updateRaceControlStatus(bean, previousRaceControlStatusUid, plannedRaceControlStatusUid, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Result Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid.longValue());
  }

  @Test
  public void testUpdateRaceControlStatusManual3() throws Exception {
    RaceControlBean bean = createManualRaceControlBeanWithStatus(ControlStatusCodeType.MissingCode.ID);
    Long previousRaceControlStatusUid = RaceStatusCodeType.MissingPunchCode.ID;
    Long plannedRaceControlStatusUid = RaceStatusCodeType.OkCode.ID;
    Long resultStatusUid = RaceValidationUtility.updateRaceControlStatus(bean, previousRaceControlStatusUid, plannedRaceControlStatusUid, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Result Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid.longValue());
  }

  @Test
  public void testUpdateRaceControlStatusManual4() throws Exception {
    RaceControlBean bean = createManualRaceControlBeanWithStatus(ControlStatusCodeType.MissingCode.ID);
    Long previousRaceControlStatusUid = RaceStatusCodeType.MissingPunchCode.ID;
    Long plannedRaceControlStatusUid = RaceStatusCodeType.MissingPunchCode.ID;
    Long resultStatusUid = RaceValidationUtility.updateRaceControlStatus(bean, previousRaceControlStatusUid, plannedRaceControlStatusUid, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Result Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid.longValue());
  }

  @Test
  public void testUpdateRaceControlStatusManual5() throws Exception {
    RaceControlBean bean = createManualRaceControlBeanWithStatus(ControlStatusCodeType.WrongCode.ID);
    Long previousRaceControlStatusUid = RaceStatusCodeType.OkCode.ID;
    Long plannedRaceControlStatusUid = RaceStatusCodeType.OkCode.ID;
    Long resultStatusUid = RaceValidationUtility.updateRaceControlStatus(bean, previousRaceControlStatusUid, plannedRaceControlStatusUid, ControlStatusCodeType.OkCode.ID);
    Assert.assertEquals("Result Status", RaceStatusCodeType.MissingPunchCode.ID, resultStatusUid.longValue());
  }

  @Test
  public void testTimeFreeorder1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "33");
    planned.get(1).setSortcode(10L);
    planned.get(2).setSortcode(10L);
    List<RaceControlBean> punched = buildControlList("31", "33", "32");
    setTimes(punched, 1000, 2000, 3000);
    RaceValidationUtility.validateControls(planned, punched, 0L, null, null);
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 0).longValue());
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 1).longValue());
    Assert.assertEquals("Leg Time", 1000, getLegTime(planned, 2).longValue());
    Assert.assertEquals("Overall Time", 1000, getOverallTime(planned, 0).longValue());
    Assert.assertEquals("Overall Time", 2000, getOverallTime(planned, 1).longValue());
    Assert.assertEquals("Overall Time", 3000, getOverallTime(planned, 2).longValue());
  }

  @Test
  public void testTimeFreeOrder2() throws Exception {
    List<RaceControlBean> planned = ValidationTestUtility.buildFreeOrderControlList("31", "32", "33");
    List<RaceControlBean> punched = buildControlList("33", "32", "31");
    setTimes(punched, 1500, 3020, 7000);

    long resultStatusUid = RaceValidationUtility.validateControls(planned, punched, 0L, null, null).getStatusUid();
    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, resultStatusUid);
    Assert.assertEquals("Control No", "33", planned.get(0).getControlNo());
    Assert.assertEquals("Control No", "32", planned.get(1).getControlNo());
    Assert.assertEquals("Control No", "31", planned.get(2).getControlNo());
    Assert.assertEquals("Legtime 1", 1500, planned.get(0).getLegTime().longValue());
    Assert.assertEquals("Legtime 2", 1520, planned.get(1).getLegTime().longValue());
    Assert.assertEquals("Legtime 3", 3980, planned.get(2).getLegTime().longValue());
  }

  @Test
  public void testMissingPunchesMinimized1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "33", "34");
    List<RaceControlBean> punched = buildControlList("32", "32", "33", "34");
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 1000L, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Missing Punch", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 2));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 3));
    Assert.assertEquals("Control Status", ControlStatusCodeType.WrongCode.ID, getControlStatus(planned, 4));
  }

  @Test
  public void testMissingPunchesMinimized2() throws Exception {
    List<RaceControlBean> planned = buildControlList("100", "200", "131", "136", "100", "142", "131", "147", "144", "135", "131");
    List<RaceControlBean> punched = buildControlList("200", "100", "131", "199", "136", "99", "142", "147", "144", "135", "131");
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 1000L, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Missing Punch", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());

    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 0)); // 100
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1)); // 200
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 2)); // 131
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 3)); // 136
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 4)); // 100
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 5)); // 142
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 6)); // 131
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 7)); // 147
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 8)); // 144
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 9)); // 135
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 10)); // 131

    Assert.assertEquals("Control Status", ControlStatusCodeType.WrongCode.ID, getControlStatus(planned, 11)); // 200
    Assert.assertEquals("Control Status", ControlStatusCodeType.AdditionalCode.ID, getControlStatus(planned, 12)); // 199
    Assert.assertEquals("Control Status", ControlStatusCodeType.WrongCode.ID, getControlStatus(planned, 13)); // 99 
  }

  @Test
  public void testMissingPunchesMinimized3() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "31", "33", "31");
    List<RaceControlBean> punched = buildControlList("32", "31", "33", "31");
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 1000L, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Missing Punch", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());

    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 2));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 3));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 4));
  }

  @Test
  public void testMissingPunchesMinimized4() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "31", "33", "31");
    List<RaceControlBean> punched = buildControlList("31", "32", "31", "33");
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 1000L, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Missing Punch", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());

    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 2));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 3));
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 4));
  }

  @Test
  public void testMissingPunchesMinimized5() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "31", "33", "31");
    List<RaceControlBean> punched = buildControlList("31", "32", "33", "31");
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 1000L, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Missing Punch", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());

    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, getControlStatus(planned, 2));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 3));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 4));
  }

  @Test
  public void testMandatory1() throws Exception {
    List<RaceControlBean> planned = buildControlList("31");
    planned.get(0).setMandatory(false);
    List<RaceControlBean> punched = buildControlList();
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 1000L, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Control Status", ControlStatusCodeType.InitialStatusCode.ID, getControlStatus(planned, 0));
  }

  @Test
  public void testMandatory2() throws Exception {
    List<RaceControlBean> planned = buildControlList("31");
    planned.get(0).setMandatory(false);
    List<RaceControlBean> punched = buildControlList("31");
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 1000L, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
  }

  @Test
  public void testMandatory3() throws Exception {
    List<RaceControlBean> planned = buildControlList("31", "32", "33");
    planned.get(1).setMandatory(false);
    List<RaceControlBean> punched = buildControlList("31", "33");
    RaceValidationResult result = RaceValidationUtility.validateControls(planned, punched, 0L, 1000L, TimePrecisionCodeType.Precision1sCode.ID);

    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 0));
    Assert.assertEquals("Control Status", ControlStatusCodeType.InitialStatusCode.ID, getControlStatus(planned, 1));
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, getControlStatus(planned, 2));
  }

  @Test(expected = VetoException.class)
  public void testValidateRace1() throws Exception {
    // no punch session, throw Veto message
    RaceValidationUtility.validateRace(null, null, null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateRace2() throws Exception {
    RaceValidationUtility.validateRace(1L, null, null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateRace3() throws Exception {
    RaceValidationUtility.validateRace(1L, null, null, null, null, null, new RaceSettings());
  }

  @Test
  public void testValidateRace4() throws Exception {
    List<RaceControlBean> planned = ValidationTestUtility.buildControlList();
    List<RaceControlBean> punched = ValidationTestUtility.buildControlList();
    RaceValidationResult result = RaceValidationUtility.validateRace(1L, planned, punched, null, null, null, new RaceSettings());
    Assert.assertEquals("No Start Time", RaceStatusCodeType.NoStartTimeCode.ID, result.getStatusUid());
  }

  @Test
  public void testValidateRaceStartTime1() throws Exception {
    List<RaceControlBean> planned = ValidationTestUtility.buildControlList();
    List<RaceControlBean> punched = ValidationTestUtility.buildControlList();
    RaceValidationResult result = RaceValidationUtility.validateRace(1L, planned, punched, 1000L, null, null, new RaceSettings());
    Assert.assertEquals("OK", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Punch Start Time", 1000, result.getStartTime().longValue());
  }

  @Test
  public void testValidateRaceStartTime2() throws Exception {
    List<RaceControlBean> planned = ValidationTestUtility.buildControlList();
    List<RaceControlBean> punched = ValidationTestUtility.buildControlList();
    RaceValidationResult result = RaceValidationUtility.validateRace(1L, planned, punched, 1000L, 1500L, null, new RaceSettings());
    Assert.assertEquals("OK", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Punch Start Time", 1000, result.getStartTime().longValue());
  }

  @Test
  public void testValidateRaceStartTime3() throws Exception {
    List<RaceControlBean> planned = ValidationTestUtility.buildControlList();
    List<RaceControlBean> punched = ValidationTestUtility.buildControlList();
    RaceValidationResult result = RaceValidationUtility.validateRace(1L, planned, punched, 1000L, 1500L, null, new RaceSettings());
    Assert.assertEquals("OK", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Punch Start Time", 1000, result.getStartTime().longValue());
  }

  @Test
  public void testValidateRaceStartTime4() throws Exception {
    List<RaceControlBean> planned = ValidationTestUtility.buildControlList();
    List<RaceControlBean> punched = ValidationTestUtility.buildControlList();
    RaceSettings settings = new RaceSettings();
    settings.setUsingStartlist(true);
    RaceValidationResult result = RaceValidationUtility.validateRace(1L, planned, punched, 1000L, 1500L, null, settings);
    Assert.assertEquals("OK", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Leg Start Time (Startlist)", 1500, result.getStartTime().longValue());
  }

  @Test
  public void testValidateRaceStartTime5() throws Exception {
    List<RaceControlBean> planned = ValidationTestUtility.buildControlList();
    List<RaceControlBean> punched = ValidationTestUtility.buildControlList();
    RaceSettings settings = new RaceSettings();
    settings.setUsingStartlist(true);
    RaceValidationResult result = RaceValidationUtility.validateRace(1L, planned, punched, 1000L, null, null, settings);
    Assert.assertEquals("OK", RaceStatusCodeType.NoStartTimeCode.ID, result.getStatusUid());
    Assert.assertNull("Leg Start Time (Startlist)", result.getStartTime());
  }

  private RaceControlBean createManualRaceControlBeanWithStatus(Long controlStatusUid) {
    RaceControlBean bean = new RaceControlBean();
    bean.setManualStatus(true);
    bean.setControlStatusUid(controlStatusUid);
    return bean;
  }

  private long getControlStatus(List<RaceControlBean> list, int i) {
    return list.get(i).getControlStatusUid();
  }

  private Long getLegTime(List<RaceControlBean> list, int i) {
    return list.get(i).getLegTime();
  }

  private Long getOverallTime(List<RaceControlBean> list, int i) {
    return list.get(i).getOverallTime();
  }

  private void setTimes(List<RaceControlBean> list, long... time) {
    int k = 0;
    for (RaceControlBean bean : list) {
      bean.setPunchTime(time[k]);
      k++;
    }
  }

  private void setReplacementControls(RaceControlBean bean, String replacementControls) {
    String[] parts = StringUtility.split(replacementControls, ",");
    bean.getReplacementControlNos().addAll(Arrays.asList(parts));
  }

}
