package com.rtiming.client.result.pos;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;

import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterControl19;
import jpos.config.JposEntryRegistry;
import jpos.loader.JposServiceLoader;

public final class PosPrinterUtility {

  private PosPrinterUtility() {
  }

  public static void posInfo(String printer) throws ProcessingException {

    final POSPrinterControl19 ptr = open(printer);

    try {
      StringBuilder info = new StringBuilder();

      info.append("PhysicalDeviceName: ");
      info.append(ptr.getPhysicalDeviceName());
      info.append("\n");

      info.append("PhysicalDeviceDescription: ");
      info.append(ptr.getPhysicalDeviceDescription());
      info.append("\n");

      info.append("DeviceControlDescription: ");
      info.append(ptr.getDeviceControlDescription());
      info.append("\n");

      info.append("DeviceControlVersion: ");
      info.append(ptr.getDeviceControlVersion());
      info.append("\n");

      info.append("DeviceServiceDescription: ");
      info.append(ptr.getDeviceServiceDescription());
      info.append("\n");

      info.append("DeviceServiceVersion: ");
      info.append(ptr.getDeviceServiceVersion());
      info.append("\n");

      info.append("CharacterSetList: ");
      info.append(ptr.getCharacterSetList());
      info.append("\n");

      info.append("RecLineWidth: ");
      info.append(ptr.getRecLineWidth());
      info.append("\n");

      info.append("RecLineChars: ");
      info.append(ptr.getRecLineChars());
      info.append("\n");

      info.append("RecLineCharsList: ");
      info.append(ptr.getRecLineCharsList());
      info.append("\n");

      info.append("RecLineHeight: ");
      info.append(ptr.getRecLineHeight());
      info.append("\n");

      FMilaClientUtility.showOkMessage(TEXTS.get("ApplicationName"), null, info.toString());

      close(ptr);
    }
    catch (JposException ex) {
      throw new ProcessingException(ex.getLocalizedMessage(), ex);
    }

  }

  public static void printTest(String printerName) throws ProcessingException {
    PosPrinter printer = new PosPrinter(printerName);

    SplitBuilder test = new SplitBuilder(printer.getLineWidth());
    test.append(Texts.get("ApplicationName") + " " + FMilaUtility.getVersion());
    test.appendNewLine();
    test.appendNewLine();
    test.append("Test width:");
    test.appendNewLine();
    for (int i = 0; i < printer.getLineWidth(); i++) {
      test.append("*");
    }
    test.appendNewLine();
    test.appendWide("Test wide font");
    test.appendNewLine();
    test.appendLeftRight("Test left", "right");
    test.appendNewLine();
    test.appendLeftRightWide("Test left", "right");
    test.appendNewLine();
    test.append("Test finished.");
    test.cut();

    printer.print(test.toString());
    printer.close();
  }

  public static String startBold() {
    return "\u001b|bC";
  }

  public static String startWide() {
    return "\u001b|bC\u001b|2C";
  }

  public static String startNormal() {
    return "\u001b|N";
  }

  public static String cut() {
    return "\u001b|fP";
  }

  public static int getLineWidth(POSPrinterControl19 ptr) throws ProcessingException {
    try {
      return ptr.getRecLineChars();
    }
    catch (JposException e) {
      throw new ProcessingException(e.getLocalizedMessage(), e);
    }
  }

  public static List<String> getPosPrinters() throws ProcessingException {
    ArrayList<String> result = new ArrayList<String>();

    JposEntryRegistry reg = JposServiceLoader.getManager().getEntryRegistry();
    Enumeration enumeration = reg.getEntries();
    while (enumeration.hasMoreElements()) {
      Object o = enumeration.nextElement();
      if (o instanceof SimpleEntry) {
        SimpleEntry entry = (SimpleEntry) o;
        // TODO MIG result.add(entry.getLogicalName());
      }
    }

    return result;
  }

  public static POSPrinterControl19 open(String printer) throws ProcessingException {
    final POSPrinterControl19 ptr = new POSPrinter();

    // JposPropertiesConst.JPOS_POPULA TOR_FILE_PROP_NAME

    try {
      //Open the device.
      //Use the name of the device that connected with your computer.
      ptr.open(printer);

      //Get the exclusive control right for the opened device.
      //Then the device is disable from other application.
      ptr.claim(1000);

      //Enable the device.
      ptr.setDeviceEnabled(true);
    }
    catch (JposException e) {
      throw new ProcessingException(e.getLocalizedMessage(), e);
    }

    return ptr;
  }

  public static void close(POSPrinterControl19 ptr) throws ProcessingException {
    try {
      //Cancel the device.
      ptr.setDeviceEnabled(false);

      //Release the device exclusive control right.
      ptr.release();

      //Finish using the device.
      ptr.close();
    }
    catch (JposException e) {
      throw new ProcessingException(e.getLocalizedMessage(), e);
    }
  }

}
