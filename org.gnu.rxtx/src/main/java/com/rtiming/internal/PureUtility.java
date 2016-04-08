package com.rtiming.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.rtiming.serial.FMilaSerialPort;

import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.SerialPort;
import purejavacomm.UnsupportedCommOperationException;

/**
 * 
 */
public final class PureUtility {

  public static String[] getPorts() {
    List<String> ports = new ArrayList<String>();
    Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();
    while (portList.hasMoreElements()) {
      CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
      if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        ports.add(portId.getName());
      }
    }
    return ports.toArray(new String[ports.size()]);
  }

  public static FMilaSerialPort getPort(int speed, String port) throws IOException {
    SerialPort serialPort;

    try {

      CommPortIdentifier comm = CommPortIdentifier.getPortIdentifier(port);
      if (comm.isCurrentlyOwned()) {
        throw new IOException("StationInUseError");
      }
      serialPort = (SerialPort) comm.open("4mila", 2000);

      serialPort.setSerialPortParams(speed, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
      serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
      serialPort.disableReceiveTimeout();
      serialPort.disableReceiveFraming();
      serialPort.disableReceiveThreshold();
      serialPort.notifyOnDataAvailable(true);
      serialPort.notifyOnOutputEmpty(true);
    }
    catch (PortInUseException | NoSuchPortException | UnsupportedCommOperationException e) {
      throw new IOException(e.getMessage());
    }

    return new PureSerialPort(serialPort);
  }

}
