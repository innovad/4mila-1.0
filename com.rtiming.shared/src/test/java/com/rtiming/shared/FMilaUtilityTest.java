package com.rtiming.shared;

import java.util.Date;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.race.TimePrecisionCodeType;

public class FMilaUtilityTest {

  @Test(expected = VetoException.class)
  public void testValidateEmailAddresses() throws Exception {
    FMilaUtility.validateEmailAddresses("amo");
  }

  @Test
  public void testFormatTime1() throws Exception {
    String result = FMilaUtility.formatTime(1000L, TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Formatted Time", "0:01", result);
  }

  @Test
  public void testFormatTime2() throws Exception {
    String result = FMilaUtility.formatTime(120 * 1000L, TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Formatted Time", "2:00", result);
  }

  @Test
  public void testFormatTime3() throws Exception {
    String result = FMilaUtility.formatTime(3600 * 1000L, TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Formatted Time", "1:00:00", result);
  }

  @Test
  public void testFormatTime4() throws Exception {
    String result = FMilaUtility.formatTime(25 * 3600 * 1000L, TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Formatted Time", "25:00:00", result);
  }

  @Test
  public void testFormatTime5() throws Exception {
    String result = FMilaUtility.formatTime(50000 * 3600 * 1000L, TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Formatted Time", "50000:00:00", result);
  }

  @Test
  public void testFormatTime10s1() throws Exception {
    String result = FMilaUtility.formatTime(333333L, TimePrecisionCodeType.Precision10sCode.ID);
    Assert.assertEquals("Formatted Time", "5:33.3", result);
  }

  @Test
  public void testFormatTime100s1() throws Exception {
    String result = FMilaUtility.formatTime(333333L, TimePrecisionCodeType.Precision100sCode.ID);
    Assert.assertEquals("Formatted Time", "5:33.33", result);
  }

  @Test
  public void testFormatTime1000s1() throws Exception {
    String result = FMilaUtility.formatTime(333333L, TimePrecisionCodeType.Precision1000sCode.ID);
    Assert.assertEquals("Formatted Time", "5:33.333", result);
  }

  @Test
  public void testFormatTimeRounding1() throws Exception {
    String result = FMilaUtility.formatTime(333999L, TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Formatted Time", "5:33", result);
  }

  @Test
  public void testFormatTimeRounding2() throws Exception {
    String result = FMilaUtility.formatTime(333000L, TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Formatted Time", "5:33", result);
  }

  @Test
  public void testValidateYear1() throws Exception {
    long result = FMilaUtility.validateYear(99L);
    Assert.assertEquals("Validated Year", 1999, result);
  }

  @Test
  public void testValidateYear2() throws Exception {
    long result = FMilaUtility.validateYear(00L);
    Assert.assertEquals("Validated Year", 2000, result);
  }

  @Test
  public void testValidateYear3() throws Exception {
    long result = FMilaUtility.validateYear(5L);
    Assert.assertEquals("Validated Year", 2005, result);
  }

  @Test
  public void testValidateYearNull() throws Exception {
    Long result = FMilaUtility.validateYear(null);
    Assert.assertNull("Validated Year", result);
  }

  @Test
  public void testValidateYearOther() throws Exception {
    long result = FMilaUtility.validateYear(123L);
    Assert.assertEquals("Validated Year", 123, result);
  }

  @Test
  public void testAddMilliseconds() throws Exception {
    Date date = new Date();
    Date result = FMilaUtility.addMilliSeconds(date, 1000L);
    Assert.assertEquals("Sum", FMilaUtility.addSeconds(date, 1), result);
  }

  @Test
  public void testRoundTime1() throws Exception {
    Long result = FMilaUtility.roundTime(12345L, TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Rounded Time", 12000, result.longValue());
  }

  @Test
  public void testRoundTime2() throws Exception {
    Long result = FMilaUtility.roundTime(12345L, TimePrecisionCodeType.Precision10sCode.ID);
    Assert.assertEquals("Rounded Time", 12300, result.longValue());
  }

  @Test
  public void testRoundTime3() throws Exception {
    Long result = FMilaUtility.roundTime(12345L, TimePrecisionCodeType.Precision100sCode.ID);
    Assert.assertEquals("Rounded Time", 12340, result.longValue());
  }

  @Test
  public void testRoundTime4() throws Exception {
    Long result = FMilaUtility.roundTime(12345L, TimePrecisionCodeType.Precision1000sCode.ID);
    Assert.assertEquals("Rounded Time", 12345, result.longValue());
  }

  @Test
  public void testRoundTimeNull() throws Exception {
    Long result = FMilaUtility.roundTime(null, TimePrecisionCodeType.Precision1000sCode.ID);
    Assert.assertNull("Rounded Time", result);
  }

}
