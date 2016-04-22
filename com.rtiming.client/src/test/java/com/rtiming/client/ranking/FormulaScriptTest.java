package com.rtiming.client.ranking;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.ranking.RankingFormData;

public class FormulaScriptTest {

  @Test(expected = VetoException.class)
  public void testScriptCompilation() throws Exception {
    new FormulaScript("bla; - 12341234 sfahkj");
  }

  @Test(expected = VetoException.class)
  public void testNullFormula() throws Exception {
    new FormulaScript(null);
  }

  @Test(expected = VetoException.class)
  public void testEmptyFormula() throws Exception {
    new FormulaScript("");
  }

  @Test(expected = VetoException.class)
  public void testSpacesFormula() throws Exception {
    new FormulaScript("      ");
  }

  @Test
  public void testOkFormula() throws Exception {
    FormulaScript formula = new FormulaScript("result=a*2");
    formula.putBinding("a", 4);
    formula.eval();
    Double value = NumberUtility.parseDouble("" + formula.getBinding("result"));
    Assert.assertEquals("JavaScript Result", 8d, value, 0.01);
  }

  @Test(expected = VetoException.class)
  public void testScriptEvaluationException() throws Exception {
    FormulaScript formula = new FormulaScript("result=fm*2");
    formula.eval();
  }

  @Test
  public void testNullResultFormula() throws Exception {
    FormulaScript formula = new FormulaScript("abc=a*2");
    formula.putBinding("a", 4);
    formula.eval();
    Object result = formula.getBinding("abc");
    Assert.assertEquals("JavaScript Result", 8d, NumberUtility.parseDouble("" + result));
  }

  @Test
  public void testRankingFormulaResult() throws Exception {
    RankingFormData formData = new RankingFormData();
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    FormulaScript formula = new FormulaScript("race.result=8");
    formula.putBinding("race", ranking);
    formula.eval();
    Assert.assertEquals("JavaScript Result", 8d, ranking.getResult(), 0.01);
  }

  @Test
  public void testRankingFormulaStatus() throws Exception {
    RankingFormData formData = new RankingFormData();
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    FormulaScript formula = new FormulaScript("race.setStatus('MP')");
    formula.putBinding("race", ranking);
    formula.eval();
    Assert.assertEquals("JavaScript Result", RaceStatusCodeType.MissingPunchCode.ID, ranking.getStatusUid().longValue());
  }

  @Test
  public void testRankingFormulaControl1() throws Exception {
    RankingFormData formData = new RankingFormData();
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    FormulaScript formula = new FormulaScript("race.result=race.getControlCount('OK');");
    formula.putBinding("race", ranking);
    formula.eval();
    Assert.assertEquals("JavaScript Result", 0, ranking.getResult(), 0.01);
  }

  @Test
  public void testRankingFormulaControl2() throws Exception {
    RankingFormData formData = new RankingFormData();
    EventRanking ranking = new EventRanking(1L, 1L, 1000L, RaceStatusCodeType.OkCode.ID, formData.getRankingBox());
    List<Control> controls = new ArrayList<Control>();
    Control control = new Control();
    control.setStatusUid(ControlStatusCodeType.OkCode.ID);
    controls.add(control);
    ranking.setControls(controls);
    FormulaScript formula = new FormulaScript("race.result=race.getControlCount('OK');");
    formula.putBinding("race", ranking);
    formula.eval();
    Assert.assertEquals("JavaScript Result", 0, ranking.getResult(), 0.01);
  }

}
