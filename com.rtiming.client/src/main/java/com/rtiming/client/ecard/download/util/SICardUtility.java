package com.rtiming.client.ecard.download.util;

import java.util.Date;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.ecard.download.PunchFormData;

public final class SICardUtility {

  private SICardUtility() {
  }

  public static final long INVALID_TIME = 61166000L;

  public static PunchFormData readV6Punch(byte[] bytes, int index, long sortcode, Date evtZero) throws ProcessingException {
    if (bytes == null || bytes.length < 4) {
      throw new ProcessingException("SI-Card punch must be 4 bytes.");
    }

    PunchFormData punch = new PunchFormData();

    /* record structure: PTD - CN - PTH - PTL
    CN - control station code number, 0...255 or subsecond value
    PTD - day of week / halfday
    bit 0 - am/pm
    bit 3...1 - day of week, 000 = Sunday, 110 = Saturday
    bit 5...4 - week counter 0�3, relative
    bit 7...6 - control station code number high
    (...1023) (reserved)
    punching time PTH, PTL - 12h binary

    1 subsecond value only for �start� and �finish� possible
    new from sw5.49: bit7=1 in PTD-byte indicates a subsecond value in CN byte (use always code numbers <256 for start/finish) */

    long byte0 = ByteUtility.getLongFromByte(bytes[index]); // PTD
    long code = ByteUtility.getLongFromByte(bytes[index + 1]); // CN
    Long punchTime = ByteUtility.getLongFromBytes(bytes[index + 2], bytes[index + 3]) * 1000L; // PTH, PTL
    if (punchTime == INVALID_TIME) {
      punchTime = null;
    }
    else if ((byte0 & 1) != 0) { // PTD am/pm (am=0, pm=1)
      punchTime += 43200000L; // 12 hours in milliseconds
    }

    punch.getSortCode().setValue(sortcode);
    punch.getControlNo().setValue(Long.toString(code));
    punch.setRawTime(alignToEvtZeroTime(punchTime, evtZero));

    return punch;
  }

  public static PunchFormData readV5Punch(byte[] data, int pos, int sortcode, Date evtZero) {
    Long time;
    long code = ByteUtility.getLongFromByte(data[pos]);
    time = ByteUtility.getLongFromBytes(data[1 + pos], data[2 + pos]) * 1000L;
    if (time == SICardUtility.INVALID_TIME) {
      time = null;
    }
    PunchFormData punch = new PunchFormData();
    punch.getControlNo().setValue("" + code);
    punch.getSortCode().setValue(Long.valueOf(sortcode));
    punch.setRawTime(alignToEvtZeroTimeV5(time, evtZero));
    return punch;
  }

  private static Long alignToEvtZeroTimeV5(Long time, Date evtZero) {
    time = SICardUtility.alignToEvtZeroTime(time, evtZero);
    if (time != null && time < 0) {
      // siV5 is 12h only
      return time + 12 * 60 * 60 * 1000; // 12h
    }
    else {
      return time;
    }
  }

  private static Long alignToEvtZeroTime(Long time, Date evtZero) {
    if (time == null) {
      return null;
    }
    Long evtZeroHoursMinsSecsInMilliSecs = FMilaUtility.getDateDifferenceInMilliSeconds(DateUtility.truncDate(evtZero), evtZero);
    return time - evtZeroHoursMinsSecsInMilliSecs;
  }

  private static long getWeekday(long byte0) {
    // TODO use this method
    String bin = StringUtility.lpad(Long.toBinaryString(byte0), "0", 8);
    String weekday = StringUtility.substring(bin, 4, 3);
    long day = Long.parseLong(weekday, 2) + 1; // add +1 for java notation
    System.out.println("Weekday: " + day);

    String week = StringUtility.substring(bin, 2, 2);
    System.out.println("Week: " + week);

    return day;
  }
}
