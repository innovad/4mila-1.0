package com.rtiming.client.ranking;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.shared.race.TimePrecisionCodeType;
import com.rtiming.shared.ranking.RankingFormatCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class RankingBoxTest {

  @Test
  public void testDecimalPlaces1() throws Exception {
    RankingEventForm form = new RankingEventForm();
    form.startNew();
    AbstractRankingBox rankingBox = form.getRankingBox();
    testDecimalPlaces(rankingBox);
    form.doClose();
  }

  @Test
  public void testDecimalPlaces2() throws Exception {
    RankingForm form = new RankingForm();
    form.startNew();
    AbstractRankingBox rankingBox = form.getRankingBox();
    testDecimalPlaces(rankingBox);
    form.doClose();
  }

  private void testDecimalPlaces(AbstractRankingBox rankingBox) {
    rankingBox.getDecimalPlacesField().setValue(2L);
    Assert.assertEquals("Time Precision Updated", TimePrecisionCodeType.Precision100sCode.ID, rankingBox.getTimePrecisionField().getValue().longValue());

    rankingBox.getDecimalPlacesField().setValue(3L);
    Assert.assertEquals("Time Precision Updated", TimePrecisionCodeType.Precision1000sCode.ID, rankingBox.getTimePrecisionField().getValue().longValue());

    rankingBox.getDecimalPlacesField().setValue(4L);
    Assert.assertEquals("Time Precision Updated", TimePrecisionCodeType.Precision1000sCode.ID, rankingBox.getTimePrecisionField().getValue().longValue());

    rankingBox.getDecimalPlacesField().setValue(1L);
    Assert.assertEquals("Time Precision Updated", TimePrecisionCodeType.Precision10sCode.ID, rankingBox.getTimePrecisionField().getValue().longValue());

    rankingBox.getDecimalPlacesField().setValue(0L);
    Assert.assertEquals("Time Precision Updated", TimePrecisionCodeType.Precision1sCode.ID, rankingBox.getTimePrecisionField().getValue().longValue());

    rankingBox.getDecimalPlacesField().setValue(5L);
    Assert.assertEquals("Time Precision Updated", TimePrecisionCodeType.Precision1000sCode.ID, rankingBox.getTimePrecisionField().getValue().longValue());
  }

  @Test
  public void testTimePrecision1() throws Exception {
    RankingEventForm form = new RankingEventForm();
    form.startNew();
    AbstractRankingBox rankingBox = form.getRankingBox();
    testTimePrecision(rankingBox);
    form.doClose();
  }

  @Test
  public void testTimePrecision2() throws Exception {
    RankingForm form = new RankingForm();
    form.startNew();
    AbstractRankingBox rankingBox = form.getRankingBox();
    testTimePrecision(rankingBox);
    form.doClose();
  }

  private void testTimePrecision(AbstractRankingBox rankingBox) {
    rankingBox.getTimePrecisionField().setValue(TimePrecisionCodeType.Precision1000sCode.ID);
    Assert.assertEquals("Decimal Places Updated", 3, rankingBox.getDecimalPlacesField().getValue().longValue());

    rankingBox.getTimePrecisionField().setValue(TimePrecisionCodeType.Precision100sCode.ID);
    Assert.assertEquals("Decimal Places Updated", 2, rankingBox.getDecimalPlacesField().getValue().longValue());

    rankingBox.getTimePrecisionField().setValue(TimePrecisionCodeType.Precision10sCode.ID);
    Assert.assertEquals("Decimal Places Updated", 1, rankingBox.getDecimalPlacesField().getValue().longValue());

    rankingBox.getTimePrecisionField().setValue(TimePrecisionCodeType.Precision1sCode.ID);
    Assert.assertEquals("Decimal Places Updated", 0, rankingBox.getDecimalPlacesField().getValue().longValue());
  }

  @Test
  public void testFormat1() throws Exception {
    RankingEventForm form = new RankingEventForm();
    form.startNew();
    AbstractRankingBox rankingBox = form.getRankingBox();
    testFormat(rankingBox);
    form.doClose();
  }

  @Test
  public void testFormat2() throws Exception {
    RankingForm form = new RankingForm();
    form.startNew();
    AbstractRankingBox rankingBox = form.getRankingBox();
    testFormat(rankingBox);
    form.doClose();
  }

  private void testFormat(AbstractRankingBox rankingBox) {
    rankingBox.getFormatField().setValue(RankingFormatCodeType.TimeCode.ID);
    ScoutClientAssert.assertVisible(rankingBox.getTimePrecisionField());
    ScoutClientAssert.assertInvisible(rankingBox.getDecimalPlacesField());

    rankingBox.getFormatField().setValue(RankingFormatCodeType.PointsCode.ID);
    ScoutClientAssert.assertInvisible(rankingBox.getTimePrecisionField());
    ScoutClientAssert.assertVisible(rankingBox.getDecimalPlacesField());
  }

}
