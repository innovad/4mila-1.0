package com.rtiming.client.common.report.jrxml;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.code.CODES;

import com.rtiming.client.common.report.ReportType;
import com.rtiming.client.race.RaceControlsTablePage;
import com.rtiming.client.race.RaceControlsTablePage.Table;
import com.rtiming.shared.common.report.template.IReportParameters;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.results.IResultsOutlineService;
import com.rtiming.shared.results.SplitTimeReportData;
import com.rtiming.shared.services.code.ReportTypeCodeType;

public final class SplitTimesReport {

  private SplitTimesReport() {
  }

  public static void printSplitTimesReport(Long[] raceNrs, String printer) throws ProcessingException {
    createSplitTimesReport(raceNrs, true, printer, false);
  }

  public static void openSplitTimesReport(Long[] raceNrs) throws ProcessingException {
    createSplitTimesReport(raceNrs, false, null, true);
  }

  private static void createSplitTimesReport(Long[] raceNrs, boolean print, String printer, boolean open) throws ProcessingException {

    FMilaJrxmlReport report = new FMilaJrxmlReport();
    ReportDataSource list = new ReportDataSource();
    Long eventNr = null;
    Map<String, Object> mainParameters = report.getParameters();

    for (Long raceNr : raceNrs) {
      if (raceNr == null) {
        throw new IllegalArgumentException("RaceNr must be set");
      }

      SplitTimeReportData splitTimesReportData = BEANS.get(IResultsOutlineService.class).getSplitTimesReportData(raceNr);
      Map<String, Object> subreportParameters = CollectionUtility.copyMap(mainParameters);
      subreportParameters.putAll(splitTimesReportData.getParameters());
      subreportParameters.put(IReportParameters.EVENT_DATE, splitTimesReportData.getEventDate()); // in jasper, we use a date object

      // use the first runner to determine the event template
      eventNr = (eventNr != null ? splitTimesReportData.getEventNr() : eventNr);

      SubreportDataSource sub = new SubreportDataSource();
      list.add(sub);
      Collection<Map<String, ?>> controlRows = new LinkedList<Map<String, ?>>();
      sub.putRunnersData(controlRows);
      sub.putHeaderData(subreportParameters);

      RaceControlsTablePage splits = new RaceControlsTablePage(raceNr);
      splits.loadChildren();
      Table data = splits.getTable();
      for (int i = 0; i < data.getRowCount(); i++) {
        if (CompareUtility.notEquals(data.getControlStatusColumn().getValue(i), ControlStatusCodeType.InitialStatusCode.ID) &&
            (data.getControlTypeColumn().getValue(i) == ControlTypeCodeType.ControlCode.ID ||
            data.getControlTypeColumn().getValue(i) == ControlTypeCodeType.FinishCode.ID)) {
          Map<String, String> control = new HashMap<String, String>();
          // control code
          if (data.getControlTypeColumn().getValue(i) == ControlTypeCodeType.ControlCode.ID) {
            control.put(IReportParameters.CONTROL_NO, "(" + data.getControlColumn().getValue(i) + ")");
          }
          else {
            control.put(IReportParameters.CONTROL_NO, data.getControlColumn().getValue(i));
          }
          // sortcode
          // additional control
          if (data.getControlStatusColumn().getValue(i) == ControlStatusCodeType.AdditionalCode.ID ||
              data.getControlStatusColumn().getValue(i) == ControlStatusCodeType.WrongCode.ID) {
            control.put(IReportParameters.CONTROL_SORTCODE, "*");
          }
          // finish
          else if (data.getControlTypeColumn().getValue(i) == ControlTypeCodeType.FinishCode.ID) {
            control.put(IReportParameters.CONTROL_SORTCODE, "");
          }
          else {
            control.put(IReportParameters.CONTROL_SORTCODE, data.getSortCodeColumn().getValue(i).toString());
          }
          // status (except ok)
          if (data.getControlStatusColumn().getValue(i) != ControlStatusCodeType.OkCode.ID) {
            Long statusUid = data.getControlStatusColumn().getValue(i);
            control.put(IReportParameters.CONTROL_STATUS, CODES.getCodeType(ControlStatusCodeType.class).getCode(statusUid).getText());
          }
          // times
          control.put(IReportParameters.CONTROL_TIME_ABS, "" + data.getOverallTimeColumn().getValue(i));
          control.put(IReportParameters.CONTROL_TIME_REL, "" + StringUtility.emptyIfNull(data.getRelativeTimeColumn().getValue(i)));
          control.put(IReportParameters.CONTROL_TIME_LEG, "" + StringUtility.emptyIfNull(data.getLegTimeColumn().getValue(i)));

          controlRows.add(control);
        }
      }
    }

    report.setData(list);
    report.setTemplate(ReportTypeCodeType.SplitTimesCode.ID, eventNr);

    report.createReport(ReportType.PDF, print, printer, open);

  }
}
