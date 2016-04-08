package com.rtiming.shared.entry;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.TriState;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.shared.dao.RtClassAge;

/**
 * 
 */
public class AgeUtility {

  public static TriState isRunnerValidForClassAge(Date evtZero, Long classUid, Long clientNr, Long sexUid, Long yearOfBirth, List<RtClassAge> settings) throws ProcessingException {
    if (settings == null || evtZero == null) {
      throw new IllegalArgumentException("Settings and evtZero must be provided");
    }

    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(evtZero);
    int eventYear = calendar.get(Calendar.YEAR);
    boolean settingsFound = false;
    TriState result = TriState.FALSE;

    for (RtClassAge setting : settings) {
      if (CompareUtility.equals(classUid, setting.getClassUid()) && CompareUtility.equals(clientNr, setting.getId().getClientNr())) {
        settingsFound = true;
        Long classSexUid = setting.getSexUid();
        Long yearFrom = eventYear - NumberUtility.nvl(setting.getAgeTo(), Integer.MAX_VALUE);
        Long yearTo = eventYear - NumberUtility.nvl(setting.getAgeFrom(), Integer.MIN_VALUE);

        long yearOfBirthNotNull = NumberUtility.nvl(yearOfBirth, Long.MIN_VALUE);
        boolean ageCheck = yearOfBirthNotNull >= yearFrom && yearOfBirthNotNull <= yearTo;
        boolean sexCheck = classSexUid == null || CompareUtility.equals(classSexUid, sexUid);

        if (yearOfBirth == null && sexUid == null) {
          result = updateState(result, TriState.TRUE);
        }
        else if (sexUid != null && yearOfBirth != null) {
          result = updateState(result, TriState.parse(ageCheck && sexCheck));
        }
        else if (sexUid == null) {
          result = updateState(result, TriState.parse(ageCheck));
        }
        else if (yearOfBirth == null) {
          result = updateState(result, TriState.parse(sexCheck));
        }
      }
    }

    if (!settingsFound) {
      return TriState.UNDEFINED;
    }
    else {
      return result;
    }
  }

  private static TriState updateState(TriState state, TriState newState) {
    if (CompareUtility.equals(state, TriState.TRUE)) {
      return TriState.TRUE;
    }
    return newState;
  }

}
