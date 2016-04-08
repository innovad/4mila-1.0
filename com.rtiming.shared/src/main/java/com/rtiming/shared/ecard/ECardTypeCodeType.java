package com.rtiming.shared.ecard;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class ECardTypeCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1000L;

  public ECardTypeCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("ECardType");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(20.0)
  public static class SICard5Code extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1001L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("SICard5");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public static class SICard6Code extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1002L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("SICard6");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(40.0)
  public static class SICard6StarCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1003L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("SICard6Star");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(50.0)
  public static class SICard8Code extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1004L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("SICard8");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(60.0)
  public static class SICard9Code extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1005L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("SICard9");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(70.0)
  public static class SICard10Code extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1008L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("SICard10");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(80.0)
  public static class SICard11Code extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1009L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("SICard11");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(100.0)
  public static class SICardSIAC1Code extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1010L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("SICardSIAC1");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(120.0)
  public static class SICardPCardCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1006L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("SICardPCard");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(130.0)
  public static class SICardTCardCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1007L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("SICardTCard");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

}
