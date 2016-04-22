package com.rtiming.client.ranking;

import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.ranking.AbstractRankingBoxData;
import com.rtiming.shared.ranking.RankingFormatCodeType;

/**
 * @author amo
 */
public class AbstractRankingTest {

  @Test
  public void testDecimalPlacesPoints1() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.PointsCode.ID, 3L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking.setResult(1000.5459d);
    Double points = ranking.getResult();
    Assert.assertEquals("Rounded Result", 1000.545, points, 0.0000001d);
  }

  @Test
  public void testDecimalPlacesPoints2() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.PointsCode.ID, 3L, BibNoOrderCodeType.AscendingCode.ID);
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking.setResult(1000.5459d);
    Double points = ranking.getResult();
    Assert.assertEquals("Rounded Result", 1000.546, points, 0.0000001d);
  }

  @Test
  public void testDecimalPlacesPoints3() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.PointsCode.ID, 0L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking.setResult(1000.5459d);
    Double points = ranking.getResult();
    Assert.assertEquals("Rounded Result", 1000, points, 0.0000001d);
  }

  @Test
  public void testDecimalPlacesTime1() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.TimeCode.ID, 0L, BibNoOrderCodeType.AscendingCode.ID);
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking.setResult(1234L);
    Double points = ranking.getResult();
    Assert.assertEquals("Rounded Result", 1000, points, 0.0000001d);
  }

  @Test
  public void testDecimalPlacesTime2() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.TimeCode.ID, 1L, BibNoOrderCodeType.AscendingCode.ID);
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking.setResult(1234L);
    Double points = ranking.getResult();
    Assert.assertEquals("Rounded Result", 1200, points, 0.0000001d);
  }

  @Test
  public void testDecimalPlacesTime3() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.TimeCode.ID, 3L, BibNoOrderCodeType.AscendingCode.ID);
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking.setResult(1234.56d);
    Double points = ranking.getResult();
    Assert.assertEquals("Rounded Result", 1234, points, 0.0000001d);
  }

  @Test
  public void testDecimalPlacesTime4() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.TimeCode.ID, 3L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking.setResult(1234.56d);
    Double points = ranking.getResult();
    Assert.assertEquals("Rounded Result", 1234, points, 0.0000001d);
  }

  @Test
  public void testDecimalPlacesTime5() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.TimeCode.ID, 0L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking.setResult(1234.56d);
    Double points = ranking.getResult();
    Assert.assertEquals("Rounded Result", 1000, points, 0.0000001d);
  }

  @Test
  public void testCompare1() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.PointsCode.ID, 0L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking1 = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking1.setResult(1000L);
    EventRanking ranking2 = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking2.setResult(1500L);
    Assert.assertEquals("Compare", 1, ranking1.compareTo(ranking2));
  }

  @Test
  public void testCompare2() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.PointsCode.ID, 0L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking1 = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking1.setResult(1000L);
    EventRanking ranking2 = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.MissingPunchCode.ID, settings);
    ranking2.setResult(1500L);
    Assert.assertEquals("Compare", -1, ranking1.compareTo(ranking2));
  }

  @Test
  public void testGetPointsFormatted1() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.TimeCode.ID, 0L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking1 = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking1.setResult(1000L);
    Assert.assertEquals("Points formatted", "0:01", ranking1.getPointsFormatted());
  }

  @Test
  public void testGetPointsFormatted2() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.PointsCode.ID, 3L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking1 = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking1.setResult(1000.4567d);
    Assert.assertEquals("Points formatted", "1000.456", ranking1.getPointsFormatted());
  }

  @Test
  public void testSetResult1() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.PointsCode.ID, 2L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking1 = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking1.setResult(1000.4567d);
    Assert.assertEquals("Result", 1000.45d, ranking1.getResult(), 0.0001d);
  }

  @Test
  public void testSetResult2() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.PointsCode.ID, 2L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking1 = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking1.setResult(1000L);
    Assert.assertEquals("Result", 1000.00d, ranking1.getResult(), 0.0001d);
  }

  @Test
  public void testSetResult3() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.PointsCode.ID, 2L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking1 = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking1.setResult(1000);
    Assert.assertEquals("Result", 1000.00d, ranking1.getResult(), 0.0001d);
  }

  @Test
  public void testSetResult4() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.PointsCode.ID, 2L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking1 = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking1.setResult("1000.45");
    Assert.assertEquals("Result", 1000.45d, ranking1.getResult(), 0.0001d);
  }

  @Test
  public void testGetResultRow() throws Exception {
    AbstractRankingBoxData settings = RankingTestUtility.createFormulaSettings(RankingFormatCodeType.PointsCode.ID, 2L, BibNoOrderCodeType.DescendingCode.ID);
    EventRanking ranking1 = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, settings);
    ranking1.setResultRow(new Object[10]);
    Object[] row = ranking1.getResultRow();
    Assert.assertEquals("Size", 11, row.length);
  }

}
