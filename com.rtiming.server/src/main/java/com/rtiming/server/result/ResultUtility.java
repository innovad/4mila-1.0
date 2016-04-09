package com.rtiming.server.result;

import java.util.List;

import org.eclipse.scout.rt.platform.util.CompareUtility;

import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.results.ResultRowData;

public final class ResultUtility {

  private ResultUtility() {
  }

  public static List<ResultRowData> calculateRanks(List<ResultRowData> resultData) {

    long rankCounter = 1;
    Long lastRank = rankCounter;
    Long lastTime = null;
    Long lastEntryNr = null;
    boolean isTeamMember = false;
    for (ResultRowData row : resultData) {
      Long resultRank = null;
      Long raceStatusUid = row.getRaceStatus();
      Long entryNr = row.getEntryNr();
      isTeamMember = CompareUtility.equals(lastEntryNr, entryNr);

      if (CompareUtility.equals(raceStatusUid, RaceStatusCodeType.OkCode.ID)) {
        Long legTime = row.getLegTime();
        if (CompareUtility.equals(legTime, lastTime)) {
          if (isTeamMember) {
            resultRank = null;
          }
          else {
            resultRank = lastRank;
          }
        }
        else {
          resultRank = rankCounter;
        }
        lastTime = legTime;
      }

      row.setRank(resultRank);
      lastEntryNr = entryNr;
      if (!isTeamMember) {
        lastRank = resultRank;
        rankCounter++;
      }
    }

    return resultData;
  }

}
