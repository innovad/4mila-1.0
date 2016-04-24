package com.rtiming.client.ecard.download;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.serial.FMilaSerialPort;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class SIStationSerialPortHandlerTest {

  @Test
  public void testStationInit() throws Exception {
    FMilaSerialPort port = new FMilaSerialTestPort();
    Object lock = new Object();
    SIStationSerialPortHandler handler = new SIStationSerialPortHandler(ClientSession.get(), lock, port);
    handler.handleData(new byte[]{0, -16, 0, 0, 0, 77, 0, 0, 0});
    Assert.assertFalse(handler.isInitialized());
  }

  @Test(expected = ProcessingException.class)
  public void testStationConfigStandardProtocol() throws Exception {
    FMilaSerialPort port = new FMilaSerialTestPort();
    Object lock = new Object();
    SIStationSerialPortHandler handler = new SIStationSerialPortHandler(ClientSession.get(), lock, port);
    handler.handleData(new byte[]{0, -125, 0, 0, 0, 0, 0, 0, 0, 0});
  }

  @Test
  public void testStationConfigExtendedProtocol() throws Exception {
    FMilaSerialPort port = new FMilaSerialTestPort();
    Object lock = new Object();
    SIStationSerialPortHandler handler = new SIStationSerialPortHandler(ClientSession.get(), lock, port);
    handler.handleData(new byte[]{2, -125, 4, 0, 26, 116, 5, -80, -72, 3});
    Assert.assertTrue(handler.isInitialized());
  }

}
