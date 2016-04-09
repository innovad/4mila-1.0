package com.rtiming.client.common.report.jrxml;

import java.util.Map;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.client.common.report.ReportType;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.common.report.template.IReportParameters;
import com.rtiming.shared.event.IEventProcessService;

public abstract class AbstractRunnerListReport {

  public void printReport(ReportParam param) throws ProcessingException {
    createReport(param, true, false);
  }

  public void openReport(ReportParam param) throws ProcessingException {
    createReport(param, false, true);
  }

  private void createReport(ReportParam param, boolean print, boolean openFile) throws ProcessingException {

    if (param == null || param.getEventNr() == null) {
      throw new ProcessingException("No event selected - please select an event.");
    }

    ReportDataSource contents = new ReportDataSource();
    FMilaJrxmlReport report = new FMilaJrxmlReport();

    // Event Data
    Map<String, Object> mainParameters = report.getParameters();
    EventBean event = new EventBean();
    event.setEventNr(param.getEventNr());
    event = BEANS.get(IEventProcessService.class).load(event);

    if (!StringUtility.isNullOrEmpty(event.getFormat())) {
      mainParameters.put(IReportParameters.JASPER_LOGO_PARAMETER, "logo/" + event.getClientNr() + "/" + param.getEventNr() + "." + event.getFormat());
    }
    mainParameters.put(IReportParameters.EVENT_NAME, StringUtility.emptyIfNull(event.getName()));
    mainParameters.put(IReportParameters.EVENT_MAP, StringUtility.emptyIfNull(event.getMap()));
    mainParameters.put(IReportParameters.EVENT_LOCATION, StringUtility.emptyIfNull(event.getLocation()));
    mainParameters.put(IReportParameters.EVENT_DATE, event.getEvtZero());
    mainParameters.put(IReportParameters.TABLE_CLUB, new Boolean(param.getClubNrs() != null));
    mainParameters.put(IReportParameters.TABLE_CLASS, new Boolean(param.getClassUids() != null));
    mainParameters.put(IReportParameters.TABLE_COURSE, new Boolean(param.getCourseNrs() != null));

    // load classes
    if (param.getClassUids() != null) {
      for (Long classUid : param.getClassUids()) {
        if (classUid != null) {
          contents.add(getResultData(param.getEventNr(), classUid, null, null, CollectionUtility.copyMap(mainParameters)));
        }
      }
    }

    // load courses
    if (param.getCourseNrs() != null) {
      for (Long courseNr : param.getCourseNrs()) {
        if (courseNr != null) {
          contents.add(getResultData(param.getEventNr(), null, courseNr, null, CollectionUtility.copyMap(mainParameters)));
        }
      }
    }

    // load clubs
    if (param.getClubNrs() != null) {
      for (Long clubNr : param.getClubNrs()) {
        if (clubNr != null) {
          contents.add(getResultData(param.getEventNr(), null, null, clubNr, CollectionUtility.copyMap(mainParameters)));
        }
      }
    }
    report.setData(contents);

    report.setTemplate(getReportTypeUid(), param.getEventNr());
    report.createReport(ReportType.PDF, print, null, openFile);
  }

  protected abstract SubreportDataSource getResultData(Long eventNr, Long classUid, Long courseNr, Long clubNr, Map<String, Object> mainParameters) throws ProcessingException;

  protected abstract Long getReportTypeUid();

}
