package com.rtiming.shared.ecard;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.junit.Test;

import junit.framework.Assert;

public class ECardUtilityTest {

  @Test
  public void testOLWM2003() throws Exception {
    Assert.assertEquals("No OLWM2003 E-Card", ECardTypeCodeType.SICard8Code.ID, ECardUtility.getType("2002999"));
    Assert.assertEquals("OLWM2003 E-Card", ECardTypeCodeType.SICard6Code.ID, ECardUtility.getType("2003000"));
    Assert.assertEquals("OLWM2003 E-Card", ECardTypeCodeType.SICard6Code.ID, ECardUtility.getType("2003799"));
    Assert.assertEquals("No OLWM2003 E-Card", ECardTypeCodeType.SICard8Code.ID, ECardUtility.getType("2003800"));
  }

  @Test(expected = VetoException.class)
  public void testSICard10Bound() throws Exception {
    ECardUtility.getType("7000000");
  }

  @Test
  public void testSICard10Lower() throws Exception {
    Assert.assertEquals("SI Card 10", ECardTypeCodeType.SICard10Code.ID, ECardUtility.getType("7000001"));
  }

  @Test
  public void testSICard10Upper() throws Exception {
    Assert.assertEquals("SI Card 10", ECardTypeCodeType.SICard10Code.ID, ECardUtility.getType("7999999"));
  }

  @Test(expected = VetoException.class)
  public void testSICard11Bound() throws Exception {
    ECardUtility.getType("9000000");
  }

  @Test
  public void testSICard11Lower() throws Exception {
    Assert.assertEquals("SI Card 11", ECardTypeCodeType.SICard11Code.ID, ECardUtility.getType("9000001"));
  }

  @Test
  public void testSICard11Upper() throws Exception {
    Assert.assertEquals("SI Card 11", ECardTypeCodeType.SICard11Code.ID, ECardUtility.getType("9999999"));
  }

  @Test(expected = VetoException.class)
  public void testSICardSIAC1Bound() throws Exception {
    ECardUtility.getType("8000000");
  }

  @Test
  public void testSIAC1Lower() throws Exception {
    Assert.assertEquals("SI Card SIAC1", ECardTypeCodeType.SICardSIAC1Code.ID, ECardUtility.getType("8000001"));
  }

  @Test
  public void testSIAC1Upper() throws Exception {
    Assert.assertEquals("SI Card SIAC1", ECardTypeCodeType.SICardSIAC1Code.ID, ECardUtility.getType("8999999"));
  }

  @Test(expected = VetoException.class)
  public void testNull() throws Exception {
    ECardUtility.getType(null);
  }

  @Test(expected = VetoException.class)
  public void testEmpty() throws Exception {
    ECardUtility.getType("");
  }

  @Test(expected = VetoException.class)
  public void testInvalid() throws Exception {
    ECardUtility.getType("-5");
  }

}
