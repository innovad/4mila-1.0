package com.rtiming.client.common.report.jrxml;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.report.DataSourceUtility;
import com.rtiming.client.result.ResultsTablePage;
import com.rtiming.client.result.ResultsTablePage.Table;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.shared.common.report.template.IReportParameters;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.services.code.ReportTypeCodeType;

public class ResultReport extends AbstractDefaultRunnerListReport {

  public ResultReport(Long eventNr, List<Long> classUids, List<Long> courseNrs, List<Long> clubNrs) {
    super(eventNr, classUids, courseNrs, clubNrs);
  }

  @Override
  protected SubreportDataSource getResultData(Long resultEventNr, Long classUid, Long courseNr, Long clubNr, Map<String, Object> parameters) throws ProcessingException {
    // set header data
    RunnerListHeaderInfo info = new RunnerListHeaderInfo(resultEventNr, classUid, courseNr, clubNr);
    String courseInfo = info.getCourseInfo();

    ResultsTablePage results = new ResultsTablePage(ClientSession.get().getSessionClientNr(), classUid, courseNr, clubNr);
    results.nodeAddedNotify();
    results.getTable().resetColumns();
    ((SingleEventSearchForm) results.getSearchFormInternal()).getEventField().setValue(resultEventNr);
    ((SingleEventSearchForm) results.getSearchFormInternal()).resetSearchFilter();
    results.loadChildren();

    Table data = results.getTable();

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
    return ReportTypeCodeType.ResultsCode.ID;
  }

}
