package com.rtiming.client.ecard.download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ecard.download.job.StatusUpdaterJob;
import com.rtiming.client.ecard.download.util.WindowsRegistryUtility;
import com.rtiming.serial.SerialUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.FMilaUtility.OperatingSystem;

public class ComPortLookupCall extends LocalLookupCall<String> {

  private static final long serialVersionUID = 1L;

  @Override
  protected List<LookupRow<String>> execCreateLookupRows() throws ProcessingException {
    ArrayList<LookupRow<String>> rows = new ArrayList<LookupRow<String>>();

    // find friendly names
    HashMap<String, String> friendlyNames = new HashMap<String, String>();
    if (FMilaUtility.getPlatform().equals(OperatingSystem.WINDOWS)) {
      String[] registryList = WindowsRegistryUtility.listRegistryEntries("HKLM\\System\\CurrentControlSet\\Enum").split("\n");
      Pattern comPattern = Pattern.compile("FriendlyName.+REG_SZ(.+)\\((COM\\d+)\\)");

      for (String string : registryList) {
        Matcher match = comPattern.matcher(string);
        if (match.find()) {
          String com = match.group(2);
          String fname = com + ": " + match.group(1).trim();
          friendlyNames.put(com, fname);
        }
      }
    }

    // serial ports by RXTX
    try {
      for (String port : SerialUtility.getPorts()) {
        LookupRow row = new LookupRow(port, StringUtility.nvl(friendlyNames.get(port), port));
        rows.add(row);
      }
    }
    catch (java.lang.Error e) {
      handleError(e);
    }

    return rows;
  }

  private void handleError(java.lang.Error e) {
    e.printStackTrace();
    StatusUpdaterJob.setText(TEXTS.get(ClientSession.get().getLocale(), "SerialPortAccessDenied") + " (" + e.getMessage() + ")", ClientSession.get());
  }
}
