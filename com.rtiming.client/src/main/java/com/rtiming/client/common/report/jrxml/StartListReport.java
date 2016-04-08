package com.rtiming.client.common.report.jrxml;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.report.DataSourceUtility;
import com.rtiming.client.entry.AbstractEntriesTablePage.Table;
import com.rtiming.client.entry.EntriesTablePage;
import com.rtiming.shared.common.report.template.IReportParameters;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.services.code.ReportTypeCodeType;

public class StartListReport extends AbstractDefaultRunnerListReport {

  public StartListReport(Long eventNr, List<Long> classUids, List<Long> courseNrs, List<Long> clubNrs) {
    super(eventNr, classUids, courseNrs, clubNrs);
  }

  @Override
  protected SubreportDataSource getResultData(Long resultEventNr, Long classUid, Long courseNr, Long clubNr, Map<String, Object> parameters) throws ProcessingException {

    // set header data
    RunnerListHeaderInfo info = new RunnerListHeaderInfo(resultEventNr, classUid, courseNr, clubNr);
    String courseInfo = info.getCourseInfo();

    EntriesTablePage startlist = new EntriesTablePage(resultEventNr, ClientSession.get().getSessionClientNr(), null, classUid, courseNr, clubNr);
    startlist.nodeAddedNotify();
    startlist.getSearchForm().getResetButton().doClick();
    startlist.getSearchForm().getEventField().setValue(resultEventNr);
    startlist.getSearchForm().getStartTimeFrom().setValue(null);
    startlist.getSearchForm().getStartTimeTo().setValue(null);
    startlist.getTable().resetColumnSortOrder();
    startlist.loadChildren();
    Table data = startlist.getTable();

    SubreportDataSource content = new SubreportDataSource();

    parameters.put(IReportParameters.TABLE_RUNNER_FINISHED, NumberUtility.format(data.getRowCount()));
    Long runnerStarted = BEANS.get(IEventProcessService.class).getRunnerStartedCount(resultEventNr, classUid, info.getCourseNr());
    parameters.put(IReportParameters.TABLE_RUNNER_TOTAL, NumberUtility.format(runnerStarted));
    parameters.put(IReportParameters.TABLE_COURSE_INFO, courseInfo);

    // add parameters and column headers from table
    parameters.putAll(DataSourceUtility.createColumnHeadersParametersFromTable(startlist.getTable()));
    Collection<Map<String, ?>> runnerRows = DataSourceUtility.createCollectionFromTable(data, false, true);

    content.putRunnersData(runnerRows);
    content.putHeaderData(parameters);

    if (runnerRows.size() > 0) {
      return content;
    }
    return null;
  }

  @Override
  protected Long getReportTypeUid() {
    return ReportTypeCodeType.StartListCode.ID;
  }

}
