package com.rtiming.internal;

import java.io.IOException;

import com.rtiming.serial.FMilaSerialPort;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 * 
 */
public class JsscUtility {

  public static String[] getPorts() {
    return SerialPortList.getPortNames();
  }

  public static FMilaSerialPort getPort(int speed, String port) throws IOException {
    SerialPort serialPort = new SerialPort(port);
    try {
      serialPort.openPort();
    }
    catch (SerialPortException e) {
      throw new IOException(e.getMessage(), e);
    }
    return new JsscSerialPort(serialPort);
  }
}
