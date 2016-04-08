package com.rtiming.client.ranking;

import java.util.HashMap;
import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.ICode;

import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;

public final class RankingUtility {

  private static HashMap<Object, ICode> raceStatusUidMap = new HashMap<Object, ICode>();
  private static HashMap<String, ICode> raceExtKeyMap = new HashMap<String, ICode>();
  private static HashMap<Object, ICode> controlStatusUidMap = new HashMap<Object, ICode>();
  private static HashMap<String, ICode> controlExtKeyMap = new HashMap<String, ICode>();
  private static Boolean initialized = Boolean.FALSE;

  private RankingUtility() throws ProcessingException {
  }

  private static void init() throws ProcessingException {
    synchronized (initialized) {
      if (initialized == false) {
        RaceStatusCodeType type = new RaceStatusCodeType();
        List<? extends ICode<Long>> codes = type.getCodes(false);
        for (ICode code : codes) {
          raceStatusUidMap.put(code.getId(), code);
          raceExtKeyMap.put(code.getExtKey(), code);
        }

        ControlStatusCodeType cType = new ControlStatusCodeType();
        List<? extends ICode<Long>> cCodes = cType.getCodes(false);
        for (ICode code : cCodes) {
          controlStatusUidMap.put(code.getId(), code);
          controlExtKeyMap.put(code.getExtKey(), code);
        }
      }
      initialized = true;
    }
  }

  public static String raceStatus2ExtKey(Long statusUid) throws ProcessingException {
    if (statusUid != null) {
      init();
      ICode<?> code = raceStatusUidMap.get(statusUid);
      if (code != null) {
        return code.getExtKey();
      }
    }
    return "";
  }

  public static Long extKey2RaceStatus(Object extKey) throws ProcessingException {
    init();
    @SuppressWarnings("unchecked")
    ICode<Long> code = raceExtKeyMap.get(extKey);
    if (code != null) {
      return code.getId();
    }
    return null;
  }

  public static String controlStatus2ExtKey(Long statusUid) throws ProcessingException {
    if (statusUid != null) {
      init();
      ICode<?> code = controlStatusUidMap.get(statusUid);
      if (code != null) {
        return code.getExtKey();
      }
    }
    return "";
  }

  public static Long extKey2ControlStatus(Object extKey) throws ProcessingException {
    init();
    @SuppressWarnings("unchecked")
    ICode<Long> code = controlExtKeyMap.get(extKey);
    if (code != null) {
      return code.getId();
    }
    return null;
  }

  public static void calculateRankings(List<? extends AbstractRanking> list) {
    long rank = 1;
    long lastRank = 1;
    Double lastPoints = null;
    for (AbstractRanking ranking : list) {
      if (CompareUtility.equals(ranking.getStatusUid(), RaceStatusCodeType.OkCode.ID)) {
        if (CompareUtility.equals(lastPoints, ranking.getResult())) {
          ranking.setRank(lastRank);
        }
        else {
          ranking.setRank(rank);
          lastRank = rank;
        }
        rank++;
      }
      lastPoints = ranking.getResult();
    }
  }

}
