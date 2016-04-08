package com.rtiming.shared.common;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class EntityCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1800L;

  public EntityCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("Entity");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public class RunnerCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1801L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Runner");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public class EntryCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1802L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Entry");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public class ClubCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1803L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Club");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(40.0)
  public class AdditionalInformationCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1804L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("AdditionalInformation");
    }

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(50.0)
  public class ClazzCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1805L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Class");
    }

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
