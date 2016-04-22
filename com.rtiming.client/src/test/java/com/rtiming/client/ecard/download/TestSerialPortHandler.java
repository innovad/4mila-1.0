package com.rtiming.client.ecard.download;

import java.io.IOException;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

/**
 * @author amo
 */
public class TestSerialPortHandler extends AbstractSISerialPortHandler {

  private byte[] data;

  public TestSerialPortHandler(IClientSession session) {
    super(session, new FMilaSerialTestPort());
  }

  @Override
  protected void handleData(byte[] data) throws IOException, ProcessingException {
    this.data = data;
  }

  public byte[] getData() {
    return data;
  }

}
