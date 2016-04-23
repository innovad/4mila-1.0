package com.rtiming.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.InternetAddress;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.Assertions.AssertionException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.code.ICodeService;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.shared.race.TimePrecisionCodeType;

/**
 *
 */
public final class FMilaUtility {

  private FMilaUtility() {
  }

  public static final String EDIT_KEY_STROKE = "alt-E";
  public static final String NEW_KEY_STROKE = "alt-N";
  public static final String NEW_ENTRY_KEY_STROKE = "f6";

  public static final String ADMIN_USER = "admin";

  public static final String FIRST_CONTROL_NO = "31";

  public static final Long GLOBAL_CLIENT_NR = 1L;

  public static final String LINE_SEPARATOR = System.getProperty("line.separator");

  public static final String FILE_SEPARATOR = System.getProperty("file.separator");

  public static enum OperatingSystem {
    MACOSX, WINDOWS, LINUX, UNKNWOWN
  }

  public static enum Architecture {
    WIN32 {
      @Override
      public String toString() {
        return "Windows 32bit (Windows XP, Vista, 7)";
      }
    },
    WIN64 {
      @Override
      public String toString() {
        return "Windows 64bit (Windows 7 x64)";
      }
    },
    MACOSX {
      @Override
      public String toString() {
        return "Mac OS X 64bit (10.7, 10.8, 10.9, 10.10)";
      }
    },
    UNKNOWN
  }

  public static OperatingSystem getPlatform() {
    String osname = StringUtility.nvl(System.getProperty("os.name"), new String()).toLowerCase();

    if (osname.startsWith("windows")) {
      return OperatingSystem.WINDOWS;
    }
    else if (osname.startsWith("mac")) {
      return OperatingSystem.MACOSX;
    }
    else if (osname.startsWith("linux")) {
      return OperatingSystem.LINUX;
    }

    return OperatingSystem.UNKNWOWN;
  }

  public static Architecture getArchitecture() {
    if (OperatingSystem.MACOSX.equals(getPlatform())) {
      return Architecture.MACOSX;
    }
    String arch = StringUtility.nvl(System.getProperty("os.arch"), new String()).toLowerCase();
    if (arch.contains("64")) {
      return Architecture.WIN64;
    }
    return Architecture.WIN32;
  }

  public static final double ONE_INCH = 2.54;

  public static Long getYearFromDate(Date birthdate) {
    if (birthdate == null) {
      return null;
    }
    Long year = null;
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(birthdate);
    year = new Long(cal.get(Calendar.YEAR));
    return year;
  }

  public static final String DEFAULT_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss.SSS";

  public static final String DEFAULT_TIME_FORMAT_DATE_HMS = "dd.MM.yyyy HH:mm:ss";

  public static final String DEFAULT_TIME_FORMAT_HMS = "HH:mm:ss";

  public static Long getDateDifferenceInMilliSeconds(Date zeroTime, Date overallTime) {
    Long timeDiff = null;

    if (overallTime != null && zeroTime != null) {

      timeDiff = (overallTime.getTime() - zeroTime.getTime());

      // if the dates are not in the same timezone (e.g. CET and CEST), subtract the difference
      GregorianCalendar zeroTimeCal = new GregorianCalendar();
      zeroTimeCal.setTime(zeroTime);
      int zeroTimeOffset = zeroTimeCal.get(Calendar.ZONE_OFFSET) + zeroTimeCal.get(Calendar.DST_OFFSET);

      GregorianCalendar overallTimeCal = new GregorianCalendar();
      overallTimeCal.setTime(overallTime);
      int overallTimeOffset = overallTimeCal.get(Calendar.ZONE_OFFSET) + overallTimeCal.get(Calendar.DST_OFFSET);

      if (zeroTimeOffset != overallTimeOffset) {
        timeDiff = timeDiff + (overallTimeOffset - zeroTimeOffset);
      }
    }

    return timeDiff;
  }

