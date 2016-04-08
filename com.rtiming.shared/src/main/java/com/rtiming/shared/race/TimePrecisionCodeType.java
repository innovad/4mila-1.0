package com.rtiming.shared.race;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class TimePrecisionCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1500L;

  public TimePrecisionCodeType() throws ProcessingException {
  super();
  }
  @Override
  protected String getConfiguredText() {
    return Texts.get("TimePrecision");
  }
  @Override
  public Long getId() {
  return ID;
  }
  @Order(10.0)
  public class Precision1sCode extends AbstractCode<Long>{

    private static final long serialVersionUID = 1L;
    public static final long ID = 1501L;
    @Override
    protected String getConfiguredText() {
      return Texts.get("Precision1s");
    }
    @Override
    public Long getId() {
    return ID;
    }
  }
  @Order(20.0)
  public class Precision10sCode extends AbstractCode<Long>{

    private static final long serialVersionUID = 1L;
    public static final long ID = 1502L;
    @Override
    protected String getConfiguredText() {
      return Texts.get("Precision10s");
    }
    @Override
    public Long getId() {
    return ID;
    }
  }
  @Order(30.0)
  public class Precision100sCode extends AbstractCode<Long>{

    private static final long serialVersionUID = 1L;
    public static final long ID = 1503L;
    @Override
    protected String getConfiguredText() {
      return Texts.get("Precision100s");
    }
    @Override
    public Long getId() {
    return ID;
    }
  }
  @Order(40.0)
  public class Precision1000sCode extends AbstractCode<Long>{

    private static final long serialVersionUID = 1L;
    public static final long ID = 1504L;
    @Override
    protected String getConfiguredText() {
      return Texts.get("Precision1000s");
    }
    @Override
    public Long getId() {
    return ID;
    }
  }
}
