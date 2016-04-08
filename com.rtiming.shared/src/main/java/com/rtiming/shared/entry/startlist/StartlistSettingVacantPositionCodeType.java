package com.rtiming.shared.entry.startlist;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

public class StartlistSettingVacantPositionCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final Long ID = 2400L;

  public StartlistSettingVacantPositionCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("Position");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class EarlyStartCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2401L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("EarlyStart");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public static class DrawingCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2402L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Drawing");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public static class LateStartCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2403L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("LateStart");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
