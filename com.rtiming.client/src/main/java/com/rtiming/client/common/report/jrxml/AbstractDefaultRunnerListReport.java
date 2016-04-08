package com.rtiming.client.common.report.jrxml;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;

public abstract class AbstractDefaultRunnerListReport extends AbstractRunnerListReport {

  private final Long eventNr;
  private final List<Long> classUids;
  private final List<Long> courseNrs;
  private final List<Long> clubNrs;

  public AbstractDefaultRunnerListReport(Long eventNr, List<Long> classUids, List<Long> courseNrs, List<Long> clubNrs) {
    super();
    this.eventNr = eventNr;
    this.classUids = classUids;
    this.courseNrs = courseNrs;
    this.clubNrs = clubNrs;
  }

  public void openReport() throws ProcessingException {
    ReportParam param = new ReportParam();
    param.setEventNr(eventNr);
    param.setClassUids(classUids);
    param.setCourseNrs(courseNrs);
    param.setClubNrs(clubNrs);
    super.openReport(param);
  }

  public void printReport() throws ProcessingException {
    ReportParam param = new ReportParam();
    param.setEventNr(eventNr);
    param.setClassUids(classUids);
    param.setCourseNrs(courseNrs);
    param.setClubNrs(clubNrs);
    super.printReport(param);
  }

}
