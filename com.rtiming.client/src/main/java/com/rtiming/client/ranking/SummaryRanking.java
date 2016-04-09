package com.rtiming.client.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.ranking.AbstractRankingBoxData;
import com.rtiming.shared.ranking.RankingEventFormData;
import com.rtiming.shared.ranking.RankingFormatCodeType;

public class SummaryRanking extends AbstractRanking {

  private final LinkedHashMap<Long, EventRanking> eventRankings;
  private final Long[] eventNrs;

  public SummaryRanking(Long[] eventNrs, Long entryNr, Long runnerNr, AbstractRankingBoxData formulaSettings) {
    super(entryNr, runnerNr, null, RaceStatusCodeType.OkCode.ID, formulaSettings);
    this.eventRankings = new LinkedHashMap<Long, EventRanking>();
    this.eventNrs = eventNrs;
  }

  public void addEventRanking(Long eventNr, EventRanking ranking) {
    eventRankings.put(eventNr, ranking);
  }

  public List<EventRanking> getEventRankings() {
    if (eventRankings.size() != eventNrs.length) {
      for (Long eventNr : eventNrs) {
        if (eventRankings.get(eventNr) == null) {
          EventRanking notParticipatedRanking = new EventRanking(getEntryNr(), getRunnerNr(), null, RaceStatusCodeType.DidNotStartCode.ID, new RankingEventFormData().getRankingBox());
          eventRankings.put(eventNr, notParticipatedRanking);
        }
      }
    }
    return new ArrayList<EventRanking>(eventRankings.values());
  }

  public Double getEventResultsSum(int bestResultCount) {
    List<EventRanking> list = new ArrayList<EventRanking>();
    list.addAll(getEventRankings());
    Collections.sort(list);

    Double result = CompareUtility.equals(getFormatUid(), RankingFormatCodeType.TimeCode.ID) ? null : 0d;
    for (int k = 0; k < bestResultCount && k < list.size(); k++) {
      if (CompareUtility.equals(list.get(k).getStatusUid(), RaceStatusCodeType.OkCode.ID)) {
        result = NumberUtility.nvl(result, 0d) + NumberUtility.nvl(list.get(k).getResult(), 0d);
      }
      list.get(k).setBestRanking(true);
    }
    for (int k = bestResultCount; k < list.size(); k++) {
      list.get(k).setBestRanking(false);
    }

    return result;
  }

  @Override
  protected int getAdditionalColumnCount() {
    return 1 + eventRankings.size() * 2;
  }

  private Long getEventStatusUid() {
    List<Long> eventStatusUids = new ArrayList<Long>();
    boolean isOk = true;
    for (EventRanking ranking : getEventRankings()) {
      isOk = isOk && (CompareUtility.equals(ranking.getStatusUid(), RaceStatusCodeType.OkCode.ID));
      eventStatusUids.add(ranking.getStatusUid());
    }
    // priority
    if (isOk) {
      return RaceStatusCodeType.OkCode.ID;
    }
    else if (eventStatusUids.contains(RaceStatusCodeType.DisqualifiedCode.ID)) {
      return RaceStatusCodeType.DisqualifiedCode.ID;
    }
    else if (eventStatusUids.contains(RaceStatusCodeType.DidNotFinishCode.ID)) {
      return RaceStatusCodeType.DidNotFinishCode.ID;
    }
    else if (eventStatusUids.contains(RaceStatusCodeType.MissingPunchCode.ID)) {
      return RaceStatusCodeType.MissingPunchCode.ID;
    }
    else if (eventStatusUids.contains(RaceStatusCodeType.DidNotStartCode.ID)) {
      return RaceStatusCodeType.DidNotStartCode.ID;
    }
    else if (eventStatusUids.contains(RaceStatusCodeType.NoStartTimeCode.ID)) {
      return RaceStatusCodeType.NoStartTimeCode.ID;
    }
    return RaceStatusCodeType.DidNotFinishCode.ID;
  }

  public String getEventStatus() throws ProcessingException {
    return RankingUtility.raceStatus2ExtKey(getEventStatusUid());
  }

}
