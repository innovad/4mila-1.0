package com.rtiming.client.common.report.jrxml;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.client.common.report.DataSourceUtility;
import com.rtiming.client.ranking.RankingEventResultsTablePage;
import com.rtiming.client.ranking.RankingSummaryResultsTablePage;
import com.rtiming.shared.common.report.template.IReportParameters;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.ranking.AbstractFormulaCode.RankingType;
import com.rtiming.shared.services.code.ReportTypeCodeType;

public class RankingReport extends AbstractRunnerListReport {

  private final Long eventNr;
  private final List<Long> classUids;
  private final Long rankingNr;
  private final RankingType type;

  public RankingReport(Long eventNr, Long rankingNr, RankingType type, List<Long> classUids) {
    super();
    this.eventNr = eventNr;
    this.classUids = classUids;
    this.rankingNr = rankingNr;
    this.type = type;
  }

  public void openReport() throws ProcessingException {
    ReportParam param = new ReportParam();
    param.setEventNr(eventNr);
    param.setClassUids(classUids);
    super.openReport(param);
  }

  public void printReport() throws ProcessingException {
    ReportParam param = new ReportParam();
    param.setEventNr(eventNr);
    param.setClassUids(classUids);
    super.printReport(param);
  }

  @Override
  protected SubreportDataSource getResultData(Long resultEventNr, Long classUid, Long courseNr, Long clubNr, Map<String, Object> parameters) throws ProcessingException {
    // set header data
    RunnerListHeaderInfo info = new RunnerListHeaderInfo(resultEventNr, classUid, courseNr, clubNr);
    String courseInfo = info.getCourseInfo();

    IPageWithTable<?> results = null;
    if (RankingType.EVENT.equals(type)) {
      results = new RankingEventResultsTablePage(rankingNr, resultEventNr, classUid);
    }
    else {
      results = new RankingSummaryResultsTablePage(rankingNr, resultEventNr, classUid);
    }

    results.nodeAddedNotify();
    results.getTable().resetColumns();
    results.loadChildren();

    ITable data = results.getTable();

    SubreportDataSource subreport = new SubreportDataSource();

    parameters.put(IReportParameters.TABLE_RUNNER_FINISHED, NumberUtility.format(data.getRowCount()));
    Long runnerStarted = BEANS.get(IEventProcessService.class).getRunnerStartedCount(resultEventNr, classUid, info.getCourseNr());
    parameters.put(IReportParameters.TABLE_RUNNER_TOTAL, NumberUtility.format(runnerStarted));
    parameters.put(IReportParameters.TABLE_COURSE_INFO, courseInfo);

    // add parameters and column headers from table
    parameters.putAll(DataSourceUtility.createColumnHeadersParametersFromTable(results.getTable()));
    Collection<Map<String, ?>> runnerRows = DataSourceUtility.createCollectionFromTable(results.getTable(), false, true);

    subreport.putRunnersData(runnerRows);
    subreport.putHeaderData(parameters);

    if (runnerRows.size() > 0) {
      return subreport;
    }
    return null;
  }

  @Override
  protected Long getReportTypeUid() {
    return ReportTypeCodeType.RankingsCode.ID;
  }

}
