package com.rtiming.client.ecard.download;

import java.io.IOException;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.serial.FMilaSerialPort;

public abstract class AbstractSISerialPortHandler {

  private final IClientSession session;
  private final FMilaSerialPort port;

  public AbstractSISerialPortHandler(IClientSession session, FMilaSerialPort port) {
    this.session = session;
    this.port = port;
  }

  protected abstract void handleData(byte[] data) throws IOException, ProcessingException;

  public IClientSession getSession() {
    return session;
  }

  public FMilaSerialPort getPort() {
    return port;
  }

}
