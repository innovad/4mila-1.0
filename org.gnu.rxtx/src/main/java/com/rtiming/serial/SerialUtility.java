package com.rtiming.serial;

import java.io.IOException;

import com.rtiming.internal.JsscUtility;
import com.rtiming.internal.PureUtility;
import com.rtiming.internal.RXTXUtility;

public final class SerialUtility {

  // RXTX is the default for Windows and Linux
  static SerialLibrary library = SerialLibrary.RXTX;

  public static String[] getPorts() {
    if (SerialLibrary.RXTX.equals(library)) {
      return RXTXUtility.getPorts();
    }
    else if (SerialLibrary.PURE.equals(library)) {
      return PureUtility.getPorts();
    }
    else {
      return JsscUtility.getPorts();
    }
  }

  public static FMilaSerialPort getPort(int speed, String port) throws IOException {
    if (SerialLibrary.RXTX.equals(library)) {
      return RXTXUtility.getPort(speed, port);
    }
    else if (SerialLibrary.PURE.equals(library)) {
      return PureUtility.getPort(speed, port);
    }
    else {
      return JsscUtility.getPort(speed, port);
    }
  }

  public static void setLibrary(SerialLibrary library) {
    SerialUtility.library = library;
  }

  public static SerialLibrary getLibrary() {
    return library;
  }

}
