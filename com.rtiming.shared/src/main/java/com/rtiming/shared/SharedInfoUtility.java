package com.rtiming.shared;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TimeZone;
import java.util.TreeMap;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;

public final class SharedInfoUtility {

  private SharedInfoUtility() {
  }

  private static final String NEW_LINE = FMilaUtility.LINE_SEPARATOR;
  private static final IScoutLogger LOG = ScoutLogManager.getLogger(SharedInfoUtility.class);

  public static String buildInstallationInfo(String caller) throws ProcessingException {
    StringBuilder info = new StringBuilder();

    info.append("*** 4mila " + caller + " Properties ***");
    info.append(NEW_LINE);

    // Timezone
    TimeZone timeZone = Calendar.getInstance().getTimeZone();
    info = appendInfo(info, "Server Time Zone", timeZone.getDisplayName());
    info = appendInfo(info, "Server Time Zone ID", timeZone.getID());
    info = appendInfo(info, "Server DST", String.valueOf(timeZone.getDSTSavings()));
    info.append(NEW_LINE);

    // Java
    Properties p = System.getProperties();
    Enumeration keys = p.keys();
    TreeMap<String, String> sortedMap = new TreeMap<String, String>();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      String value = (String) p.get(key);
      sortedMap.put(key, value);
    }
    for (String key : sortedMap.keySet()) {
      info = appendInfo(info, key, sortedMap.get(key));
    }
    info.append(NEW_LINE);

    return info.toString();
  }

  public static StringBuilder appendInfo(StringBuilder builder, String title, String info) {
    builder.append(title);
    builder.append(": ");
    builder.append(info);
    builder.append(NEW_LINE);
    return builder;
  }

}
