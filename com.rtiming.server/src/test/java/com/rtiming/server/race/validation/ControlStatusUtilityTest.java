package com.rtiming.server.race.validation;

import org.junit.Assert;
import org.junit.Test;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;

/**
 * @author amo
 */
public class ControlStatusUtilityTest {

  @Test(expected = IllegalArgumentException.class)
  public void testSetControlStatus1() throws Exception {
    ControlStatusUtility.setControlStatus(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetControlStatus2() throws Exception {
    ControlStatusUtility.setControlStatus(new RaceControlBean(), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetControlStatus3() throws Exception {
    ControlStatusUtility.setControlStatus(null, ControlStatusCodeType.OkCode.ID);
  }

  @Test
  public void testSetControlStatus4() throws Exception {
    RaceControlBean control = new RaceControlBean();
    control.setManualStatus(false);
    Long targetStatusUid = ControlStatusCodeType.MissingCode.ID;
    ControlStatusUtility.setControlStatus(control, targetStatusUid);
    Assert.assertEquals("Status set", targetStatusUid, control.getControlStatusUid());
  }

  @Test
  public void testSetControlStatus5() throws Exception {
    RaceControlBean control = new RaceControlBean();
    control.setManualStatus(true);
    Long targetStatusUid = ControlStatusCodeType.MissingCode.ID;
    ControlStatusUtility.setControlStatus(control, targetStatusUid);
    Assert.assertNull("Status NOT set", control.getControlStatusUid());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetMissingControlStatus1() throws Exception {
    ControlStatusUtility.setMissingControlStatus(null, RaceStatusCodeType.OkCode.ID);
  }

  @Test
  public void testSetMissingControlStatus2() throws Exception {
    RaceControlBean missing = new RaceControlBean();
    long result = ControlStatusUtility.setMissingControlStatus(missing, RaceStatusCodeType.OkCode.ID);
    Assert.assertEquals("Result", result, RaceStatusCodeType.OkCode.ID);
  }

  @Test
  public void testSetMissingControlStatus3() throws Exception {
    RaceControlBean missing = new RaceControlBean();
    long result = ControlStatusUtility.setMissingControlStatus(missing, RaceStatusCodeType.DidNotFinishCode.ID);
    Assert.assertEquals("Result", result, RaceStatusCodeType.DidNotFinishCode.ID);
  }

  @Test
  public void testSetMissingControlStatus4() throws Exception {
    RaceControlBean missing = new RaceControlBean();
    missing.setMandatory(false);
    missing.setControlStatusUid(ControlStatusCodeType.MissingCode.ID);
    long result = ControlStatusUtility.setMissingControlStatus(missing, RaceStatusCodeType.DidNotFinishCode.ID);
    Assert.assertEquals("Result", result, RaceStatusCodeType.DidNotFinishCode.ID);
  }

  @Test
  public void testSetMissingControlStatus5() throws Exception {
    RaceControlBean missing = new RaceControlBean();
    missing.setMandatory(true);
    missing.setControlStatusUid(ControlStatusCodeType.MissingCode.ID);
    long result = ControlStatusUtility.setMissingControlStatus(missing, RaceStatusCodeType.OkCode.ID);
    Assert.assertEquals("Result", result, RaceStatusCodeType.MissingPunchCode.ID);
  }

  @Test
  public void testSetMissingControlStatus6() throws Exception {
    RaceControlBean missing = new RaceControlBean();
    missing.setMandatory(false);
    missing.setControlStatusUid(ControlStatusCodeType.MissingCode.ID);
    long result = ControlStatusUtility.setMissingControlStatus(missing, RaceStatusCodeType.OkCode.ID);
    Assert.assertEquals("Result", result, RaceStatusCodeType.OkCode.ID);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateRaceStatusFromManualControlStatusNull() throws Exception {
    ControlStatusUtility.calculateRaceStatusFromManualControlStatus(0L, null);
  }

  @Test
  public void testCalculateRaceStatusFromManualControlStatus1() throws Exception {
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceControlBean planned = new RaceControlBean();
    long result = ControlStatusUtility.calculateRaceStatusFromManualControlStatus(raceStatusUid, planned);
    Assert.assertEquals("OK", raceStatusUid, result);
  }

  @Test
  public void testCalculateRaceStatusFromManualControlStatus2() throws Exception {
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    RaceControlBean planned = new RaceControlBean();
    planned.setControlStatusUid(ControlStatusCodeType.MissingCode.ID);
    planned.setMandatory(true);
    long result = ControlStatusUtility.calculateRaceStatusFromManualControlStatus(raceStatusUid, planned);
    Assert.assertEquals("MP", RaceStatusCodeType.MissingPunchCode.ID, result);
  }

  @Test
  public void testCalculateRaceStatusFromManualControlStatus3() throws Exception {
    long raceStatusUid = RaceStatusCodeType.DidNotStartCode.ID;
    RaceControlBean planned = new RaceControlBean();
    planned.setControlStatusUid(ControlStatusCodeType.MissingCode.ID);
    planned.setMandatory(true);
    long result = ControlStatusUtility.calculateRaceStatusFromManualControlStatus(raceStatusUid, planned);
    Assert.assertEquals("MP", RaceStatusCodeType.DidNotStartCode.ID, result);
  }

}
