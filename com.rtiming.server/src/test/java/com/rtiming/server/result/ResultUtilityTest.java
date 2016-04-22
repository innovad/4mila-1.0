package com.rtiming.server.result;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.results.ResultRowData;

import org.junit.Assert;

public class ResultUtilityTest {

  @Test
  public void testSimpleResult1() throws Exception {
    List<ResultRowData> data = createList(createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L));
    data = ResultUtility.calculateRanks(data);
    Assert.assertEquals("Rank 1", 1L, data.get(0).getRank().longValue());
  }

  @Test
  public void testSimpleResult2() throws Exception {
    List<ResultRowData> data = createList(
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1500L, RaceStatusCodeType.OkCode.ID, 3L)
        );

    data = ResultUtility.calculateRanks(data);

    Assert.assertEquals("Rank 1", 1L, data.get(0).getRank().longValue());
    Assert.assertEquals("Rank 2", 2L, data.get(1).getRank().longValue());
  }

  @Test
  public void testSameTime1() throws Exception {
    List<ResultRowData> data = createList(
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 3L)
        );

    data = ResultUtility.calculateRanks(data);

    Assert.assertEquals("Rank 1", 1L, data.get(0).getRank().longValue());
    Assert.assertEquals("Rank 2", 1L, data.get(1).getRank().longValue());
  }

  @Test
  public void testSameTime2() throws Exception {
    List<ResultRowData> data = createList(
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 3L),
        createResultRowData(1001L, RaceStatusCodeType.OkCode.ID, 4L)
        );

    data = ResultUtility.calculateRanks(data);

    Assert.assertEquals("Rank 1", 1L, data.get(0).getRank().longValue());
    Assert.assertEquals("Rank 2", 1L, data.get(1).getRank().longValue());
    Assert.assertEquals("Rank 3", 3L, data.get(2).getRank().longValue());
  }

  @Test
  public void testDisq1() throws Exception {
    List<ResultRowData> data = createList(
        createResultRowData(1000L, RaceStatusCodeType.DisqualifiedCode.ID, 1L)
        );

    data = ResultUtility.calculateRanks(data);

    Assert.assertNull("Rank 1", data.get(0).getRank());
  }

  @Test
  public void testDisq2() throws Exception {
    List<ResultRowData> data = createList(
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1000L, RaceStatusCodeType.DisqualifiedCode.ID, 2L)
        );

    data = ResultUtility.calculateRanks(data);

    Assert.assertEquals("Rank 1", 1L, data.get(0).getRank().longValue());
    Assert.assertNull("Rank 2", data.get(1).getRank());
  }

  @Test
  public void testTeam1() throws Exception {
    List<ResultRowData> data = createList(
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L)
        );

    data = ResultUtility.calculateRanks(data);

    Assert.assertEquals("Rank 1", 1L, data.get(0).getRank().longValue());
    Assert.assertNull("Rank 2", data.get(1).getRank());
  }

  @Test
  public void testTeam2() throws Exception {
    List<ResultRowData> data = createList(
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1005L, RaceStatusCodeType.OkCode.ID, 2L)
        );

    data = ResultUtility.calculateRanks(data);

    Assert.assertEquals("Rank 1", 1L, data.get(0).getRank().longValue());
    Assert.assertNull("Rank 2", data.get(1).getRank());
    Assert.assertEquals("Rank 3", 2L, data.get(2).getRank().longValue());
  }

  @Test
  public void testTeam3SameTime() throws Exception {
    List<ResultRowData> data = createList(
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 2L)
        );

    data = ResultUtility.calculateRanks(data);

    Assert.assertEquals("Rank 1", 1L, data.get(0).getRank().longValue());
    Assert.assertNull("Rank 2", data.get(1).getRank());
    Assert.assertEquals("Rank 3", 1L, data.get(2).getRank().longValue());
  }

  @Test
  public void testTeam4SameTime() throws Exception {
    List<ResultRowData> data = createList(
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 2L),
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 2L),
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 3L)
        );

    data = ResultUtility.calculateRanks(data);

    Assert.assertEquals("Rank 1", 1L, data.get(0).getRank().longValue());
    Assert.assertNull("Rank 2", data.get(1).getRank());
    Assert.assertEquals("Rank 3", 1L, data.get(2).getRank().longValue());
    Assert.assertNull("Rank 4", data.get(3).getRank());
    Assert.assertEquals("Rank 5", 1L, data.get(4).getRank().longValue());
  }

  @Test
  public void testTeam5() throws Exception {
    List<ResultRowData> data = createList(
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1000L, RaceStatusCodeType.OkCode.ID, 1L),
        createResultRowData(1005L, RaceStatusCodeType.OkCode.ID, 2L),
        createResultRowData(1010L, RaceStatusCodeType.OkCode.ID, 3L)
        );

    data = ResultUtility.calculateRanks(data);

    Assert.assertEquals("Rank 1", 1L, data.get(0).getRank().longValue());
    Assert.assertNull("Rank 2", data.get(1).getRank());
    Assert.assertEquals("Rank 3", 2L, data.get(2).getRank().longValue());
    Assert.assertEquals("Rank 4", 3L, data.get(3).getRank().longValue());
  }

  private ResultRowData createResultRowData(Long time, Long statusUid, Long entryNr) {
    ResultRowData row = new ResultRowData();
    row.setLegTime(time);
    row.setRaceStatus(statusUid);
    row.setEntryNr(entryNr);
    return row;
  }

  private List<ResultRowData> createList(ResultRowData... datas) {
    List<ResultRowData> list = new ArrayList<>();
    for (ResultRowData data : datas) {
      list.add(data);
    }
    return list;
  }

}
