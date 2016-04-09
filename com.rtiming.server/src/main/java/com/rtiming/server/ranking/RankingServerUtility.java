package com.rtiming.server.ranking;

import java.util.List;

import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;

import com.rtiming.shared.ranking.AbstractFormulaCode;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

public final class RankingServerUtility {

  private RankingServerUtility() {
  }

  public static long analyzeFormulaType(String formula) {
    long result = RankingFormulaTypeCodeType.CustomFormulaCode.ID;
    List<? extends ICode<Long>> codes = CODES.getCodeType(RankingFormulaTypeCodeType.class).getCodes();
    for (ICode code : codes) {
      if (code instanceof AbstractFormulaCode) {
        AbstractFormulaCode formulaCode = (AbstractFormulaCode) code;
        if (StringUtility.equalsIgnoreNewLines(formulaCode.getFormula(), formula)) {
          result = formulaCode.getId();
        }
      }
    }
    return result;
  }

}
