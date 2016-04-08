package com.rtiming.shared.ranking;

import org.eclipse.scout.commons.annotations.ConfigProperty;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;

public abstract class AbstractFormulaCode extends AbstractCode<Long> {

  public enum RankingType {
    EVENT, SUMMARY
  }

  private static final long serialVersionUID = 1L;

  public abstract RankingType getRankingType();

  public abstract String getFormula();

  public abstract Long getFormatUid();

  @ConfigProperty(ConfigProperty.LONG)
  public abstract Long getDecimalPlaces();

  public abstract Long getSortingUid();

}
