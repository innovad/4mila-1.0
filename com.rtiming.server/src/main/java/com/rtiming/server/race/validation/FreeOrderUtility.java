package com.rtiming.server.race.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.scout.rt.platform.util.CompareUtility;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;

/**
 * 
 */
public final class FreeOrderUtility {

  public static FreeOrderResult validateFreeOrderGroup(List<RaceControlBean> freeOrderGroup, List<RaceControlBean> punchedControls) {
    if (freeOrderGroup == null || punchedControls == null) {
      throw new IllegalArgumentException("Controls must not be null");
    }
    punchedControls = new ArrayList<>(punchedControls);

    Long freeOrderGroupSortCode = null;
    long raceStatusUid = RaceStatusCodeType.OkCode.ID;

    if (freeOrderGroup.size() > 1) {
      // there is a free order group with at least 2 controls
      freeOrderGroupSortCode = freeOrderGroup.get(0).getSortcode();
      int groupSize = freeOrderGroup.size();
      int punchedControlCount = 0;

      for (RaceControlBean freeOrderControl : freeOrderGroup) {
        int p = 0;
        for (RaceControlBean punchedControl : punchedControls) {
          if (RaceValidationUtility.areEqual(freeOrderControl, punchedControl)) {
            // drop control from punched controls
            punchedControls.remove(p);
            // mark control as ok
            freeOrderControl.setControlStatusUid(ControlStatusCodeType.OkCode.ID);
            freeOrderControl.setPunchTime(punchedControl.getPunchTime());
            punchedControlCount++;
            break;
          }
          p++;
        }
      }

      if (punchedControlCount < groupSize) {
        // not all controls of group found
        // in future, check here if missing some controls is allowed
        for (RaceControlBean missing : freeOrderGroup) {
          if (missing.getControlStatusUid() == null || missing.getControlStatusUid() == ControlStatusCodeType.InitialStatusCode.ID) {
            raceStatusUid = ControlStatusUtility.setMissingControlStatus(missing, raceStatusUid);
          }
        }
      }
    }
    freeOrderGroup.clear();
    return new FreeOrderResult(raceStatusUid, freeOrderGroupSortCode);
  }

  public static void sortControlsBySortCodeAndTime(List<RaceControlBean> list) {
    if (list == null) {
      throw new IllegalArgumentException("Controls must not be null");
    }

    Comparator<? super RaceControlBean> freeOrderComparator = new Comparator<RaceControlBean>() {
      @Override
      public int compare(RaceControlBean rc1, RaceControlBean rc2) {
        if (rc1 == null || rc1.getSortcode() == null) {
          return 1;
        }
        else if (rc2 == null || rc2.getSortcode() == null) {
          return -1;
        }
        else if (CompareUtility.notEquals(rc1.getSortcode(), rc2.getSortcode())) {
          return rc1.getSortcode().compareTo(rc2.getSortcode());
        }
        else if (rc1.getPunchTime() != null && rc2.getPunchTime() == null) {
          return -1;
        }
        else if (rc1.getPunchTime() == null && rc2.getPunchTime() != null) {
          return 1;
        }
        else if (rc1.getPunchTime() != null && rc2.getPunchTime() != null) {
          return rc1.getPunchTime().compareTo(rc2.getPunchTime());
        }
        return 0;
      }
    };
    Collections.sort(list, freeOrderComparator);
  }

}
