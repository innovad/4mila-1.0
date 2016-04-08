package com.rtiming.shared.ranking;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

public class RankingFormatCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final Long ID = 2250L;

  public RankingFormatCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("Format");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class TimeCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2251L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Time");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public static class PointsCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2252L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Points");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
