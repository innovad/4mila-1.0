package com.rtiming.shared.entry.startlist;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class BibNoOrderCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 2000L;

  public BibNoOrderCodeType() throws ProcessingException {
  super();
  }
  @Override
  protected String getConfiguredText() {
    return Texts.get("BibNoOrder");
  }
  @Override
  public Long getId() {
  return ID;
  }
  @Order(10.0)
  public class AscendingCode extends AbstractCode<Long>{

    private static final long serialVersionUID = 1L;
    public static final long ID = 2001L;
    @Override
    protected String getConfiguredText() {
      return Texts.get("Ascending");
    }
    @Override
    public Long getId() {
    return ID;
    }
  }
  @Order(20.0)
  public class DescendingCode extends AbstractCode<Long>{

    private static final long serialVersionUID = 1L;
    public static final long ID = 2002L;
    @Override
    protected String getConfiguredText() {
      return Texts.get("Descending");
    }
    @Override
    public Long getId() {
    return ID;
    }
  }
}
