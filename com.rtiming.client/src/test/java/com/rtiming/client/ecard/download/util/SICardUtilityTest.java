package com.rtiming.client.ecard.download.util;

import java.util.Date;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.ecard.download.PunchFormData;

public class SICardUtilityTest {

  @Test(expected = ProcessingException.class)
  public void testReadV6PunchNull() throws Exception {
    SICardUtility.readV6Punch(null, 0, 0, new Date());
  }

  @Test(expected = ProcessingException.class)
  public void testReadV6PunchTooShort() throws Exception {
    SICardUtility.readV6Punch(new byte[]{0, 1}, 0, 0, new Date());
  }

  @Test
  public void testReadV6ControlNo() throws Exception {
    byte[] bytes = new byte[]{0, 33, 2, 3};
    PunchFormData punch = SICardUtility.readV6Punch(bytes, 0, 0, new Date());
    Assert.assertEquals("Control No", "33", punch.getControlNo().getValue());
  }

  @Test
  public void testReadV6RawTime() throws Exception {
    String evtZeroString = "10-11-2012 08:00:00.000";
    String punchTimeString = "10-11-2012 16:18:14.000";
    PunchFormData punch = doTestV6(evtZeroString, punchTimeString);
    Assert.assertEquals("Raw Time", punch.getRawTime().longValue(), 29894000);
  }

  @Test
  public void testReadV6EvtZero1() throws Exception {
    String evtZeroString = "10-11-2012 16:00:00.000";
    String punchTimeString = "10-11-2012 16:18:14.000";
    doTestV6(evtZeroString, punchTimeString);
  }

  @Test
  public void testReadV6EvtZero2() throws Exception {
    String evtZeroString = "10-11-2012 17:00:00.000";
    String punchTimeString = "10-11-2012 16:18:14.000";
    doTestV6(evtZeroString, punchTimeString);
  }

  @Test
  public void testReadV6EvtZero3() throws Exception {
    String evtZeroString = "10-11-2012 17:00:00.000";
    String punchTimeString = "10-11-2012 16:18:14.000";
    doTestV6(evtZeroString, punchTimeString);
  }

  @Test
  public void testReadV6EvtZero4() throws Exception {
    String evtZeroString = "10-11-2012 00:00:00.000";
    String punchTimeString = "10-11-2012 16:18:14.000";
    doTestV6(evtZeroString, punchTimeString);
  }

  @Test
  public void testReadV6EvtZero5() throws Exception {
    // TODO
    String evtZeroString = "10-11-2012 23:59:59.999";
    String punchTimeString = "10-11-2012 16:18:14.000";
    doTestV6(evtZeroString, punchTimeString);
  }

  @Test
  public void testReadV5EvtZero1() throws Exception {
    String evtZeroString = "10-11-2012 08:00:00.000";
    String punchTimeString = "10-11-2012 14:31:40.000";
    doTestV5(evtZeroString, punchTimeString);
  }

  @Test
  public void testReadV5EvtZero2() throws Exception {
    String evtZeroString = "10-11-2012 02:00:00.000";
    String punchTimeString = "10-11-2012 02:31:40.000";
    doTestV5(evtZeroString, punchTimeString);
  }

  @Test
  public void testReadV5EvtZero3() throws Exception {
    String evtZeroString = "10-11-2012 23:59:59.999";
    String punchTimeString = "10-11-2012 14:31:40.000";
    doTestV5(evtZeroString, punchTimeString);
  }

  private PunchFormData doTestV5(String evtZeroString, String punchTimeString) throws ProcessingException {
    byte[] bytes = new byte[]{34, 35, -116};
    Date evtZero = DateUtility.parse(evtZeroString, "dd-MM-yyyy HH:mm:ss.SSS");
    PunchFormData punch = SICardUtility.readV5Punch(bytes, 0, 0, evtZero);
    Assert.assertEquals("Control No", "34", punch.getControlNo().getValue());
    Date punched = FMilaUtility.addMilliSeconds(evtZero, punch.getRawTime());
    Assert.assertEquals("Time", punchTimeString, DateUtility.format(punched, "dd-MM-yyyy HH:mm:ss.SSS"));
    return punch;
  }

  private PunchFormData doTestV6(String evtZeroString, String punchTimeString) throws ProcessingException {
    byte[] bytes = new byte[]{13, 54, 60, -122};
    Date evtZero = DateUtility.parse(evtZeroString, "dd-MM-yyyy HH:mm:ss.SSS");
    PunchFormData punch = SICardUtility.readV6Punch(bytes, 0, 0, evtZero);
    Assert.assertEquals("Control No", "54", punch.getControlNo().getValue());
    Date punched = FMilaUtility.addMilliSeconds(evtZero, punch.getRawTime());
    Assert.assertEquals("Time", punchTimeString, DateUtility.format(punched, "dd-MM-yyyy HH:mm:ss.SSS"));
    return punch;
  }

}
