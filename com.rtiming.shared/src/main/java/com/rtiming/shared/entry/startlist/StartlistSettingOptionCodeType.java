package com.rtiming.shared.entry.startlist;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

public class StartlistSettingOptionCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final Long ID = 2350L;

  public StartlistSettingOptionCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("StartlistOption");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class SplitRegistrationsCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2351L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("SplitRegistrations");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public static class GroupRegistrationsCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2352L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("GroupRegistrations");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public static class SeparateNationsCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2353L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("SeparateNations");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(40.0)
  public static class SeparateClubsCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2354L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("SeparateClubs");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(50.0)
  public static class AllowStarttimeWishesCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2355L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("AllowStarttimeWishes");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
