package com.rtiming.client.event.course;

import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.event.course.ControlTypeCodeType;

/**
 * @author amo
 */
public class ControlFormUtilityTest {

  @Test
  public void testParseControlType1() throws Exception {
    long typeUid = ControlFormUtility.parseControlType(null);
    Assert.assertEquals("Type", ControlTypeCodeType.ControlCode.ID, typeUid);
  }

  @Test
  public void testParseControlType2() throws Exception {
    long typeUid = ControlFormUtility.parseControlType("s1");
    Assert.assertEquals("Type", ControlTypeCodeType.StartCode.ID, typeUid);
  }

  @Test
  public void testParseControlType3() throws Exception {
    long typeUid = ControlFormUtility.parseControlType("F");
    Assert.assertEquals("Type", ControlTypeCodeType.FinishCode.ID, typeUid);
  }

  @Test
  public void testParseControlType4() throws Exception {
    long typeUid = ControlFormUtility.parseControlType("3SF");
    Assert.assertEquals("Type", ControlTypeCodeType.ControlCode.ID, typeUid);
  }

  @Test
  public void testParseControlType5() throws Exception {
    long typeUid = ControlFormUtility.parseControlType("z");
    Assert.assertEquals("Type", ControlTypeCodeType.FinishCode.ID, typeUid);
  }

  @Test
  public void testParseControlType6() throws Exception {
    long typeUid = ControlFormUtility.parseControlType("");
    Assert.assertEquals("Type", ControlTypeCodeType.ControlCode.ID, typeUid);
  }

}
