package com.rtiming.shared.runner;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class SexCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1550L;

  public SexCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("Sex");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public class ManCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1551L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Man");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public class WomanCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1552L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Woman");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
