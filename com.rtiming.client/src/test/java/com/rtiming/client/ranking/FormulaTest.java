package com.rtiming.client.ranking;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.ranking.AbstractFormulaCode;
import com.rtiming.shared.ranking.AbstractRankingBoxData;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType.PercentFormulaCode;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType.RogainingFormulaCode;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType.ScatterFormulaCode;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType.ScatterPenaltyFormulaCode;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType.ScoreTimeLimitFormulaCode;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType.ScottishRankingFormulaCode;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType.SumPointsCode;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType.SumTimeCode;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType.TimeCode;

/**
 * @author amo
 */
public class FormulaTest {

  @Test
  public void testScatterPenaltyFormula1() throws Exception {
    doScatterPenaltyFormulaTest(2750 * 1000L, "OK");
  }

  @Test
  public void testScatterPenaltyFormula2() throws Exception {
    doScatterPenaltyFormulaTest(2450 * 1000L, "OK", "MP", "OK");
  }

  @Test
  public void testScatterPenaltyFormula3() throws Exception {
    doScatterPenaltyFormulaTest(50 * 1000L, "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK");
  }

  @Test
  public void testScatterPenaltyFormula4() throws Exception {
    doScatterPenaltyFormulaTest(50 * 1000L, "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK");
  }

  @Test
  public void testScatterFormula1() throws Exception {
    doScatterFormulaTest(RaceStatusCodeType.OkCode.ID, "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK");
  }

  @Test
  public void testScatterFormula2() throws Exception {
    doScatterFormulaTest(RaceStatusCodeType.MissingPunchCode.ID, "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK", "OK");
  }

  @Test
  public void testScatterFormula3() throws Exception {
    doScatterFormulaTest(RaceStatusCodeType.MissingPunchCode.ID, "OK", "OK", "OK", "OK", "MP", "OK", "OK", "OK", "OK", "OK");
  }

  @Test
  public void testScatterFormula4() throws Exception {
    doScatterFormulaTest(RaceStatusCodeType.MissingPunchCode.ID, "OK", "OK", "OK", "OK", "IN", "OK", "OK", "OK", "OK", "OK");
  }

  @Test
  public void testScoreTimeLimitFormula1() throws Exception {
    doScoreTimeLimitTest(1000L, 1L, "OK");
  }

  @Test
  public void testScoreTimeLimitFormula2() throws Exception {
    doScoreTimeLimitTest(20 * 60 * 1000L, 2L, "OK", "OK");
  }

  @Test
  public void testScoreTimeLimitFormula3() throws Exception {
    doScoreTimeLimitTest(20 * 60 * 1000L + 1, 1L, "OK", "OK");
  }

  @Test
  public void testScoreTimeLimitFormula4() throws Exception {
    doScoreTimeLimitTest(1000L, 3L, "OK", "OK", "MP", "IN", "WR", "OK");
  }

  @Test
  public void testRogaining1() throws Exception {
    doRogainingTest(1000L, 10L, new String[]{"31"}, new String[]{"OK"});
  }

  @Test
  public void testRogaining2() throws Exception {
    doRogainingTest(1000L, 0L, new String[]{"31"}, new String[]{"MP"});
  }

  @Test
  public void testRogaining3() throws Exception {
    doRogainingTest(1000L, 100L, new String[]{"31", "33"}, new String[]{"OK", "OK"});
  }

  @Test
  public void testRogaining4() throws Exception {
    doRogainingTest(1000L, 40L, new String[]{"40", "31", "99", "100"}, new String[]{"OK", "IN", "OK", "WR"});
  }

  @Test
  public void testRogaining5() throws Exception {
    // negative penalty results in 0
    doRogainingTest(24 * 60 * 60 * 1000 + 1L, 0L, new String[]{}, new String[]{});
  }

  @Test
  public void testRogaining6() throws Exception {
    // points 100, penalty -50
    doRogainingTest(24 * 60 * 60 * 1000 + 500L, 50L, new String[]{"31", "33"}, new String[]{"OK", "OK"});
  }

  @Test
  public void testRogaining7() throws Exception {
    // points 140, penalty -100
    doRogainingTest(24 * 60 * 60 * 1000 + (60 * 1000L + 1L), 40L, new String[]{"31", "33", "40"}, new String[]{"OK", "OK", "OK"});
  }

  @Test
  public void testRogaining8() throws Exception {
    // points 140, penalty -100
    doRogainingTest(24 * 60 * 60 * 1000 + 120 * 1000L, 40L, new String[]{"31", "33", "40"}, new String[]{"OK", "OK", "OK"});
  }

