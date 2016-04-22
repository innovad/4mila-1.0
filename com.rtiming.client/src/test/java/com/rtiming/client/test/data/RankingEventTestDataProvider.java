package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ranking.RankingEventForm;
import com.rtiming.shared.dao.RtRankingEventKey;

public class RankingEventTestDataProvider extends AbstractTestDataProvider<RankingEventForm> {

  private Long rankingNr;
  private Long eventNr;

  public RankingEventTestDataProvider(Long rankingNr, Long eventNr) throws ProcessingException {
    this.rankingNr = rankingNr;
    this.eventNr = eventNr;
    callInitializer();
  }

  @Override
  protected RankingEventForm createForm() throws ProcessingException {
    RankingEventForm form = new RankingEventForm();
    RtRankingEventKey key = new RtRankingEventKey();
    key.setRankingNr(rankingNr);
    key.setEventNr(eventNr);
    key.setClientNr(ClientSession.get().getSessionClientNr());
    form.setKey(key);
    form.startNew();
    form.getEventField().setValue(eventNr);
    form.doOk();
    return form;
  }

  @Override
  public void remove() throws ProcessingException {
    // nop, handled by RankingTestDataProvider
  }

}
