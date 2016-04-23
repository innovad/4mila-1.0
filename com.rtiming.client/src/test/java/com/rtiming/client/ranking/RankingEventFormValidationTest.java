package com.rtiming.client.ranking;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ranking.RankingEventForm.MainBox.EventField;
import com.rtiming.client.ranking.RankingEventForm.MainBox.SortCodeField;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.data.RankingTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.dao.RtRankingEventKey;
import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.race.TimePrecisionCodeType;
import com.rtiming.shared.ranking.RankingFormatCodeType;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RankingEventFormValidationTest {

  private RankingTestDataProvider ranking;
  private EventTestDataProvider event;

  @Test
  public void testNewEventRanking() throws Exception {
    RankingEventForm form = new RankingEventForm();
    form.startNew();

    Assert.assertEquals("Default Format", RankingFormatCodeType.TimeCode.ID, form.getRankingBox().getFormatField().getValue());
    Assert.assertEquals("Default Time Precision", TimePrecisionCodeType.Precision1sCode.ID, form.getRankingBox().getTimePrecisionField().getValue().longValue());
    Assert.assertEquals("Default Decimal Places", 0L, form.getRankingBox().getDecimalPlacesField().getValue().longValue());
    Assert.assertEquals("Default Sorting", BibNoOrderCodeType.AscendingCode.ID, form.getRankingBox().getSortingField().getValue().longValue());
    Assert.assertEquals("Default Formula Type", RankingFormulaTypeCodeType.TimeCode.ID, form.getRankingBox().getFormulaTypeField().getValue());

    Assert.assertNotNull("Formula not empty", form.getRankingBox().getFormulaField().getValue());

    ScoutClientAssert.assertVisible(form.getRankingBox().getTimePrecisionField());
    ScoutClientAssert.assertInvisible(form.getRankingBox().getDecimalPlacesField());

    form.doClose();
  }

  @Test
  public void testFirstSortCode() throws Exception {
    ranking = new RankingTestDataProvider();
    RankingEventForm form = new RankingEventForm();
    form.startNew();
    Assert.assertEquals("1st Sortcode=1", 1, form.getSortCodeField().getValue().longValue());
    form.doClose();
  }

  @Test
  public void testSecondSortCode() throws Exception {
    ranking = new RankingTestDataProvider();
    event = new EventTestDataProvider();
    RankingEventForm form = new RankingEventForm();
    RtRankingEventKey key = new RtRankingEventKey();
    key.setRankingNr(ranking.getRankingNr());
    key.setEventNr(event.getEventNr());
    key.setClientNr(ClientSession.get().getSessionClientNr());
    form.setKey(key);
    form.startNew();
    Assert.assertEquals("1st Sortcode=1", 1, form.getSortCodeField().getValue().longValue());
    FieldValue sortCode = new FieldValue(SortCodeField.class, 44L);
    FieldValue eventField = new FieldValue(EventField.class, event.getEventNr());
    FormTestUtility.fillFormFields(form, sortCode, eventField);
    Assert.assertEquals("1st Sortcode=44", 44, form.getSortCodeField().getValue().longValue());
    form.doOk();

    form = new RankingEventForm();
    key = new RtRankingEventKey();
    key.setRankingNr(ranking.getRankingNr());
    key.setEventNr(event.getEventNr());
    key.setClientNr(ClientSession.get().getSessionClientNr());
    form.setKey(key);
    form.startNew();
    Assert.assertEquals("2nd Sortcode=45", 45, form.getSortCodeField().getValue().longValue());
    form.doClose();
  }

  @After
  public void after() throws ProcessingException {
    if (ranking != null) {
      ranking.remove();
    }
    if (event != null) {
      event.remove();
    }
  }

}