  @Test
  public void testRogaining9() throws Exception {
    // points 140, penalty -100
    doRogainingTest(24 * 60 * 60 * 1000 + (120 * 1000L + 1L), 0L, new String[]{"31", "33", "40"}, new String[]{"OK", "OK", "OK"});
  }

  @Test
  public void testRogaining10() throws Exception {
    // points 100, penalty 0
    doRogainingTest(24 * 60 * 60 * 1000 + 0L, 100L, new String[]{"31", "33"}, new String[]{"OK", "OK"});
  }

  @Test
  public void testRogaining11() throws Exception {
    // overdue
    long statusUid = doRogainingTest(24 * 60 * 60 * 1000 + 30L * 60 * 1000, 0L, new String[]{"31", "33"}, new String[]{"OK", "OK"});
    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, statusUid);
  }

  @Test
  public void testRogaining12() throws Exception {
    // overdue
    long statusUid = doRogainingTest(24 * 60 * 60 * 1000 + 30L * 60 * 1000 + 1, 0L, new String[]{"31", "33"}, new String[]{"OK", "OK"});
    Assert.assertEquals("Race Status", RaceStatusCodeType.DisqualifiedCode.ID, statusUid);
  }

  @Test
  public void testTime1() throws Exception {
    doTimeFormulaTest(RaceStatusCodeType.OkCode.ID, 50L * 1000, 50L * 1000);
  }

  @Test
  public void testTime2() throws Exception {
    doTimeFormulaTest(RaceStatusCodeType.DisqualifiedCode.ID, 50L * 1000, null);
  }

  @Test
  public void testTime3() throws Exception {
    doTimeFormulaTest(RaceStatusCodeType.MissingPunchCode.ID, 50L * 1000, null);
  }

  @Test
  public void testScottishRanking1() throws Exception {
    doScottishFormulaTest(RaceStatusCodeType.OkCode.ID, 15 * 60 * 1000L, 15 * 60 * 1000L, 1000d);
  }

  @Test
  public void testScottishRanking2() throws Exception {
    doScottishFormulaTest(RaceStatusCodeType.OkCode.ID, 15 * 60 * 1000L, 15 * 60 * 1000L / 2, 500d);
  }

  @Test
  public void testScottishRanking3() throws Exception {
    doScottishFormulaTest(RaceStatusCodeType.MissingPunchCode.ID, 15 * 60 * 1000L, 15 * 60 * 1000L / 2, 0d);
  }

  @Test
  public void testScottishRanking4() throws Exception {
    doScottishFormulaTest(RaceStatusCodeType.OkCode.ID, 1500000 * 60 * 1000L, 15 * 60 * 1000L, 0d);
  }

  @Test
  public void testScottishRanking5() throws Exception {
    doScottishFormulaTest(RaceStatusCodeType.MissingPunchCode.ID, null, 15 * 60 * 1000L, 0d);
  }

  @Test
  public void testScottishRanking6() throws Exception {
    doScottishFormulaTest(RaceStatusCodeType.MissingPunchCode.ID, null, null, 0d);
  }

  @Test
  public void testSumTime1() throws Exception {
    doSumTimeTest(new Long[]{5000L}, new String[]{"OK"}, RaceStatusCodeType.OkCode.ID, 5000L);
  }

  @Test
  public void testSumTime2() throws Exception {
    doSumTimeTest(new Long[]{5000L, 4000L}, new String[]{"OK", "OK"}, RaceStatusCodeType.OkCode.ID, 9000L);
  }

  @Test
  public void testSumTime3() throws Exception {
    doSumTimeTest(new Long[]{5000L}, new String[]{"MP"}, RaceStatusCodeType.MissingPunchCode.ID, null);
  }

  @Test
  public void testSumTime4() throws Exception {
    doSumTimeTest(new Long[]{5000L, 2000L}, new String[]{"DNS", "MP"}, RaceStatusCodeType.MissingPunchCode.ID, null);
  }

  @Test
  public void testSumTime5() throws Exception {
    doSumTimeTest(new Long[]{5000L}, new String[]{"DNF"}, RaceStatusCodeType.DidNotFinishCode.ID, null);
  }

  @Test
  public void testSumTime6() throws Exception {
    doSumTimeTest(new Long[]{null, 5000L}, new String[]{"DISQ", "OK"}, RaceStatusCodeType.DisqualifiedCode.ID, 5000L);
  }

  @Test
  public void testSumTime7() throws Exception {
    doSumTimeTest(new Long[]{null, 5000L}, new String[]{"NST", "OK"}, RaceStatusCodeType.NoStartTimeCode.ID, 5000L);
  }

  @Test
  public void testSumTime8() throws Exception {
    doSumTimeTest(new Long[]{null, 5000L}, new String[]{"OK", "OK"}, RaceStatusCodeType.OkCode.ID, 5000L);
  }

  @Test
  public void testSumTime9() throws Exception {
    doSumTimeTest(new Long[]{null}, new String[]{"OK"}, RaceStatusCodeType.OkCode.ID, 0L);
  }

  @Test
  public void testSumTime10() throws Exception {
    // test with unknown race status
    doSumTimeTest(new Long[]{1000L}, new String[]{"UNKNOWN"}, RaceStatusCodeType.DidNotFinishCode.ID, null);
  }

  @Test
  public void testSumPoints1() throws Exception {
    doSumPointsTest(new Long[]{1000L}, new String[]{"OK"}, RaceStatusCodeType.OkCode.ID, 1000L);
  }

  @Test
  public void testSumPoints2() throws Exception {
    doSumPointsTest(new Long[]{1000L, 3000L, 2000L}, new String[]{"OK", "OK", "OK"}, RaceStatusCodeType.OkCode.ID, 5000L);
  }

  @Test
  public void testSumPoints3() throws Exception {
    doSumPointsTest(new Long[]{1000L, 3000L, 2000L}, new String[]{"OK", "MP", "OK"}, RaceStatusCodeType.OkCode.ID, 3000L);
  }

  @Test
  public void testSumPoints4() throws Exception {
    doSumPointsTest(new Long[]{1000L, 3000L, 2000L}, new String[]{"DISQ", "MP", "DNS"}, RaceStatusCodeType.OkCode.ID, 0L);
  }

  @Test
  public void testSumPoints5() throws Exception {
    doSumPointsTest(new Long[]{1000L, 3000L, 2000L}, new String[]{"DISQ", "DNS", "OK"}, RaceStatusCodeType.OkCode.ID, 2000L);
  }

  @Test
  public void testSumPoints6() throws Exception {
    doSumPointsTest(new Long[]{1000L, 3000L, 2000L}, new String[]{"OK", "DNS", "DISQ"}, RaceStatusCodeType.OkCode.ID, 1000L);
  }

  @Test
  public void testSumPoints7() throws Exception {
    doSumPointsTest(new Long[]{1000L, 3000L, 2000L}, new String[]{"DISQ", "OK", "DISQ"}, RaceStatusCodeType.OkCode.ID, 3000L);
  }

  @Test
  public void testSumPoints8() throws Exception {
    // test with unknown race status
    doSumPointsTest(new Long[]{1000L}, new String[]{"UNKNOWN"}, RaceStatusCodeType.OkCode.ID, 0L);
  }

  @Test
  public void testPercent1() throws Exception {
    doPercentFormulaTest(RaceStatusCodeType.OkCode.ID, 10000L, 10000L, 100d);
  }

  @Test
  public void testPercent2() throws Exception {
    doPercentFormulaTest(RaceStatusCodeType.OkCode.ID, 10000L, 7777L, 71.41d);
  }

  @Test
  public void testPercent3() throws Exception {
    doPercentFormulaTest(RaceStatusCodeType.OkCode.ID, 10000L, 1000L, 0d);
  }

  @Test
  public void testPercent4() throws Exception {
    doPercentFormulaTest(RaceStatusCodeType.OkCode.ID, null, 1000L, 0d);
  }

  @Test
  public void testPercent5() throws Exception {
    doPercentFormulaTest(RaceStatusCodeType.MissingPunchCode.ID, 2000L, 1000L, 0d);
  }

  @Test
  public void testPercent6() throws Exception {
    doPercentFormulaTest(RaceStatusCodeType.OkCode.ID, null, null, 0d);
  }

  private void doSumTimeTest(Long[] times, String[] status, long expectedStatusUid, Long expectedResult) throws ProcessingException {
    doSumTestInternal(new SumTimeCode(), times, status, expectedStatusUid, expectedResult);
  }

  private void doSumPointsTest(Long[] times, String[] status, long expectedStatusUid, Long expectedResult) throws ProcessingException {
    doSumTestInternal(new SumPointsCode(), times, status, expectedStatusUid, expectedResult);
  }

  private void doSumTestInternal(AbstractFormulaCode code, Long[] times, String[] status, long expectedStatusUid, Long expectedResult) throws ProcessingException {
    String formula = code.getFormula();
    FormulaScript script = new FormulaScript(formula);

    // Race
    List<SummaryRanking> list = new ArrayList<SummaryRanking>();
    SummaryRanking summary = createSummaryRanking(times, status, code.getFormatUid());
    list.add(summary);

    script.putBinding("races", list);
    script.putBinding("eventCount", times.length);
    script.eval();

    Assert.assertEquals("Status", expectedStatusUid, summary.getStatusUid().longValue());
    if (expectedResult == null) {
      Assert.assertNull("Result", summary.getResult());
    }
    else {
      Assert.assertEquals("Result", expectedResult, summary.getResult(), 0.01d);
    }
  }

  private SummaryRanking createSummaryRanking(Long[] times, String[] status, Long rankingFormatUid) throws ProcessingException {
    AbstractRankingBoxData formulaSettings = RankingTestUtility.createFormulaSettings(rankingFormatUid, 0L, BibNoOrderCodeType.DescendingCode.ID);
    SummaryRanking ranking1 = new SummaryRanking(new Long[]{1L}, 1L, 1L, formulaSettings);
    Assert.assertEquals("Must be same size", times.length, status.length);
    for (int k = 0; k < times.length; k++) {
      EventRanking eventRanking1 = new EventRanking(1L, 1L, times[k], RankingUtility.extKey2RaceStatus(status[k]), formulaSettings);
      eventRanking1.setResult(times[k]);
      ranking1.addEventRanking(k + 1L, eventRanking1);
    }
    return ranking1;
  }

  private void doPercentFormulaTest(long statusUid, Long time, Long winningTime, Double expectedPoints) throws ProcessingException {
    PercentFormulaCode code = new PercentFormulaCode();
    String formula = code.getFormula();
    FormulaScript script = new FormulaScript(formula);

    // Race
    List<EventRanking> list = new ArrayList<EventRanking>();
    AbstractRankingBoxData formulaSettings = RankingTestUtility.createFormulaSettings(code.getFormatUid(), code.getDecimalPlaces(), code.getSortingUid());
    EventRanking ranking1 = new EventRanking(0L, 0L, time, statusUid, formulaSettings);
    list.add(ranking1);

    script.putBinding("races", list);
    script.putBinding("winningTime", winningTime);
    script.eval();

    if (expectedPoints == null) {
      Assert.assertNull("Result", ranking1.getResult());
    }
    else {
      Assert.assertEquals("Result", expectedPoints, ranking1.getResult(), 0.001);
    }
    Assert.assertEquals("Status", statusUid, ranking1.getStatusUid().longValue());
  }

  private void doScottishFormulaTest(long statusUid, Long time, Long winningTime, Double expectedPoints) throws ProcessingException {
    ScottishRankingFormulaCode code = new ScottishRankingFormulaCode();
    String formula = code.getFormula();
    FormulaScript script = new FormulaScript(formula);

    // Race
    List<EventRanking> list = new ArrayList<EventRanking>();
    AbstractRankingBoxData formulaSettings = RankingTestUtility.createFormulaSettings(code.getFormatUid(), code.getDecimalPlaces(), code.getSortingUid());
    EventRanking ranking1 = new EventRanking(0L, 0L, time, statusUid, formulaSettings);
    list.add(ranking1);

    script.putBinding("races", list);
    script.putBinding("winningTime", winningTime);
    script.eval();

    if (expectedPoints == null) {
      Assert.assertNull("Result", ranking1.getResult());
    }
    else {
      Assert.assertEquals("Result", expectedPoints, ranking1.getResult(), 0.1);
    }
    Assert.assertEquals("Status", statusUid, ranking1.getStatusUid().longValue());
  }

  private void doTimeFormulaTest(long statusUid, long time, Long expectedTime) throws ProcessingException {
    TimeCode code = new TimeCode();
    String formula = code.getFormula();
    FormulaScript script = new FormulaScript(formula);

    // Race
    List<EventRanking> list = new ArrayList<EventRanking>();
    AbstractRankingBoxData formulaSettings = RankingTestUtility.createFormulaSettings(code.getFormatUid(), code.getDecimalPlaces(), code.getSortingUid());
    EventRanking ranking1 = new EventRanking(0L, 0L, time, statusUid, formulaSettings);
    list.add(ranking1);

    script.putBinding("races", list);
    script.eval();

    if (expectedTime == null) {
      Assert.assertNull("Result", ranking1.getResult());
    }
    else {
      Assert.assertEquals("Result", expectedTime, ranking1.getResult(), 0.1);
    }
    Assert.assertEquals("Status", statusUid, ranking1.getStatusUid().longValue());
  }

  private void doScatterPenaltyFormulaTest(Long expectedTime, String... codes) throws ProcessingException {
    ScatterPenaltyFormulaCode code = new ScatterPenaltyFormulaCode();
    String formula = code.getFormula();
    FormulaScript script = new FormulaScript(formula);

    // Race
    List<EventRanking> list = new ArrayList<EventRanking>();
    AbstractRankingBoxData formulaSettings = RankingTestUtility.createFormulaSettings(code.getFormatUid(), code.getDecimalPlaces(), code.getSortingUid());
    EventRanking ranking1 = new EventRanking(0L, 0L, 50L * 1000, RaceStatusCodeType.OkCode.ID, formulaSettings);

    // Control
    List<Control> controls = createControls(codes);
    ranking1.setControls(controls);

    list.add(ranking1);
    script.putBinding("races", list);
    script.eval();

    // time (50) + penalty (X * 300)
    Assert.assertEquals("Result", expectedTime, ranking1.getResult(), 0.1);
  }

  private void doScatterFormulaTest(Long expectedRaceStatusUid, String... codes) throws ProcessingException {
    ScatterFormulaCode code = new ScatterFormulaCode();
    String formula = code.getFormula();
    FormulaScript script = new FormulaScript(formula);

    // Race
    List<EventRanking> list = new ArrayList<EventRanking>();
    AbstractRankingBoxData formulaSettings = RankingTestUtility.createFormulaSettings(code.getFormatUid(), code.getDecimalPlaces(), code.getSortingUid());
    EventRanking ranking1 = new EventRanking(0L, 0L, 50000L, RaceStatusCodeType.OkCode.ID, formulaSettings);

    // Control
    List<Control> controls = createControls(codes);
    ranking1.setControls(controls);

    list.add(ranking1);
    script.putBinding("races", list);
    script.eval();

    Assert.assertEquals("Status", expectedRaceStatusUid, ranking1.getStatusUid());
    Assert.assertEquals("Result", 50000L, ranking1.getResult(), 0.01);
  }

  private void doScoreTimeLimitTest(Long time, Long expectedPoints, String... codes) throws ProcessingException {
    ScoreTimeLimitFormulaCode code = new ScoreTimeLimitFormulaCode();
    String formula = code.getFormula();
    FormulaScript script = new FormulaScript(formula);

    // Race
    List<EventRanking> list = new ArrayList<EventRanking>();
    AbstractRankingBoxData formulaSettings = RankingTestUtility.createFormulaSettings(code.getFormatUid(), code.getDecimalPlaces(), code.getSortingUid());
    EventRanking ranking1 = new EventRanking(0L, 0L, time, RaceStatusCodeType.OkCode.ID, formulaSettings);

    // Control
    List<Control> controls = createControls(codes);
    ranking1.setControls(controls);

    list.add(ranking1);
    script.putBinding("races", list);
    script.eval();

    Assert.assertEquals("Points", expectedPoints, ranking1.getResult(), 0.01);
    Assert.assertEquals("Time", time, ranking1.getTime(), 0.01);
  }

  private Long doRogainingTest(Long time, Long expectedPoints, String[] codes, String[] status) throws ProcessingException {
    RogainingFormulaCode code = new RogainingFormulaCode();
    String formula = code.getFormula();
    FormulaScript script = new FormulaScript(formula);

    // Race
    List<EventRanking> list = new ArrayList<EventRanking>();
    AbstractRankingBoxData formulaSettings = RankingTestUtility.createFormulaSettings(code.getFormatUid(), code.getDecimalPlaces(), code.getSortingUid());
    EventRanking ranking1 = new EventRanking(0L, 0L, time, RaceStatusCodeType.OkCode.ID, formulaSettings);

    // Control
    List<Control> controls = createControls(codes, status);
    ranking1.setControls(controls);

    list.add(ranking1);
    script.putBinding("races", list);
    script.eval();

    Assert.assertEquals("Points", expectedPoints, ranking1.getResult(), 0.01);
    Assert.assertEquals("Time", time, ranking1.getTime(), 0.01);

    return ranking1.getStatusUid();
  }

  private List<Control> createControls(String... status) throws ProcessingException {
    List<Control> controls = new ArrayList<Control>();
    for (String s : status) {
      Control control1 = new Control();
      control1.setStatus(s);
      control1.setTypeUid(ControlTypeCodeType.ControlCode.ID);
      controls.add(control1);
    }
    return controls;
  }

  private List<Control> createControls(String[] codes, String[] status) throws ProcessingException {
    Assert.assertEquals("Size does match", codes.length, status.length);
    List<Control> controls = new ArrayList<Control>();
    for (int i = 0; i < codes.length; i++) {
      Control control1 = new Control();
      control1.setStatus(status[i]);
      control1.setControlNo(codes[i]);
      control1.setTypeUid(ControlTypeCodeType.ControlCode.ID);
      controls.add(control1);
    }
    return controls;
  }

}
