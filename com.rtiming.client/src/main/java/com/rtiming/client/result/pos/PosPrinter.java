package com.rtiming.client.result.pos;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import jpos.JposException;
import jpos.POSPrinterConst;
import jpos.POSPrinterControl19;

public class PosPrinter implements IPosPrinter {

  private final POSPrinterControl19 ptr;

  public PosPrinter(String printerName) throws ProcessingException {
    ptr = PosPrinterUtility.open(printerName);
  }

  @Override
  public int getLineWidth() throws ProcessingException {
    return PosPrinterUtility.getLineWidth(ptr);
  }

  @Override
  public void print(String line) throws ProcessingException {
    try {
      ptr.printNormal(POSPrinterConst.PTR_S_RECEIPT, line);
    }
    catch (JposException e) {
      throw new ProcessingException(e.getLocalizedMessage());
    }
  }

  @Override
  public void close() throws ProcessingException {
    PosPrinterUtility.close(ptr);
  }

}
