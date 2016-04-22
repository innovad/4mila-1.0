package com.rtiming.client.result.split;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.event.RaceControlRowData;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.race.TimePrecisionCodeType;

public class SplitCalculatorTest {

  @Test
  public void test1() throws Exception {
    List<RaceControlRowData> splits = new ArrayList<>();
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(null, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 0, result.size());
  }

  @Test
  public void test2() throws Exception {
    List<RaceControlRowData> splits = new ArrayList<>();
    RaceControlRowData control = new RaceControlRowData();
    splits.add(control);
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(null, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 1, result.size());
  }

  @Test
  public void test3() throws Exception {
    List<RaceControlRowData> splits = createList(
        createControl(1L, "31", 1L, null, null)
        );
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(null, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 1, result.size());
  }

  @Test
  public void test4() throws Exception {
    List<RaceControlRowData> splits = createList(
        createControl(1L, "31", 1L, null, null),
        createControl(1L, "32", 2L, null, null),
        createControl(1L, "33", 3L, null, null)
        );
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(null, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 1, result.size());
  }

  @Test
  public void test5() throws Exception {
    List<RaceControlRowData> splits = createList(
        createControl(1L, "31", 1L, null, null),
        createControl(1L, "32", 2L, null, null),
        createControl(3L, "31", 1L, null, null)
        );
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(null, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 2, result.size());
  }

  @Test
  public void testWinnerAlignment1() throws Exception {
    List<RaceControlRowData> splits = createList(
        createControl(1L, "31", 1L, null, null),
        createControl(1L, "32", 2L, null, null),
        createControl(3L, "32", 1L, null, null),
        createControl(3L, "31", 2L, null, null)
        );
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(1L, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 2, result.size());
    assertControlOrder(result.get(1L), "31", "32");
    assertControlOrder(result.get(3L), "31", "32");
  }

  @Test
  public void testWinnerAlignment2() throws Exception {
    List<RaceControlRowData> splits = createList(
        createControl(1L, "31", 1L, null, null),
        createControl(1L, "32", 2L, null, null),
        createControl(3L, "32", 1L, null, null),
        createControl(3L, "35", 2L, null, null)
        );
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(1L, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 2, result.size());
    assertControlOrder(result.get(1L), "31", "32");
    assertControlOrder(result.get(3L), "32", "35");
  }

  @Test
  public void testWinnerAlignment3() throws Exception {
    List<RaceControlRowData> splits = createList(
        createControl(1L, "31", 1L, null, null),
        createControl(1L, "32", 2L, null, null),
        createControl(3L, "35", 1L, null, null),
        createControl(3L, "32", 2L, null, null)
        );
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(1L, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 2, result.size());
    assertControlOrder(result.get(1L), "31", "32");
    assertControlOrder(result.get(3L), "32", "35");
  }

  @Test
  public void testWinnerAlignment4() throws Exception {
    List<RaceControlRowData> splits = createList(
        createControl(1L, "31", 1L, null, null),
        createControl(1L, "32", 2L, null, null),
        createControl(1L, "31", 3L, null, null),
        createControl(3L, "32", 2L, null, null),
        createControl(3L, "31", 3L, null, null),
        createControl(3L, "31", 4L, null, null)
        );
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(1L, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 2, result.size());
    assertControlOrder(result.get(1L), "31", "32", "31");
    assertControlOrder(result.get(3L), "31", "32", "31");
  }

  @Test
  public void testTimeCalculation1() throws Exception {
    List<RaceControlRowData> splits = createList(
        createControl(1L, "31", 1L, 0L, 0L),
        createControl(1L, "32", 2L, 3000L, 3000L),
        createControl(1L, "33", 3L, 2000L, 5000L),
        createControl(3L, "31", 1L, 0L, 0L),
        createControl(3L, "32", 2L, 4000L, 4000L),
        createControl(3L, "33", 3L, 2000L, 6000L)
        );
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(1L, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 2, result.size());
    assertControlOrder(result.get(1L), "31", "32", "33");
    assertControlOrder(result.get(3L), "31", "32", "33");
    assertOverallTimes(result.get(1L), 0L, 3000L, 5000L);
    assertOverallTimes(result.get(3L), 0L, 4000L, 6000L);
  }

  @Test
  public void testTimeCalculation2() throws Exception {
    List<RaceControlRowData> splits = createList(
        createControl(1L, "31", 1L, 0L, 0L),
        createControl(1L, "32", 2L, 3000L, 3000L),
        createControl(1L, "33", 3L, 2000L, 5000L),
        createControl(3L, "31", 1L, 0L, 0L),
        createControl(3L, "33", 2L, 4000L, 4000L),
        createControl(3L, "32", 3L, 2000L, 6000L)
        );
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(1L, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 2, result.size());
    assertControlOrder(result.get(1L), "31", "32", "33");
    assertControlOrder(result.get(3L), "31", "32", "33");
    assertOverallTimes(result.get(1L), 0L, 3000L, 5000L);
    assertOverallTimes(result.get(3L), 0L, 2000L, 6000L);
  }

  @Test
  public void testTimeCalculation3() throws Exception {
    List<RaceControlRowData> splits = createList(
        createControl(1L, "31", 1L, 0L, 0L),
        createControl(1L, "32", 2L, 3000L, 3000L),
        createControl(1L, "33", 3L, 2000L, 5000L),
        createControl(3L, "31", 1L, 0L, 0L),
        createControl(3L, "33", 2L, 4000L, 4000L),
        createControl(3L, "32", 3L, null, null)
        );
    Map<Long, List<SplitTime>> result = SplitCalculator.calculate(1L, TimePrecisionCodeType.Precision1sCode.ID, splits);
    Assert.assertEquals("Size", 2, result.size());
    assertControlOrder(result.get(1L), "31", "32", "33");
    assertControlOrder(result.get(3L), "31", "32", "33");
    assertOverallTimes(result.get(1L), 0L, 3000L, 5000L);
    assertOverallTimes(result.get(3L), 0L, null, 4000L);
  }

  private void assertControlOrder(List<SplitTime> splits, String... controlNos) {
    Assert.assertEquals("Size", controlNos.length, splits.size());
    int k = 0;
    for (SplitTime split : splits) {
      Assert.assertEquals("Control No", controlNos[k], split.getControl().getControlNo());
      k++;
    }
  }

  private void assertOverallTimes(List<SplitTime> splits, Long... overallTimes) {
    Assert.assertEquals("Size", overallTimes.length, splits.size());
    int k = 0;
    for (SplitTime split : splits) {
      Assert.assertEquals("Time", overallTimes[k], split.getOverallTime().getOverallTimeRaw());
      k++;
    }
  }

  private RaceControlRowData createControl(Long raceNr, String controlNo, Long sortCode, Long legTimeRaw, Long overallTimeRaw) {
    RaceControlRowData control = new RaceControlRowData();
    control.setRaceNr(raceNr);
    control.setSortCode(sortCode);
    control.setControlNo(controlNo);
    control.setOverallTimeRaw(overallTimeRaw);
    control.setLegTimeRaw(legTimeRaw);
    control.setStatusUid(ControlStatusCodeType.OkCode.ID);
    return control;
  }

  private List<RaceControlRowData> createList(RaceControlRowData... rows) {
    List<RaceControlRowData> splits = new ArrayList<>();
    splits.addAll(Arrays.asList(rows));
    return splits;
  }

}
