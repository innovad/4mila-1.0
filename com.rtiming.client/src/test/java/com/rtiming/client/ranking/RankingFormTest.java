package com.rtiming.client.ranking;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ranking.AbstractRankingBox.FormulaBox.FormulaTypeField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.ranking.IRankingProcessService;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RankingFormTest extends AbstractFormTest<RankingForm> {

  @Override
  protected RankingForm getStartedForm() throws ProcessingException {
    RankingForm form = new RankingForm();
    form.startNew();
    return form;
  }

  @Override
  protected RankingForm getModifyForm() throws ProcessingException {
    RankingForm form = new RankingForm();
    form.setKey(getForm().getKey());
    form.startModify();
    return form;
  }

  @Override
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(FormulaTypeField.class, RankingFormulaTypeCodeType.CustomFormulaCode.ID));
    return list;
  }

  @Override
  public void cleanup() throws ProcessingException {
    BEANS.get(IRankingProcessService.class).delete(getForm().getKey());
  }

}
