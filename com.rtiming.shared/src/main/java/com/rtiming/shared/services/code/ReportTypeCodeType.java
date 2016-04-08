package com.rtiming.shared.services.code;

import java.util.List;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.dao.RtReportTemplateFile;

public class ReportTypeCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 2450;

  public ReportTypeCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("ReportType");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class DefaultCode extends AbstractReportTypeCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2454L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Default");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public ReportTemplateType getConfiguredReportTemplateType() {
      return ReportTemplateType.GENERIC_TABLE;
    }

    @Override
    public List<RtReportTemplateFile> getConfiguredDefaultTemplates() {
      return buildDefaultTemplateList(ID, "headerFooterTemplate.jrxml", "styles.jrtx");
    }

    @Override
    public boolean getConfiguredCompilationRequired() {
      return false;
    }

  }

  @Order(20.0)
  public static class StartListCode extends AbstractReportTypeCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2451L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("StartList");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public ReportTemplateType getConfiguredReportTemplateType() {
      return ReportTemplateType.CUSTOM_TEMPLATE;
    }

    @Override
    public List<RtReportTemplateFile> getConfiguredDefaultTemplates() {
      return buildDefaultTemplateList(ID, "startlists.jrxml", "startlistsTable.jrxml");
    }

    @Override
    public boolean getConfiguredCompilationRequired() {
      return true;
    }

  }

  @Order(30.0)
  public static class ResultsCode extends AbstractReportTypeCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2452L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Results");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public ReportTemplateType getConfiguredReportTemplateType() {
      return ReportTemplateType.CUSTOM_TEMPLATE;
    }

    @Override
    public List<RtReportTemplateFile> getConfiguredDefaultTemplates() {
      return buildDefaultTemplateList(ID, "results.jrxml", "resultsTable.jrxml");
    }

    @Override
    public boolean getConfiguredCompilationRequired() {
      return true;
    }

  }

  @Order(40.0)
  public static class SplitTimesCode extends AbstractReportTypeCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2453L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("SplitTimes");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public ReportTemplateType getConfiguredReportTemplateType() {
      return ReportTemplateType.CUSTOM_TEMPLATE;
    }

    @Override
    public List<RtReportTemplateFile> getConfiguredDefaultTemplates() {
      return buildDefaultTemplateList(ID, "splittimes.jrxml", "splittimesTable.jrxml");
    }

    @Override
    public boolean getConfiguredCompilationRequired() {
      return true;
    }

  }

  @Order(40.0)
  public static class RankingsCode extends AbstractReportTypeCode {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2455L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("Rankings");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public ReportTemplateType getConfiguredReportTemplateType() {
      return ReportTemplateType.CUSTOM_TEMPLATE;
    }

    @Override
    public List<RtReportTemplateFile> getConfiguredDefaultTemplates() {
      return buildDefaultTemplateList(ID, "rankings.jrxml", "rankingsTable.jrxml");
    }

    @Override
    public boolean getConfiguredCompilationRequired() {
      return true;
    }

  }

}
