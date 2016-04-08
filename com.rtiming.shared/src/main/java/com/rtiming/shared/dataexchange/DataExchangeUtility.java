package com.rtiming.shared.dataexchange;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.runner.SexCodeType;

public final class DataExchangeUtility {

  private static IScoutLogger logger = ScoutLogManager.getLogger(DataExchangeUtility.class);

  private DataExchangeUtility() {

  }

  public static Field[] getAllFields(Class clazz) {
    ArrayList<Field> allFieldsRec = getAllFieldsRec(clazz, new ArrayList<Field>());
    return allFieldsRec.toArray(new Field[allFieldsRec.size()]);
  }

  private static ArrayList<Field> getAllFieldsRec(Class clazz, ArrayList<Field> list) {
    Class superClazz = clazz.getSuperclass();
    if (superClazz != null) {
      getAllFieldsRec(superClazz, list);
    }
    list.addAll(Arrays.asList(clazz.getDeclaredFields()));
    return list;
  }

  public static boolean parseBoolean(String string) {
    if (StringUtility.isNullOrEmpty(string) || "0".equals(string)) {
      return false;
    }
    return true;
  }

  public static Long parseSex(String string) {
    Long sexUid = null;
    if (StringUtility.equalsIgnoreCase(string, "M")) {
      sexUid = SexCodeType.ManCode.ID;
    }
    else if (StringUtility.equalsIgnoreCase(string, "W") || StringUtility.equalsIgnoreCase(string, "F")) {
      sexUid = SexCodeType.WomanCode.ID;
    }
    return sexUid;
  }

  public static Long parseLong(String string) {
    Long result = null;
    if (StringUtility.isNullOrEmpty(string)) {
      return result;
    }
    try {
      result = Long.parseLong(string);
    }
    catch (Exception e) {
      logger.warn("Parsing Long value failed: " + string);
    }
    return result;
  }

  public static Double parseDouble(String string) {
    Double result = null;
    if (StringUtility.isNullOrEmpty(string)) {
      return result;
    }
    try {
      result = Double.parseDouble(string);
    }
    catch (Exception e) {
      logger.warn("Parsing Double value failed: " + string);
    }
    return result;
  }

  public static Date parseDate(Date evtZero, String string) {
    Date date = null;
    if (StringUtility.isNullOrEmpty(string)) {
      return date;
    }
    try {
      Date starttime = DateUtility.parse(string, "H:m:s");
      GregorianCalendar greg = new GregorianCalendar();
      greg.setTime(starttime);

      long ms = 1000 * (greg.get(Calendar.SECOND) + greg.get(Calendar.MINUTE) * 60 + greg.get(Calendar.HOUR) * 3600);
      date = FMilaUtility.addMilliSeconds(evtZero, ms);
    }
    catch (Exception e) {
      logger.warn("Parsing Date value failed: " + string);
    }
    return date;
  }

  public static Long parseLongEx(String string) {
    Long result = 0L;
    if (StringUtility.isNullOrEmpty(string)) {
      return result;
    }
    else {
      try {
        string = string.trim();
        result = Long.parseLong(string);
      }
      catch (NumberFormatException e) {
        logger.warn("Parsing Long value failed: " + string);
      }
    }
    return result;
  }

  /**
   * @param year
   *          of birth, either two-digit or four-digit
   * @return four-digit year of birth, based on current year
   */
  public static Long parseYear(String year) {
    Long result = null;
    try {
      result = Long.parseLong(year);
    }
    catch (Exception e) {
      // nop
    }
    if (result == null) {
      return null;
    }
    if (result >= 1900 && result <= 9999) {
      return result;
    }
    GregorianCalendar greg = new GregorianCalendar();
    greg.setTime(new Date());
    int currentYear = greg.get(Calendar.YEAR);
    int currentYear2digit = currentYear % 100;
    int currentAge = currentYear - currentYear2digit;
    if (result >= currentYear2digit && result <= 99) {
      return (currentAge - 100) + result;
    }
    if (result <= currentYear2digit && result >= 0) {
      return currentAge + result;
    }
    return null;
  }

  public static String getCodeText(Long codeUid, Class<? extends ICodeType> codeTypeClass) {
    if (codeUid != null && codeTypeClass != null) {
      ICodeType codeType = CODES.getCodeType(codeTypeClass);
      if (codeType != null) {
        ICode code = codeType.getCode(codeUid);
        if (code != null) {
          return code.getText();
        }
      }
    }
    return "";
  }

  public static String exportLong(Long value) {
    if (value == null) {
      return null;
    }
    return String.valueOf(value);
  }

}
