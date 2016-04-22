package com.rtiming.server.ranking;

import static org.junit.Assert.assertEquals;

import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.ranking.AbstractFormulaCode;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

/**
 * @author amo
 */
@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class RankingServerUtilityTest {

  @Test
  public void testAnalyzeFormulaType1() throws Exception {
    long result = RankingServerUtility.analyzeFormulaType(null);
    assertEquals("Formula Type", result, RankingFormulaTypeCodeType.CustomFormulaCode.ID.longValue());
  }

  @Test
  public void testAnalyzeFormulaType2() throws Exception {
    doTest(RankingFormulaTypeCodeType.PercentFormulaCode.ID);
  }

  @Test
  public void testAnalyzeFormulaType3() throws Exception {
    doTest(RankingFormulaTypeCodeType.RogainingFormulaCode.ID);
  }

  @Test
  public void testAnalyzeFormulaType4() throws Exception {
    doTest(RankingFormulaTypeCodeType.TimeCode.ID);
  }

  private void doTest(Long codeTypeId) {
    AbstractFormulaCode code = (AbstractFormulaCode) CODES.getCodeType(RankingFormulaTypeCodeType.class).getCode(codeTypeId);
    long result = RankingServerUtility.analyzeFormulaType(code.getFormula());
    assertEquals("Formula Type", result, codeTypeId.longValue());
  }

}