  public static Date addMilliSeconds(Date baseDate, Long ms) {
    Date resultDate = null;

    if (baseDate != null && ms != null) {
      resultDate = new Date(baseDate.getTime() + ms);

      // if the dates are not in the same timezone (e.g. CET and CEST), add the difference
      GregorianCalendar baseDateCal = new GregorianCalendar();
      baseDateCal.setTime(baseDate);
      int baseDateOffset = baseDateCal.get(Calendar.ZONE_OFFSET) + baseDateCal.get(Calendar.DST_OFFSET);

      GregorianCalendar resultDateCal = new GregorianCalendar();
      resultDateCal.setTime(resultDate);
      int resultTimeOffset = resultDateCal.get(Calendar.ZONE_OFFSET) + resultDateCal.get(Calendar.DST_OFFSET);

      if (baseDateOffset != resultTimeOffset) {
        ms = ms - (resultTimeOffset - baseDateOffset);
      }

      resultDate = new Date(baseDate.getTime() + ms);
    }
    return resultDate;
  }

  public static String formatFileSize(Long size) {
    if (size == null || size <= 0) {
      return "0";
    }
    final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
    int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
    return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
  }

  public static String formatTime(Long time, Long precisionUid) {
    if (time != null && time >= 0) {
      StringBuilder result = new StringBuilder();
      long h = TimeUnit.MILLISECONDS.toHours(time);
      long minutes = time - (h * 60 * 60 * 1000);
      long mi = TimeUnit.MILLISECONDS.toMinutes(minutes);
      long seconds = minutes - (mi * 60 * 1000);
      long s = TimeUnit.MILLISECONDS.toSeconds(seconds);
      if (h == 0) {
        if (mi >= 10) {
          result.append(String.format("%02d:%02d", mi, s));
        }
        else {
          result.append(String.format("%d:%02d", mi, s));
        }
      }
      else {
        result.append(String.format("%d:%02d:%02d", h, mi, s));
      }
      long part = seconds - (s * 1000);
      if (CompareUtility.equals(precisionUid, TimePrecisionCodeType.Precision10sCode.ID)) {
        result.append("." + part / 100);
      }
      else if (CompareUtility.equals(precisionUid, TimePrecisionCodeType.Precision100sCode.ID)) {
        result.append("." + part / 10);
      }
      else if (CompareUtility.equals(precisionUid, TimePrecisionCodeType.Precision1000sCode.ID)) {
        result.append("." + part);
      }
      return result.toString();
    }
    return new String();
  }

  public static final long MAX_SORTCODE = 999L;

  public static final long MAX_AGE = 199L;

  public static final String COLOR_LIGHT_GREY = "cccccc";

  public static final String COLOR_ORANGE = "ffa500";

  public static final String COLOR_LIGHT_BLUE = "819FF7";

  public static String getHostAddress() throws ProcessingException {
    try {
      InetAddress addr = InetAddress.getLocalHost();
      return StringUtility.nvl(addr.getHostName(), addr.getAddress().toString());

    }
    catch (UnknownHostException e) {
      throw new ProcessingException(e.getMessage());
    }
  }

  public static Long parseLongParameter(String param) {
    Long result;
    try {
      result = Long.parseLong(param);
    }
    catch (Exception e) {
      result = null;
    }
    return result;
  }

  public static String[] getSupportedImageFormats() {
    return new String[]{"jpg", "jpeg", "png", "gif"};
  }

