package com.rtiming.client.result.pos;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

public final class PosPrinterManager {

  private PosPrinterManager() {
  }

  private static Map<String, IPosPrinter> printers = new HashMap<String, IPosPrinter>();

  public static IPosPrinter get(String printerName) throws ProcessingException {
    if (printers.get(printerName) == null) {
      PosPrinter printer = new PosPrinter(printerName);
      printers.put(printerName, printer);
    }
    return printers.get(printerName);
  }

  public static void closeAll() throws ProcessingException {
    for (IPosPrinter printer : printers.values()) {
      printer.close();
    }
    printers.clear();
  }

}
