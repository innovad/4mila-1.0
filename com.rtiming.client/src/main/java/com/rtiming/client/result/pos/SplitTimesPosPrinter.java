package com.rtiming.client.result.pos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.services.common.code.CODES;

import com.rtiming.client.race.RaceControlsTablePage;
import com.rtiming.client.race.RaceControlsTablePage.Table;
import com.rtiming.shared.common.report.template.IReportParameters;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.results.IResultsOutlineService;
import com.rtiming.shared.results.SplitTimeReportData;

public final class SplitTimesPosPrinter {

  private SplitTimesPosPrinter() {
  }

  public static void printSplitTimesReport(long raceNr, IPosPrinter printer) throws ProcessingException {
    SplitTimeReportData generalData = BEANS.get(IResultsOutlineService.class).getSplitTimesReportData(raceNr);

    RaceControlsTablePage splits = new RaceControlsTablePage(raceNr);
    splits.loadChildren();
    Table data = splits.getTable();
    List<SplitDataBean> beans = new ArrayList<SplitDataBean>();
    for (ITableRow row : data.getRows()) {
      SplitDataBean bean = new SplitDataBean();
      bean.setControlNo(data.getControlColumn().getValue(row));
      bean.setControlStatusUid(data.getControlStatusColumn().getValue(row));
      bean.setControlTypeUid(data.getControlTypeColumn().getValue(row));
      bean.setLegTime(data.getLegTimeColumn().getValue(row));
      bean.setRelativeLegTime(data.getRelativeTimeColumn().getValue(row));
      bean.setSortCode(data.getSortCodeColumn().getValue(row));
      beans.add(bean);
    }

    String additionalText = CODES.getCode(ControlStatusCodeType.AdditionalCode.class).getText();
    String wrongText = CODES.getCode(ControlStatusCodeType.WrongCode.class).getText();
    String missingText = CODES.getCode(ControlStatusCodeType.MissingCode.class).getText();

    printSplitTimesInternal(beans, generalData.getParameters(), printer, additionalText, wrongText, missingText);
  }

  public static void printSplitTimesInternal(List<SplitDataBean> splitData, Map<String, String> generalData, IPosPrinter printer, String additionalText, String wrongText, String missingText) throws ProcessingException {
    String runnerName = generalData.get(IReportParameters.RUNNER_NAME);
    String eCardNo = generalData.get(IReportParameters.RUNNER_ECARD_NO);
    String runnerClub = generalData.get(IReportParameters.RUNNER_CLUB);
    String clazz = generalData.get(IReportParameters.RACE_CLASS);
    String time = generalData.get(IReportParameters.RACE_TIME);
    String appName = generalData.get(IReportParameters.APPLICATION_NAME);
    String currentTime = DateUtility.formatDateTime(new Date());
    String eventDate = generalData.get(IReportParameters.EVENT_DATE_FORMATTED);
    String eventName = generalData.get(IReportParameters.EVENT_NAME);

    SplitBuilder builder = new SplitBuilder(printer.getLineWidth());
    builder.appendLeftRight(eventName, eventDate);
    builder.appendNewLine();
    builder.appendNewLine();
    builder.appendWide(runnerName);
    builder.appendNewLine();
    builder.appendLeftRightWide(clazz, time);
    builder.appendNewLine();
    builder.append(eCardNo);
    if (!StringUtility.isNullOrEmpty(runnerClub)) {
      builder.append(", ");
      builder.append(runnerClub);
    }
    builder.appendNewLine();
    builder.appendNewLine();

    int sortCodeCol = 5;
    int controlNoCol = 6;
    int statusCol = 2;
    int legTimeCol = 12;
    int overallTimeCol = 12;
    boolean printAdditionalStatusLegend = false;
    boolean printWrongOrderStatusLegend = false;
    boolean printMissingStatusLegend = false;
    boolean lineBreakAfterControlBlockDone = false;

    for (SplitDataBean bean : splitData) {
      if (CompareUtility.notEquals(bean.getControlStatusUid(), ControlStatusCodeType.InitialStatusCode.ID)) {
        // Sort Code
        if (ControlTypeCodeType.ControlCode.ID == bean.getControlTypeUid() &&
            ControlStatusCodeType.AdditionalCode.ID != bean.getControlStatusUid() &&
            ControlStatusCodeType.WrongCode.ID != bean.getControlStatusUid()) {
          builder.appendPadLeft(String.valueOf(bean.getSortCode()), sortCodeCol);
        }
        else {
          builder.appendPadLeft("", sortCodeCol);
        }

        // Control Code
        builder.appendPadLeft(bean.getControlNo(), controlNoCol);

        // Status
        String status = "";
        if (ControlStatusCodeType.AdditionalCode.ID == bean.getControlStatusUid()) {
          status = "+";
          printAdditionalStatusLegend = true;
        }
        else if (ControlStatusCodeType.MissingCode.ID == bean.getControlStatusUid()) {
          status = "-";
          printMissingStatusLegend = true;
        }
        else if (ControlStatusCodeType.WrongCode.ID == bean.getControlStatusUid()) {
          status = "X";
          printWrongOrderStatusLegend = true;
        }
        builder.appendPadLeft(status, statusCol);

        // Leg Time
        builder.appendPadLeft(bean.getLegTime(), legTimeCol);

        // Overall Time
        builder.appendPadLeft(bean.getRelativeLegTime(), overallTimeCol);

        // Line Break
        builder.appendNewLine();

        // another Line Break after Finish (separate additional controls)
        if (ControlTypeCodeType.FinishCode.ID == bean.getControlTypeUid()) {
          builder.appendNewLine();
          lineBreakAfterControlBlockDone = true;
        }
        else {
          lineBreakAfterControlBlockDone = false;
        }
      }
    }

    // Legend (+ - X)
    if (printAdditionalStatusLegend || printWrongOrderStatusLegend || printMissingStatusLegend) {
      builder.appendNewLine();
      if (printAdditionalStatusLegend) {
        builder.append("+ " + additionalText);
      }
      if (printWrongOrderStatusLegend) {
        if (printAdditionalStatusLegend) {
          builder.append(", ");
        }
        builder.append("X " + wrongText);
      }
      if (printMissingStatusLegend) {
        if (printAdditionalStatusLegend || printWrongOrderStatusLegend) {
          builder.append(", ");
        }
        builder.append("- " + missingText);
      }
      builder.appendNewLine();
      lineBreakAfterControlBlockDone = true;
    }

    // 4mila Info
    if (!lineBreakAfterControlBlockDone) {
      builder.appendNewLine();
    }
    builder.appendLeftRight(appName, currentTime);
    builder.cut();

    printer.print(builder.toString());
  }

}