  public static Date addSeconds(Date d, int seconds) {
    if (d == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    cal.add(Calendar.SECOND, seconds);
    return cal.getTime();
  }

  /**
   * Checks if a string contains only
   * valid e-mail addresses. If not, a {@link VetoException} is thrown.
   */
  public static void validateEmailAddresses(String emailAddresses) throws VetoException {
    if (emailAddresses != null) {
      try {
        InternetAddress[] addresses = InternetAddress.parse(emailAddresses);
        for (InternetAddress address : addresses) {
          address.validate();
        }
      }
      catch (Exception e) {
        throw new VetoException(TEXTS.get("InvalidEmail") + " (" + e.getLocalizedMessage() + ")");
      }
    }
  }

  public static Date truncDateToHour(Date d) {
    if (d == null) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    c.set(Calendar.MINUTE, 0);
    return new Date(c.getTime().getTime());
  }

  public static Long roundTime(Long time, Long timePrecisionUid) {
    if (time != null && timePrecisionUid != null) {
      if (CompareUtility.equals(TimePrecisionCodeType.Precision1sCode.ID, timePrecisionUid)) {
        time = time / 1000 * 1000;
        return time;
      }
      else if (CompareUtility.equals(TimePrecisionCodeType.Precision10sCode.ID, timePrecisionUid)) {
        time = time / 100 * 100;
        return time;
      }
      else if (CompareUtility.equals(TimePrecisionCodeType.Precision100sCode.ID, timePrecisionUid)) {
        time = time / 10 * 10;
        return time;
      }
    }
    return time;
  }

  public static URL findFileLocation(String path, String bundleSymbolicName) throws ProcessingException {
    return Thread.currentThread().getContextClassLoader().getResource(path);
  }

  public static String getCodeText(Class<? extends ICodeType<Long, Long>> codeTypeClass, Long codeUid) throws ProcessingException {
    if (codeUid == null || codeTypeClass == null) {
      return "";
    }
    if (BEANS.get(ICodeService.class) != null) {
      try {
        ICode code = BEANS.get(codeTypeClass).getCode(codeUid);
        if (code != null) {
          return code.getText();
        }
      }
      catch (AssertionException e) {
        // ignore AssertionException due to Unit Tests
      }
    }
    return String.valueOf(codeUid);
  }

  public static String getCodeExtKey(Class<? extends ICodeType<Long, Long>> codeTypeClass, Long codeUid) throws ProcessingException {
    if (codeUid == null || codeTypeClass == null) {
      return "";
    }
    if (BEANS.get(ICodeService.class) != null) {
      try {
        ICode code = BEANS.get(codeTypeClass).getCode(codeUid);
        if (code != null) {
          return code.getExtKey();
        }
      }
      catch (AssertionException e) {
        // ignore AssertionException due to Unit Tests
      }
    }
    return String.valueOf(codeUid);
  }

  public static byte[] getContentAsByteArray(File file) throws ProcessingException {
    try {
      return IOUtility.getContent(new FileInputStream(file), true);
    }
    catch (Exception e) {
      throw new ProcessingException("Could not get content from file", e);
    }
  }

  /**
   * @return Returns <code>true</code> if the given object is in the list of the given elements.
   */
  public static boolean isOneOf(Object o, Object... elements) {
    if (elements == null || elements.length == 0) {
      return false;
    }
    for (Object e : elements) {
      if (CompareUtility.equals(o, e)) {
        return true;
      }
    }
    return false;
  }

  public static String getStackTrace(Throwable t) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    t.printStackTrace(pw);
    pw.flush();
    sw.flush();
    return sw.toString();
  }

  public static Long validateYear(Long rawValue) {
    if (rawValue != null && rawValue < 100 && rawValue >= 0) {
      GregorianCalendar greg = new GregorianCalendar();
      greg.setTime(new Date());
      int currentYear = greg.get(Calendar.YEAR);
      int last2digits = currentYear % 100;
      int century = currentYear - last2digits;
      if (rawValue <= last2digits) {
        rawValue = century + rawValue;
      }
      else {
        rawValue = century - 100 + rawValue;
      }
    }
    return rawValue;
  }

  public static String boxHtmlBody(String s) {
    return StringUtility.box("<html><body>", s, "</body></html>");
  }

  public static boolean isStandalone() {
    // TODO MIG
//    String jdbc = Activator.getDefault().getBundle().getBundleContext().getProperty("jdbcMappingName");
//    if (jdbc != null) {
//      return jdbc.contains("standalone");
//    }
    return false;
  }

  @Deprecated
  public static boolean isRichClient() {
    return true;
  }

  @Deprecated
  public static boolean isWebClient() {
    return false;
  }

  public static String getVersion() {
    return "2.0.0";
  }

  public static BinaryResource createBinaryResource(String path) {
    File file = new File(path);
    return new BinaryResource(file.getName(), IOUtility.getContent(file));
  }

  public static boolean isTestEnvironment() {
    try {
      Thread.currentThread().getContextClassLoader().loadClass(FMilaUtility.class.getCanonicalName() + "Test");
    }
    catch (ClassNotFoundException e) {
      return false;
    }
    return true;
  }

}
