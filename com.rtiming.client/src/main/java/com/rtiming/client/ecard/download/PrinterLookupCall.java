package com.rtiming.client.ecard.download;

import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

public class PrinterLookupCall extends LocalLookupCall<String> {

  private static final long serialVersionUID = 1L;

  @Override
  protected List<LookupRow<String>> execCreateLookupRows() throws ProcessingException {
    ArrayList<LookupRow<String>> rows = new ArrayList<LookupRow<String>>();
    PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

    for (PrintService printer : printServices) {
      LookupRow row = new LookupRow(printer.getName(), printer.getName());
      rows.add(row);
    }

    return rows;
  }
}
