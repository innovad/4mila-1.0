package com.rtiming.client.ranking;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ranking.AbstractRankingBox.FormulaBox.FormulaTypeField;
import com.rtiming.client.ranking.RankingEventForm.MainBox.EventField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.data.RankingTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.dao.RtRankingEventKey;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RankingEventFormTest extends AbstractFormTest<RankingEventForm> {

  private RankingTestDataProvider ranking;
  private EventTestDataProvider event;

  @Override
  protected RankingEventForm getStartedForm() throws ProcessingException {
    ranking = new RankingTestDataProvider();
    event = new EventTestDataProvider();
    RankingEventForm form = new RankingEventForm();
    RtRankingEventKey key = new RtRankingEventKey();
    key.setRankingNr(ranking.getRankingNr());
    key.setEventNr(event.getEventNr());
    key.setClientNr(ClientSession.get().getSessionClientNr());
    form.setKey(key);
    form.startNew();
    return form;
  }

  @Override
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(EventField.class, event.getEventNr()));
    list.add(new FieldValue(FormulaTypeField.class, RankingFormulaTypeCodeType.CustomFormulaCode.ID));
    return list;
  }

  @Override
  protected RankingEventForm getModifyForm() throws ProcessingException {
    RankingEventForm form = new RankingEventForm();
    form.setKey(getForm().getKey());
    form.getEventField().setValue(event.getEventNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    if (ranking != null) {
      ranking.remove();
    }
    if (event != null) {
      event.remove();
    }
  }

}
