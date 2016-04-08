package com.rtiming.shared.race;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class RaceStatusCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1600L;

  public RaceStatusCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("RaceStatus");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class OkCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1601L;

    @Override
    protected String getConfiguredExtKey() {
      return "OK";
    }

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("Ok");
    }

    @Override
    public Long getId() {
      return ID;
    }

  }

  @Order(20.0)
  public static class DidNotFinishCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1602L;

    @Override
    protected String getConfiguredExtKey() {
      return "DNF";
    }

    @Override
    protected String getConfiguredText() {
      return Texts.get("DidNotFinish");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public static class DidNotStartCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1603L;

    @Override
    protected String getConfiguredExtKey() {
      return "DNS";
    }

    @Override
    protected String getConfiguredText() {
      return Texts.get("DidNotStart");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(35.0)
  public static class NoStartTimeCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1606L;

    @Override
    protected String getConfiguredExtKey() {
      return "NST";
    }

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("NoStartTime");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(40.0)
  public static class MissingPunchCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1604L;

    @Override
    protected String getConfiguredExtKey() {
      return "MP";
    }

    @Override
    protected String getConfiguredText() {
      return Texts.get("MissingPunch");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(50.0)
  public static class DisqualifiedCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1605L;

    @Override
    protected String getConfiguredExtKey() {
      return "DISQ";
    }

    @Override
    protected String getConfiguredText() {
      return Texts.get("Disqualified");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

}
