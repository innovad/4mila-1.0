package com.rtiming.client.ecard.download;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rtiming.client.test.ClientTestingUtility;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.serial.SerialLibrary;
import com.rtiming.serial.SerialUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.FMilaUtility.Architecture;

public class SerialUtilityTest {

  static SerialLibrary defaultLibrary;

  @BeforeClass
  public static void beforeClass() {
    defaultLibrary = SerialUtility.getLibrary();
  }

  @Test
  public void testPure() throws Exception {
    SerialUtility.setLibrary(SerialLibrary.PURE);
    SerialUtility.getPorts();
  }

  @Test
  public void testRxTx() throws Exception {
    SerialUtility.setLibrary(SerialLibrary.RXTX);
    SerialUtility.getPorts();
  }

  @Test
  public void testJssc() throws Exception {
    SerialUtility.setLibrary(SerialLibrary.JSSC);
    SerialUtility.getPorts();
  }

  @Test(expected = IOException.class)
  public void testPureOpenNA() throws Exception {
    SerialUtility.setLibrary(SerialLibrary.PURE);
    SerialUtility.getPort(38400, "NOT AVAILABLE");
  }

  @Test(expected = IOException.class)
  public void testRxTxOpenNA() throws Exception {
    SerialUtility.setLibrary(SerialLibrary.RXTX);
    SerialUtility.getPort(38400, "NOT AVAILABLE");
  }

  @Test(expected = IOException.class)
  public void testJsscOpenNA() throws Exception {
    Assume.assumeTrue(FMilaUtility.getArchitecture().equals(Architecture.WIN32));
    SerialUtility.setLibrary(SerialLibrary.JSSC);
    SerialUtility.getPort(38400, "NOT AVAILABLE");
  }

  @Test
  public void testPureOpen() throws Exception {
    Assume.assumeTrue(FMilaUtility.getArchitecture().equals(Architecture.MACOSX));
    SerialUtility.setLibrary(SerialLibrary.PURE);
    FMilaSerialPort port = SerialUtility.getPort(38400, ClientTestingUtility.getSerialTestingPort());
    port.close();
  }

  @Test
  public void testRxTxOpen() throws Exception {
    Assume.assumeTrue(FMilaUtility.getArchitecture().equals(Architecture.WIN32));
    SerialUtility.setLibrary(SerialLibrary.RXTX);
    FMilaSerialPort port = SerialUtility.getPort(38400, ClientTestingUtility.getSerialTestingPort());
    port.close();
  }

  @Test
  public void testJsscOpen() throws Exception {
    Assume.assumeTrue(FMilaUtility.getArchitecture().equals(Architecture.WIN32));
    SerialUtility.setLibrary(SerialLibrary.JSSC);
    FMilaSerialPort port = SerialUtility.getPort(38400, ClientTestingUtility.getSerialTestingPort());
    port.close();
  }

  @AfterClass
  public static void afterClass() {
    SerialUtility.setLibrary(defaultLibrary);
  }

}
