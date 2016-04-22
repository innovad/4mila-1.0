package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.ranking.RankingForm;
import com.rtiming.shared.ranking.IRankingProcessService;

public class RankingTestDataProvider extends AbstractTestDataProvider<RankingForm> {

  public RankingTestDataProvider() throws ProcessingException {
    callInitializer();
  }

  @Override
  protected RankingForm createForm() throws ProcessingException {
    RankingForm form = new RankingForm();
    form.startNew();
    form.getNameField().setValue("TestABC");
    form.doOk();
    return form;
  }

  @Override
  public void remove() throws ProcessingException {
    BEANS.get(IRankingProcessService.class).delete(getForm().getKey());
  }

  public Long getRankingNr() throws ProcessingException {
    return getForm().getKey().getId();
  }

}
