package com.rtiming.client.ecard.download;

import java.io.IOException;

import com.rtiming.serial.FMilaSerialEventListener;
import com.rtiming.serial.FMilaSerialPort;

public class FMilaSerialTestPort implements FMilaSerialPort {

  public FMilaSerialTestPort() {
  }

  @Override
  public void write(byte... data) throws IOException {
  }

  @Override
  public void close() {
  }

  @Override
  public void addEventListener(FMilaSerialEventListener listener) {
  }

}
