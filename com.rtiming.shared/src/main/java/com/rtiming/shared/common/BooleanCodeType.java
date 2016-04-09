package com.rtiming.shared.common;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class BooleanCodeType extends AbstractCodeType<Long, Boolean> {

  private static final long serialVersionUID = 1L;

  public BooleanCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("Boolean");
  }

  @Override
  public Long getId() {
    // TODO MIG
    return 1L;
  }

  @Order(10.0)
  public class YesCode extends AbstractCode<Boolean> {

    private static final long serialVersionUID = 1L;

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("Yes");
    }

    @Override
    public Boolean getId() {
      return Boolean.TRUE;
    }

  }

  @Order(20.0)
  public class NoCode extends AbstractCode<Boolean> {

    private static final long serialVersionUID = 1L;

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("No");
    }

    @Override
    public Boolean getId() {
      return Boolean.FALSE;
    }
  }

}
