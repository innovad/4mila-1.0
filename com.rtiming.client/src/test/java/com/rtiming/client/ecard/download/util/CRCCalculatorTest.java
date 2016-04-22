package com.rtiming.client.ecard.download.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author amo
 */
public class CRCCalculatorTest {

  @Test
  public void testCRC() {

    byte[] testData = new byte[]{

        // Example Test Message:

    /*(byte)0x02, */
    (byte) 0x53, (byte) 0x00, (byte) 0x05, (byte) 0x01, (byte) 0x0F,
        (byte) 0xB5, (byte) 0x00, (byte) 0x00, (byte) 0x1E, (byte) 0x08
    /*, (byte)0x2C, (byte)0x12, (byte)0x03 */
    };

    // Should give 2C12 (see message above)
    Assert.assertEquals("2c12", Integer.toString(CRCCalculator.crc(testData), 16));

  }

}
