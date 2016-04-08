package com.rtiming.internal;

import java.io.IOException;

import com.rtiming.serial.FMilaSerialEventListener;
import com.rtiming.serial.FMilaSerialPort;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * 
 */
public class JsscSerialPort implements FMilaSerialPort {

  private final SerialPort serialPort;

  public JsscSerialPort(SerialPort serialPort) {
    this.serialPort = serialPort;
  }

  @Override
  public void write(byte... data) throws IOException {
    try {
      serialPort.writeBytes(data);
    }
    catch (SerialPortException e) {
      throw new IOException(e.getMessage());
    }
  }

  @Override
  public void close() {
    try {
      serialPort.closePort();
    }
    catch (SerialPortException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addEventListener(final FMilaSerialEventListener milaListener) {
    SerialPortEventListener listener = new SerialPortEventListener() {

      @Override
      public void serialEvent(SerialPortEvent arg0) {
        try {
          milaListener.serialEvent(serialPort.readBytes());
        }
        catch (SerialPortException | IOException e) {
          e.printStackTrace();
        }
      }
    };
    try {
      serialPort.addEventListener(listener, SerialPort.MASK_RXCHAR);
    }
    catch (SerialPortException e) {
      e.printStackTrace();
    }
  }
}
