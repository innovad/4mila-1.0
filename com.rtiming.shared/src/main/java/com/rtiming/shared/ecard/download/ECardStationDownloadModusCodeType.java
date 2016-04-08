package com.rtiming.shared.ecard.download;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class ECardStationDownloadModusCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1650L;

  public ECardStationDownloadModusCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("DownloadModus");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class EntryCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1651L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Entry");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public static class DownloadSplitTimesCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1652L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("DownloadSplitTimes");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public static class DownloadSplitTimesAndRegisterCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1653L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("DownloadSplitTimesAndRegister");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(40.0)
  public static class CourseAndClassFromECardCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1654L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("CreateCourseAndClassFromECard");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(35.0)
  public static class DownloadSplitTimesAndAssignRaceCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 1655L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("DownloadSplitTimesAndAssignRace");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
