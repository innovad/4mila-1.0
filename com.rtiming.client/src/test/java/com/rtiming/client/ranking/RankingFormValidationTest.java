package com.rtiming.client.ranking;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.race.TimePrecisionCodeType;
import com.rtiming.shared.ranking.RankingFormatCodeType;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RankingFormValidationTest {

  @Test
  public void testNewRanking() throws Exception {
    RankingForm form = new RankingForm();
    form.startNew();

    Assert.assertEquals("Default Format", RankingFormatCodeType.TimeCode.ID, form.getRankingBox().getFormatField().getValue());
    Assert.assertEquals("Default Time Precision", TimePrecisionCodeType.Precision1sCode.ID, form.getRankingBox().getTimePrecisionField().getValue().longValue());
    Assert.assertEquals("Default Decimal Places", 0L, form.getRankingBox().getDecimalPlacesField().getValue().longValue());
    Assert.assertEquals("Default Sorting", BibNoOrderCodeType.AscendingCode.ID, form.getRankingBox().getSortingField().getValue().longValue());
    Assert.assertEquals("Default Formula Type", RankingFormulaTypeCodeType.SumTimeCode.ID, form.getRankingBox().getFormulaTypeField().getValue());

    Assert.assertNotNull("Formula not empty", form.getRankingBox().getFormulaField().getValue());

    ScoutClientAssert.assertVisible(form.getRankingBox().getTimePrecisionField());
    ScoutClientAssert.assertInvisible(form.getRankingBox().getDecimalPlacesField());

    form.doClose();
  }

}
