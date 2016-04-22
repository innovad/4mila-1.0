package com.rtiming.client.ranking;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.ranking.RankingFormData;

public class EventRankingTest {

  @Test
  public void testBestEvent() throws Exception {
    RankingFormData formData = createRankingFormData();
    SummaryRanking summary = new SummaryRanking(new Long[]{1L, 2L, 3L}, 99L, 99L, formData.getRankingBox());

    EventRanking ranking1 = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    ranking1.setResult(10d);
    summary.addEventRanking(1L, ranking1);

    EventRanking ranking2 = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    ranking2.setResult(15d);
    summary.addEventRanking(2L, ranking2);

    Double sum = summary.getEventResultsSum(1);
    Assert.assertEquals("Event Result Sum", 10d, sum, 0.01d);
    Assert.assertTrue("Best event", ranking1.isBestRanking());
    Assert.assertFalse("Best event", ranking2.isBestRanking());
    Assert.assertFalse("Best event", summary.getEventRankings().get(2).isBestRanking());
  }

  @Test
  public void testControlCount1() throws Exception {
    EventRanking ranking = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, createRankingFormData().getRankingBox());
    int count = ranking.getControlCount(null);

    Assert.assertEquals("Amount", 0, count);
  }

  @Test
  public void testControlCount2() throws Exception {
    EventRanking ranking = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, createRankingFormData().getRankingBox());
    List<Control> controls = new ArrayList<Control>();
    controls.add(new Control()); // no type
    ranking.setControls(controls);
    int count = ranking.getControlCount(null);

    Assert.assertEquals("Amount", 0, count);
  }

  @Test
  public void testControlCount3() throws Exception {
    EventRanking ranking = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, createRankingFormData().getRankingBox());
    List<Control> controls = new ArrayList<Control>();
    Control control = new Control();
    control.setTypeUid(ControlTypeCodeType.ControlCode.ID);
    controls.add(control);
    ranking.setControls(controls);
    int count = ranking.getControlCount(null);

    Assert.assertEquals("Amount", 1, count);
  }

  @Test
  public void testControlCount4() throws Exception {
    EventRanking ranking = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, createRankingFormData().getRankingBox());
    List<Control> controls = new ArrayList<Control>();
    Control control = new Control();
    control.setStatusUid(ControlStatusCodeType.MissingCode.ID);
    control.setTypeUid(ControlTypeCodeType.ControlCode.ID);
    controls.add(control);
    ranking.setControls(controls);
    int count = ranking.getControlCount(new ControlStatusCodeType().getCode(ControlStatusCodeType.MissingCode.ID).getExtKey());

    Assert.assertEquals("Amount", 1, count);
  }

  @Test
  public void testControlCount5() throws Exception {
    EventRanking ranking = new EventRanking(99L, 99L, 77L, RaceStatusCodeType.OkCode.ID, createRankingFormData().getRankingBox());
    List<Control> controls = new ArrayList<Control>();

    Control control1 = new Control();
    control1.setStatusUid(ControlStatusCodeType.MissingCode.ID);
    control1.setTypeUid(ControlTypeCodeType.ControlCode.ID);
    controls.add(control1);

    Control control2 = new Control();
    control2.setStatusUid(ControlStatusCodeType.OkCode.ID);
    control2.setTypeUid(ControlTypeCodeType.ControlCode.ID);
    controls.add(control2);

    ranking.setControls(controls);

    int count = ranking.getControlCount(new ControlStatusCodeType().getCode(ControlStatusCodeType.MissingCode.ID).getExtKey());
    Assert.assertEquals("Amount", 1, count);

    count = ranking.getControlCount(new ControlStatusCodeType().getCode(ControlStatusCodeType.OkCode.ID).getExtKey());
    Assert.assertEquals("Amount", 1, count);

    count = ranking.getControlCount(new ControlStatusCodeType().getCode(ControlStatusCodeType.AdditionalCode.ID).getExtKey());
    Assert.assertEquals("Amount", 0, count);

    count = ranking.getControlCount(null);
    Assert.assertEquals("Amount", 2, count);
  }

  private RankingFormData createRankingFormData() {
    RankingFormData formData = new RankingFormData();
    formData.getRankingBox().getDecimalPlaces().setValue(0L);
    formData.getRankingBox().getSorting().setValue(BibNoOrderCodeType.AscendingCode.ID);
    return formData;
  }

}
