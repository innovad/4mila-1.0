package com.rtiming.client.dataexchange.oe2003;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.runner.SexCodeType;

public final class OE2003Utility {

  private OE2003Utility() {
  }

  public static String exportRelativeTime(Long time) {
    if (time == null) {
      return "";
    }
    Date date = FMilaUtility.addMilliSeconds(DateUtility.truncDate(new Date()), time); // TODO is this correct?
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    return sdf.format(date);
  }

  public static String exportSex(Long sexUid) {
    if (CompareUtility.equals(sexUid, SexCodeType.ManCode.ID)) {
      return "M";
    }
    else if (CompareUtility.equals(sexUid, SexCodeType.WomanCode.ID)) {
      return "F";
    }
    else {
      return "";
    }
  }

  public static String exportRaceStatus(Long raceStatusUid) {

    // 0 = normal
    // 1 = dns (did not start)
    // 2 = dnf (did not finish)
    // 3 = mp (miss punched)

    if (CompareUtility.equals(raceStatusUid, RaceStatusCodeType.OkCode.ID)) {
      return "0";
    }
    else if (CompareUtility.equals(raceStatusUid, RaceStatusCodeType.DidNotStartCode.ID)) {
      return "1";
    }
    else if (CompareUtility.equals(raceStatusUid, RaceStatusCodeType.DidNotFinishCode.ID)) {
      return "2";
    }
    else if (CompareUtility.equals(raceStatusUid, RaceStatusCodeType.MissingPunchCode.ID)) {
      return "3";
    }
    else if (CompareUtility.equals(raceStatusUid, RaceStatusCodeType.NoStartTimeCode.ID)) {
      return "3";
    }
    else {
      // handle other race status (disqualified) not supported by OE as did not finish
      return "2";
    }
  }

  public static String exportYear(Long year) {
    if (year == null) {
      return "";
    }
    if (year >= 0 && year <= 99) {
      return String.valueOf(year);
    }
    if (year >= 1000 && year <= 9999) {
      return String.valueOf(year).substring(2, 4);
    }
    return "";
  }

  public static String exportRunnerName(String firstName, String lastName) {
    if (firstName == null) {
      return lastName;
    }
    if (lastName != null) {
      return firstName + " " + lastName;
    }
    return "";
  }

  public static String exportBoolean(Boolean value) {
    if (value == null) {
      return "0";
    }
    if (value) {
      return "X";
    }
    else {
      return "0";
    }
  }

}
