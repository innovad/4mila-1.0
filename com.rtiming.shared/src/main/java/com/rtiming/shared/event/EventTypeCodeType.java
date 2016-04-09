package com.rtiming.shared.event;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class EventTypeCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 2200L;

  public EventTypeCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("EventType");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public class InternationalCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 2201L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("International");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public class NationalCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 2202L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("National");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public class RegionalCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 2203L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Regional");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(40.0)
  public class LocalCode extends AbstractCode<Long>{

    private static final long serialVersionUID = 1L;
    public static final long ID = 2204L;
    @Override
    protected String getConfiguredText() {
      return Texts.get("Local");
    }
    @Override
    public Long getId() {
    return ID;
    }
  }

  @Order(50.0)
  public class ClubCode extends AbstractCode<Long>{

    private static final long serialVersionUID = 1L;
    public static final long ID = 2205L;
    @Override
    protected String getConfiguredText() {
      return Texts.get("Club");
    }
    @Override
    public Long getId() {
    return ID;
    }
  }
}
