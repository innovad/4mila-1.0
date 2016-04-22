package com.rtiming.client.ranking;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.ranking.AbstractFormulaCode;
import com.rtiming.shared.ranking.AbstractFormulaCode.RankingType;
import com.rtiming.shared.ranking.RankingFormData;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

public class FormulaScriptDefaultTest {

  @Test
  public void testEventFormulas() throws Exception {
    int testCount = 0;
    List<? extends ICode<?>> codes = new RankingFormulaTypeCodeType().getCodes();
    for (ICode<?> code : codes) {
      if (code instanceof AbstractFormulaCode) {
        AbstractFormulaCode fCode = (AbstractFormulaCode) code;
        if (RankingType.EVENT.equals(fCode.getRankingType())) {
          FormulaScript script = new FormulaScript(fCode.getFormula());
          List<EventRanking> list = new ArrayList<EventRanking>();
          script.putBinding("races", list);
          script.eval();
          testCount++;
        }
      }
    }
    Assert.assertTrue("Codes tested", testCount > 0);
  }

  @Test
  public void testSummaryFormulas() throws Exception {
    int testCount = 0;
    RankingFormData formData = new RankingFormData();
    List<? extends ICode<?>> codes = new RankingFormulaTypeCodeType().getCodes();
    for (ICode<?> code : codes) {
      if (code instanceof AbstractFormulaCode) {
        AbstractFormulaCode fCode = (AbstractFormulaCode) code;
        if (RankingType.SUMMARY.equals(fCode.getRankingType())) {
          FormulaScript script = new FormulaScript(fCode.getFormula());
          List<SummaryRanking> list = new ArrayList<SummaryRanking>();
          list.add(new SummaryRanking(new Long[]{1L}, 1L, 1L, formData.getRankingBox()));
          script.putBinding("races", list);
          script.putBinding("eventCount", 1);
          script.eval();
          testCount++;
        }
      }
    }
    Assert.assertTrue("Codes tested", testCount > 0);
  }

}
