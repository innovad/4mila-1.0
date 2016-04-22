package com.rtiming.client.ranking;

import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.race.TimePrecisionCodeType;
import com.rtiming.shared.ranking.FormulaUtility;

/**
 * @author amo
 */
public class FormulaUtilityTest {

  @Test
  public void testNull() throws Exception {
    long precisionUid = FormulaUtility.decimalPlaces2timePrecision(null);
    Assert.assertEquals("Conversion", TimePrecisionCodeType.Precision1sCode.ID, precisionUid);
  }

  @Test
  public void test1s() throws Exception {
    long precisionUid = FormulaUtility.decimalPlaces2timePrecision(0L);
    Assert.assertEquals("Conversion", TimePrecisionCodeType.Precision1sCode.ID, precisionUid);
  }

  @Test
  public void test10s() throws Exception {
    long precisionUid = FormulaUtility.decimalPlaces2timePrecision(1L);
    Assert.assertEquals("Conversion", TimePrecisionCodeType.Precision10sCode.ID, precisionUid);
  }

  @Test
  public void test100s() throws Exception {
    long precisionUid = FormulaUtility.decimalPlaces2timePrecision(2L);
    Assert.assertEquals("Conversion", TimePrecisionCodeType.Precision100sCode.ID, precisionUid);
  }

  @Test
  public void test1000s() throws Exception {
    long precisionUid = FormulaUtility.decimalPlaces2timePrecision(3L);
    Assert.assertEquals("Conversion", TimePrecisionCodeType.Precision1000sCode.ID, precisionUid);
  }

  @Test
  public void test10000s() throws Exception {
    long precisionUid = FormulaUtility.decimalPlaces2timePrecision(4L);
    Assert.assertEquals("Conversion", TimePrecisionCodeType.Precision1000sCode.ID, precisionUid);
  }

  @Test
  public void testDecimalNull() throws Exception {
    long precisionUid = FormulaUtility.timePrecision2decimalPlaces(TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Conversion", 0, precisionUid);
  }

  @Test
  public void testDecimal0() throws Exception {
    long precisionUid = FormulaUtility.timePrecision2decimalPlaces(TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Conversion", 0, precisionUid);
  }

  @Test
  public void testDecimal1() throws Exception {
    long precisionUid = FormulaUtility.timePrecision2decimalPlaces(TimePrecisionCodeType.Precision10sCode.ID);
    Assert.assertEquals("Conversion", 1, precisionUid);
  }

  @Test
  public void testDecimal2() throws Exception {
    long precisionUid = FormulaUtility.timePrecision2decimalPlaces(TimePrecisionCodeType.Precision100sCode.ID);
    Assert.assertEquals("Conversion", 2, precisionUid);
  }

  @Test
  public void testDecimal3() throws Exception {
    long precisionUid = FormulaUtility.timePrecision2decimalPlaces(TimePrecisionCodeType.Precision1000sCode.ID);
    Assert.assertEquals("Conversion", 3, precisionUid);
  }

  @Test
  public void testDecimalUnkown() throws Exception {
    long precisionUid = FormulaUtility.timePrecision2decimalPlaces(4L);
    Assert.assertEquals("Conversion", 0, precisionUid);
  }

}
