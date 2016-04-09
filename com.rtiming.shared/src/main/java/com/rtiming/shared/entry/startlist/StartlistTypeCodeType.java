package com.rtiming.shared.entry.startlist;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class StartlistTypeCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1950L;

  public StartlistTypeCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("StartlistType");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public class DrawingCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1951L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Drawing");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public class MassStartCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1952L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("MassStart");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public class SingleEventCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1953L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("SingleEvent");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }

  }

  @Order(40.0)
  public class MultidayEventCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1954L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("MultidayEvent");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }

  }

  @Order(50.0)
  public class StartlistNoneCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1955L;

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }

    @Override
    protected String getConfiguredText() {
      return Texts.get("StartlistNone");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
