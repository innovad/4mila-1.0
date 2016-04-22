package com.rtiming.client.ecard.download;

import org.eclipse.scout.rt.client.IClientSession;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.rtiming.client.ecard.download.util.ByteUtility;

/**
 * @author amo
 */
public class SISerialPortListenerTest {

  @Test
  public void testMessageSize12() throws Exception {
    TestObjects test = createSerialInterface();
    byte[] data = new byte[]{2, -24, 6, 1, -13, 2, 31, 47, -38, 31, 108, 3};
    test.getListener().serialEvent(data);
    Assert.assertEquals("Data", ByteUtility.dumpBytes(data), ByteUtility.dumpBytes(test.getHandler().getData()));
  }

  @Test
  public void testMessageSizeV6ConfigData() throws Exception {
    TestObjects test = createSerialInterface();
    byte[] data = new byte[]{2, -125, 4, 0, 26, 51, -63, 32, 38, 3};
    test.getListener().serialEvent(data);
    Assert.assertEquals("Data", ByteUtility.dumpBytes(data), ByteUtility.dumpBytes(test.getHandler().getData()));
  }

  @Test
  public void testMessageSizeV6Inserted() throws Exception {
    TestObjects test = createSerialInterface();
    byte[] data = new byte[]{2, -26, 6, 0, 26, 0, 11, -56, 7, -48, 34, 3};
    test.getListener().serialEvent(data);
    Assert.assertEquals("Data", ByteUtility.dumpBytes(data), ByteUtility.dumpBytes(test.getHandler().getData()));
  }

  @Test
  public void testMessageSizeV6Data() throws Exception {
    TestObjects test = createSerialInterface();
    byte[] data = new byte[]{2, -31, -125, 0, 26, 6, 18, 32, 126, 84, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, 121, -5, 3};
    test.getListener().serialEvent(data);
    Assert.assertEquals("Data", ByteUtility.dumpBytes(data), ByteUtility.dumpBytes(test.getHandler().getData()));
  }

  /**
   * Tests a partial read with SI Card 8 or 9
   */
  @Test
  public void testMessageSizeV8Partial() throws Exception {
    TestObjects test = createSerialInterface();
    byte[] data = new byte[]{2, -17, -125, 1, -13, 0, 24, -15, 18, 127, -22, -22, -22, -22, 11, 4, 118, -42, -18, -18, -18, -18, 75, -12, 122, 47, 0, 58, 26, 12, 2, 31, 47, -38, 12, -1, -12, -75, 65, 100, 114, 105, 97, 110, 59, 77, 111, 115, 101, 114, 59, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    test.getListener().serialEvent(data);
    Assert.assertNull("Data", test.getHandler().getData());
    byte[] data2 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 28, -7, 3};
    test.getListener().serialEvent(data2);
    Assert.assertEquals("Data", ByteUtility.dumpBytes(ByteUtility.concatArrays(data, data2)), ByteUtility.dumpBytes(test.getHandler().getData()));
  }

  @Test
  public void testMessageSizeV5() throws Exception {
    TestObjects test = createSerialInterface();
    byte[] data = new byte[]{2, -79, -126, 0, 4, -86, 41, 0, 1, -10, -19, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 101, -10, -19, 35, 2, 42, 52, 10, 86, -11, -90, 40, 1, -28, 0, 7, 0, 34, 35, -116, 52, 36, 99, 54, 37, 30, 88, 37, -93, 90, 38, 43, 0, 96, 39, 108, 95, 40, 15, 99, 41, -86, 100, 42, 12, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, 0, -18, -18, -26, 7, 3};
    test.getListener().serialEvent(data);
    Assert.assertEquals("Data", ByteUtility.dumpBytes(data), ByteUtility.dumpBytes(test.getHandler().getData()));
  }

  @Test
  public void testMessageSizeV8Multiple() throws Exception {
    TestObjects test = createSerialInterface();
    byte[] data1 = new byte[]{2, -17, -125, 0, 4, 0, 26, -67, 18, 127, -22, -22, -22, -22, 29, 3, 58, -112, -99, 93, 60, 8, 29, 2, 66, -65, 0, 50, 26, 15, 1, 17, -109, -25, 12, -1, 85, -33, 65, 100, 114, 105, 97, 110, 59, 77, 111, 115, 101, 114, 59, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 29, 36, 60, 122, 29, 31, 60, -86, 29, 32, 60, -45, 29, 38, 61, 27, 29, 35, 61, 103, 29, 34, 61, 127, 29, 33, 61, -89, 29, 37, 61, -51, 29, 58, 62, 20, 29, 54, 62, -124, 29, 56, 62, -61, 29, 49, 62, -16, 29, 40, 63, 80, 29, 41, 63, -115, 29, 39, 63, -16, 29, 51, 64, 15, 29, 59, 64, 101, 29, 47, 64, -121, -90, -34, 3};
    byte[] data2 = new byte[]{2, -17, -125, 0, 4, 1, 29, 48, 64, -77, 29, 46, 65, 90, 29, 44, 65, -100, 29, 43, 65, -87, 29, 53, 65, -52, 29, 45, 65, -16, 29, 42, 66, 86, 29, 50, 66, -78, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -18, -70, 17, 3};
    byte[] data = ByteUtility.concatArrays(data1, data2);
    test.getListener().serialEvent(data);
    Assert.assertEquals("Data", ByteUtility.dumpBytes(data), ByteUtility.dumpBytes(test.getHandler().getData()));
  }

  @Test
  public void testMessageSizeV8PartialMultiple() throws Exception {
    TestObjects test = createSerialInterface();
    byte[] data1 = new byte[]{2, -17, -125, 1, -13, 0, 24, -15, 18, 127, -22, -22, -22, -22, 11, 4, 118, -42, -18, -18, -18, -18, 75, -12, 122, 47, 0, 58, 26, 12, 2, 31, 47, -38, 12, -1, -12, -75, 65, 100, 114, 105, 97, 110, 59, 77, 111, 115, 101, 114, 59, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 28, -7, 3};
    byte[] data2 = new byte[]{2, -17, -125, 1, -13, 0, 24, -15, 18, 127, -22, -22, -22, -22, 11, 4, 118, -42, -18, -18, -18, -18, 75, -12, 122, 47, 0, 58, 26, 12, 2, 31, 47, -38, 12, -1, -12, -75, 65, 100, 114, 105, 97, 110, 59, 77, 111, 115, 101, 114, 59, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    byte[] data = ByteUtility.concatArrays(data1, data2);
    test.getListener().serialEvent(data);
    Assert.assertNull("Data", test.getHandler().getData());
    byte[] data3 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 28, -7, 3};
    test.getListener().serialEvent(data3);
    Assert.assertEquals("Data", ByteUtility.dumpBytes(ByteUtility.concatArrays(data, data3)), ByteUtility.dumpBytes(test.getHandler().getData()));
  }

  private TestObjects createSerialInterface() {
    IClientSession session = Mockito.mock(IClientSession.class);
    SISerialPortListener listener = new SISerialPortListener(session);
    TestSerialPortHandler serialPortHandler = new TestSerialPortHandler(session);
    listener.installHandler(serialPortHandler);
    TestObjects test = new TestObjects(listener, serialPortHandler);
    return test;
  }

  private class TestObjects {

    private SISerialPortListener listener;
    private TestSerialPortHandler handler;

    public TestObjects(SISerialPortListener listener, TestSerialPortHandler handler) {
      super();
      this.listener = listener;
      this.handler = handler;
    }

    public TestSerialPortHandler getHandler() {
      return handler;
    }

    public SISerialPortListener getListener() {
      return listener;
    }

  }

}
