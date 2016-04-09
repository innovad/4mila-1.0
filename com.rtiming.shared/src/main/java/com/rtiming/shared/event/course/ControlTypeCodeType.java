package com.rtiming.shared.event.course;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class ControlTypeCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1350L;

  public ControlTypeCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("ControlType");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public class ControlCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1351L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Control");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public class StartCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1352L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Start");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public class FinishCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1353L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Finish");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
