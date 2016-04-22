package com.rtiming.client.ranking;

import java.util.List;

import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.ranking.RankingFormData;

public class SummaryRankingTest {

  @Test
  public void testMissingEventsAll() throws Exception {
    RankingFormData formData = new RankingFormData();
    SummaryRanking summary = new SummaryRanking(new Long[]{1L, 2L, 3L}, 99L, 99L, formData.getRankingBox());
    List<EventRanking> list = summary.getEventRankings();

    Assert.assertEquals("Events are filled up", 3, list.size());
    Assert.assertEquals("Not Started", RaceStatusCodeType.DidNotStartCode.ID, list.get(0).getStatusUid().longValue());
    Assert.assertEquals("Not Started", RaceStatusCodeType.DidNotStartCode.ID, list.get(1).getStatusUid().longValue());
    Assert.assertEquals("Not Started", RaceStatusCodeType.DidNotStartCode.ID, list.get(2).getStatusUid().longValue());
  }

  @Test
  public void testMissingEventsSome() throws Exception {
    RankingFormData formData = new RankingFormData();
    SummaryRanking summary = new SummaryRanking(new Long[]{1L, 2L, 3L}, 99L, 99L, formData.getRankingBox());
    EventRanking ranking = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    summary.addEventRanking(1L, ranking);
    List<EventRanking> list = summary.getEventRankings();

    Assert.assertEquals("Events are filled up", 3, list.size());
    Assert.assertEquals("Ok", RaceStatusCodeType.OkCode.ID, list.get(0).getStatusUid().longValue());
    Assert.assertEquals("Not Started", RaceStatusCodeType.DidNotStartCode.ID, list.get(1).getStatusUid().longValue());
    Assert.assertEquals("Not Started", RaceStatusCodeType.DidNotStartCode.ID, list.get(2).getStatusUid().longValue());
  }

  @Test
  public void testEventStatusDNS() throws Exception {
    RankingFormData formData = new RankingFormData();
    SummaryRanking summary = new SummaryRanking(new Long[]{1L, 2L, 3L}, 99L, 99L, formData.getRankingBox());
    EventRanking ranking = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    summary.addEventRanking(1L, ranking);

    Assert.assertEquals("Status not started", new RaceStatusCodeType().getCode(RaceStatusCodeType.DidNotStartCode.ID).getExtKey(), summary.getEventStatus());
  }

  @Test
  public void testEventStatusOK() throws Exception {
    RankingFormData formData = new RankingFormData();
    SummaryRanking summary = new SummaryRanking(new Long[]{1L}, 99L, 99L, formData.getRankingBox());
    EventRanking ranking = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    summary.addEventRanking(1L, ranking);

    Assert.assertEquals("Status OK", new RaceStatusCodeType().getCode(RaceStatusCodeType.OkCode.ID).getExtKey(), summary.getEventStatus());
  }

  @Test
  public void testSum() throws Exception {
    RankingFormData formData = new RankingFormData();
    formData.getRankingBox().getDecimalPlaces().setValue(2L);
    SummaryRanking summary = new SummaryRanking(new Long[]{1L, 2L, 3L}, 99L, 99L, formData.getRankingBox());
    EventRanking ranking = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    ranking.setResult(12345.67d);
    summary.addEventRanking(1L, ranking);

    Double sum = summary.getEventResultsSum(3);
    Assert.assertEquals("Event Result Sum", 12345.67d, sum, 0.01d);
  }

  @Test
  public void testSumWithBestResults1() throws Exception {
    RankingFormData formData = new RankingFormData();
    formData.getRankingBox().getDecimalPlaces().setValue(2L);
    formData.getRankingBox().getSorting().setValue(BibNoOrderCodeType.DescendingCode.ID);
    SummaryRanking summary = new SummaryRanking(new Long[]{1L, 2L, 3L}, 99L, 99L, formData.getRankingBox());

    EventRanking ranking1 = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    ranking1.setResult(12345.67d);
    summary.addEventRanking(1L, ranking1);

    EventRanking ranking2 = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    ranking2.setResult(12345.67d);
    summary.addEventRanking(2L, ranking2);

    EventRanking ranking3 = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    ranking3.setResult(12345.67d);
    summary.addEventRanking(3L, ranking3);

    Double sum = summary.getEventResultsSum(2);
    Assert.assertEquals("Event Result Sum", 2 * 12345.67d, sum, 0.01d);
  }

  @Test
  public void testSumWithBestResults2() throws Exception {
    RankingFormData formData = new RankingFormData();
    formData.getRankingBox().getDecimalPlaces().setValue(0L);
    formData.getRankingBox().getSorting().setValue(BibNoOrderCodeType.AscendingCode.ID);
    SummaryRanking summary = new SummaryRanking(new Long[]{1L, 2L, 3L}, 99L, 99L, formData.getRankingBox());

    EventRanking ranking1 = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    ranking1.setResult(10d);
    summary.addEventRanking(1L, ranking1);

    EventRanking ranking2 = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    ranking2.setResult(9d);
    summary.addEventRanking(2L, ranking2);

    EventRanking ranking3 = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    ranking3.setResult(8d);
    summary.addEventRanking(3L, ranking3);

    Double sum = summary.getEventResultsSum(2);
    Assert.assertEquals("Event Result Sum", 17d, sum, 0.01d);
  }

  @Test
  public void testSumWithBestResultsMoreThanEvents() throws Exception {
    RankingFormData formData = new RankingFormData();
    formData.getRankingBox().getDecimalPlaces().setValue(0L);
    formData.getRankingBox().getSorting().setValue(BibNoOrderCodeType.AscendingCode.ID);
    SummaryRanking summary = new SummaryRanking(new Long[]{1L, 2L, 3L}, 99L, 99L, formData.getRankingBox());

    Double sum = NumberUtility.nvl(summary.getEventResultsSum(Integer.MAX_VALUE), 0d);
    Assert.assertEquals("Event Result Sum", 0d, sum, 0.01d);
  }

  @Test
  public void testAddColumnCount() throws Exception {
    RankingFormData formData = new RankingFormData();
    SummaryRanking summary = new SummaryRanking(new Long[]{1L, 2L, 3L}, 99L, 99L, formData.getRankingBox());
    Assert.assertEquals("No events * 2 + Ranking Col = 1", 1, summary.getAdditionalColumnCount());

    EventRanking ranking1 = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    ranking1.setResult(10d);
    summary.addEventRanking(1L, ranking1);
    Assert.assertEquals("1 event * 2 + Ranking Col = 3", 3, summary.getAdditionalColumnCount());
  }

}
