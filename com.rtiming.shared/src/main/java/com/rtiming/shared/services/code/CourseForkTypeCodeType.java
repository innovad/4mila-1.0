package com.rtiming.shared.services.code;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

public class CourseForkTypeCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final Long ID = 2550L;

  public CourseForkTypeCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("Fork");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class ButterflyCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2551L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Butterfly");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public static class ForkCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2552L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Fork");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
