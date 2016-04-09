package com.rtiming.shared.event.course;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class ControlStatusCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1400L;

  public ControlStatusCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("ControlStatus");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public class InitialStatusCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1401L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("InitialStatus");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getExtKey() {
      return "IN";
    }

  }

  @Order(20.0)
  public class OkCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1402L;

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("Ok");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getExtKey() {
      return "OK";
    }

  }

  @Order(30.0)
  public class MissingCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1403L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Missing");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getExtKey() {
      return "MP";
    }

  }

  @Order(40.0)
  public class AdditionalCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1404L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Additional");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getExtKey() {
      return "AP";
    }

  }

  @Order(50.0)
  public class WrongCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1405L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("WrongControl");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getExtKey() {
      return "WP";
    }

  }

}
