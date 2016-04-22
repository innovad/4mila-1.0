package com.rtiming.client.common.ui;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author amo
 */
public class TimeDifferenceUtilityTest {

  @Test
  public void testFormatValue1() throws Exception {
    String formatted = TimeDifferenceUtility.formatValue(null);
    Assert.assertEquals("Empty String", "", formatted);
  }

  @Test
  public void testFormatValue2() throws Exception {
    String formatted = TimeDifferenceUtility.formatValue(1L);
    Assert.assertEquals("Formatted String", "0.001 s", formatted);
  }

  @Test
  public void testFormatValue3() throws Exception {
    String formatted = TimeDifferenceUtility.formatValue(1001L);
    Assert.assertEquals("Formatted String", "1.001 s", formatted);
  }

  @Test
  public void testFormatValue4() throws Exception {
    String formatted = TimeDifferenceUtility.formatValue(-0L);
    Assert.assertEquals("Formatted String", "0.000 s", formatted);
  }

  @Test
  public void testFormatValue5() throws Exception {
    String formatted = TimeDifferenceUtility.formatValue(-555L);
    Assert.assertEquals("Formatted String", "-0.555 s", formatted);
  }

  @Test
  public void testParseValue1() throws Exception {
    Long result = TimeDifferenceUtility.parseValue(null);
    Assert.assertNull("Result", result);
  }

  @Test
  public void testParseValue2() throws Exception {
    Long result = TimeDifferenceUtility.parseValue("0");
    Assert.assertEquals("Parsed Value", 0, result.longValue());
  }

  @Test
  public void testParseValue3() throws Exception {
    Long result = TimeDifferenceUtility.parseValue("1");
    Assert.assertEquals("Parsed Value", 1000, result.longValue());
  }

  @Test
  public void testParseValue4() throws Exception {
    Long result = TimeDifferenceUtility.parseValue("1s");
    Assert.assertEquals("Parsed Value", 1000, result.longValue());
  }

  @Test
  public void testParseValue5() throws Exception {
    Long result = TimeDifferenceUtility.parseValue("1 s");
    Assert.assertEquals("Parsed Value", 1000, result.longValue());
  }

  @Test
  public void testParseValue6() throws Exception {
    Long result = TimeDifferenceUtility.parseValue("1  s");
    Assert.assertEquals("Parsed Value", 1000, result.longValue());
  }

  @Test
  public void testParseValue7() throws Exception {
    Long result = TimeDifferenceUtility.parseValue("1,555 s");
    Assert.assertEquals("Parsed Value", 1555, result.longValue());
  }

  @Test(expected = ProcessingException.class)
  public void testParseValue8() throws Exception {
    TimeDifferenceUtility.parseValue("notparseable");
  }

  @Test
  public void testParseValue9() throws Exception {
    Long result = TimeDifferenceUtility.parseValue("  1.515  s");
    Assert.assertEquals("Parsed Value", 1515, result.longValue());
  }

  @Test
  public void testParseValue10() throws Exception {
    Long result = TimeDifferenceUtility.parseValue("-.001s");
    Assert.assertEquals("Parsed Value", -1, result.longValue());
  }

  @Test
  public void testParseValue11() throws Exception {
    Long result = TimeDifferenceUtility.parseValue(" - 25s");
    Assert.assertEquals("Parsed Value", -25000, result.longValue());
  }

}
