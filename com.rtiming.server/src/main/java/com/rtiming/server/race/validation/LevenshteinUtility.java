package com.rtiming.server.race.validation;

import java.util.ArrayList;
import java.util.List;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;

/**
 * 
 */
public final class LevenshteinUtility {

  private LevenshteinUtility() {
  }

  /**
   * @param plannedControls
   * @param punchedControls
   * @param matrix
   *          the levenshtein matrix
   * @return
   */
  public static LevenshteinResult backtrace(List<RaceControlBean> plannedControls, List<RaceControlBean> punchedControls, int[][] matrix) {
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;
    List<RaceControlBean> additional = new ArrayList<RaceControlBean>();

    int plannedPos = plannedControls.size() - 1;
    int punchedPos = punchedControls.size() - 1;

    while (plannedPos >= 0 && punchedPos >= 0) {
      RaceControlBean planned = plannedControls.get(plannedPos);
      RaceControlBean punched = punchedControls.get(punchedPos);

      if (RaceValidationUtility.areEqual(punched, planned)) {
        // Ok
        if (planned.isManualStatus()) {
          raceStatusUid = ControlStatusUtility.calculateRaceStatusFromManualControlStatus(raceStatusUid, planned);
        }
        else {
          planned.setControlStatusUid(ControlStatusCodeType.OkCode.ID);
          planned.setPunchTime(punched.getPunchTime());
        }
        plannedPos--;
        punchedPos--;
      }
      else if (planned.isManualStatus()) {
        // Handle manual control status
        raceStatusUid = ControlStatusUtility.calculateRaceStatusFromManualControlStatus(raceStatusUid, planned);
        plannedPos--;
      }
      else {
        // compare the left, upper-left and top value from the current position, choose the greatest
        int max = max(matrix[punchedPos][plannedPos], matrix[punchedPos + 1][plannedPos], matrix[punchedPos][plannedPos + 1]);
        mpVariant: {
          // current *planned* control is missing
          // current *punched* control is superfluous (new code: wrong control?)
          if (max == matrix[punchedPos][plannedPos]) {
            raceStatusUid = ControlStatusUtility.setMissingControlStatus(planned, raceStatusUid);
            punched.setControlStatusUid(ControlStatusCodeType.WrongCode.ID);
            additional.add(punched);
            plannedPos--;
            punchedPos--;
            break mpVariant;
          }
          // current *punched* control is superfluous
          if (max == matrix[punchedPos][plannedPos + 1]) {
            punched.setControlStatusUid(ControlStatusCodeType.AdditionalCode.ID);
            additional.add(punched);
            punchedPos--;
            break mpVariant;
          }
          // current *planned* control is missing
          if (max == matrix[punchedPos + 1][plannedPos]) {
            raceStatusUid = ControlStatusUtility.setMissingControlStatus(planned, raceStatusUid);
            plannedPos--;
            break mpVariant;
          }
        }
      }
    }
    while (plannedPos >= 0) {
      // missing
      RaceControlBean planned = plannedControls.get(plannedPos);
      if (planned.isManualStatus()) {
        raceStatusUid = ControlStatusUtility.calculateRaceStatusFromManualControlStatus(raceStatusUid, planned);
      }
      else {
        raceStatusUid = ControlStatusUtility.setMissingControlStatus(planned, raceStatusUid);
      }
      plannedPos--;
    }
    while (punchedPos >= 0) {
      // superfluous
      RaceControlBean punched = punchedControls.get(punchedPos);
      punched.setControlStatusUid(ControlStatusCodeType.AdditionalCode.ID);
      additional.add(punched);
      punchedPos--;
    }

    // Analyze Wrong Order
    List<String> plannedList = new ArrayList<String>();
    for (RaceControlBean planned : plannedControls) {
      plannedList.add(planned.getControlNo());
    }
    for (RaceControlBean control : additional) {
      if (plannedList.contains(control.getControlNo())) {
        control.setControlStatusUid(ControlStatusCodeType.WrongCode.ID);
      }
    }

    return new LevenshteinResult(additional, raceStatusUid);
  }

  /**
   * @param plannedControls
   * @param punchedControls
   * @return the levenshtein distance matrix
   */
  public static int[][] calculateMatrix(List<RaceControlBean> plannedControls, List<RaceControlBean> punchedControls) {
    if (plannedControls == null || punchedControls == null) {
      throw new IllegalArgumentException("Controls must not be null");
    }

    int n = plannedControls.size();
    int m = punchedControls.size();
    int[][] matrix = new int[m + 1][n + 1];
    for (int i = 1; i < matrix.length; i++) {
      for (int j = 1; j < matrix[0].length; j++) {
        int commonLength = matrix[i - 1][j - 1];
        if (!RaceValidationUtility.areEqual(punchedControls.get(i - 1), plannedControls.get(j - 1)) || lengthLimit(commonLength, i, j)) {
          // codes are not equal, or length exceeds index potential
          matrix[i][j] = max(matrix[i - 1][j - 1], matrix[i - 1][j], matrix[i][j - 1]);
        }
        else {
          // longest sequence (in context) len += 1
          matrix[i][j] = commonLength + 1;
        }
      }
    }
    return matrix;
  }

  private static boolean lengthLimit(int length, int i, int j) {
    return length > Math.min(i, j);
  }

  private static int max(int a, int b, int c) {
    return Math.max(a, Math.max(b, c));
  }

}
