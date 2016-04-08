package com.rtiming.client.result.pos;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

public class PosPrinterLookupCall extends LocalLookupCall<String> {

  private static final long serialVersionUID = 1L;

  @Override
  protected List<LookupRow<String>> execCreateLookupRows() throws ProcessingException {
    ArrayList<LookupRow<String>> rows = new ArrayList<LookupRow<String>>();

    for (String printer : PosPrinterUtility.getPosPrinters()) {
      LookupRow row = new LookupRow(printer, printer);
      rows.add(row);
    }

    return rows;
  }
}
