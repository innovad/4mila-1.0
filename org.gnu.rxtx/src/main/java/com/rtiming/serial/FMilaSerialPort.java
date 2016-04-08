package com.rtiming.serial;

import java.io.IOException;

/**
 * 
 */
public interface FMilaSerialPort {

  public void write(byte... data) throws IOException;

  public void close();

  public void addEventListener(final FMilaSerialEventListener listener);

}
