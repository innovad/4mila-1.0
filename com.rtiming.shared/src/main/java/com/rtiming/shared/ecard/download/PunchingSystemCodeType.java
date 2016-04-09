package com.rtiming.shared.ecard.download;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class PunchingSystemCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1450L;

  public PunchingSystemCodeType() throws ProcessingException {
  super();
  }
  @Override
  protected String getConfiguredText() {
    return Texts.get("PunchingSystem");
  }
  @Override
  public Long getId() {
  return ID;
  }
  @Order(10.0)
  public class SportIdentCode extends AbstractCode<Long>{

    private static final long serialVersionUID = 1L;
    public static final long ID = 1451L;
    @Override
    protected String getConfiguredText() {
      return Texts.get("SportIdent");
    }
    @Override
    public Long getId() {
    return ID;
    }
  }
  @Order(20.0)
  public class PunchingSystemNoneCode extends AbstractCode<Long>{

    private static final long serialVersionUID = 1L;
    public static final long ID = 1452L;
    @Override
    protected String getConfiguredText() {
      return Texts.get("PunchingSystemNone");
    }
    @Override
    public Long getId() {
    return ID;
    }
  }
}
