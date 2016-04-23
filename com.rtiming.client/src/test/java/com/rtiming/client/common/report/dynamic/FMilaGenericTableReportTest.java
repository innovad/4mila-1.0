package com.rtiming.client.common.report.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.report.ReportType;
import com.rtiming.client.event.EventsTablePage;
import com.rtiming.shared.services.code.ReportTypeCodeType;

/**
 * @author amo
 */
@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class FMilaGenericTableReportTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNoContent1() throws Exception {
    FMilaGenericTableReport report = new FMilaGenericTableReport();
    report.createReport(ReportType.HTML, false, null, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoContent2() throws Exception {
    FMilaGenericTableReport report = new FMilaGenericTableReport();
    report.setContent(new ArrayList<GenericTableReportContent>());
    report.createReport(ReportType.HTML, false, null, true);
  }

  @Test
  public void testNoContent3() throws Exception {
    FMilaGenericTableReport report = new FMilaGenericTableReport();
    report.setContent(new ArrayList<GenericTableReportContent>());
    report.setTemplate(ReportTypeCodeType.DefaultCode.ID, null);
    String path = report.createReport(ReportType.HTML, false, null, true);
    Assert.assertNotNull("Report Path", path);
  }

  @Test
  public void testEmptyContent() throws Exception {
    FMilaGenericTableReport report = new FMilaGenericTableReport();
    List<GenericTableReportContent> content = new ArrayList<GenericTableReportContent>();
    GenericTableReportContent genericTable = new GenericTableReportContent();
    content.add(genericTable);
    report.setContent(content);
    report.setTemplate(ReportTypeCodeType.DefaultCode.ID, null);
    report.createReport(ReportType.HTML, false, null, true);
  }

  @Test
  public void testReport() throws Exception {
    FMilaGenericTableReport report = new FMilaGenericTableReport();
    List<GenericTableReportContent> content = new ArrayList<GenericTableReportContent>();
    GenericTableReportContent genericTable = new GenericTableReportContent();
    EventsTablePage eventsTablePage = new EventsTablePage(ClientSession.get().getSessionClientNr());
    eventsTablePage.nodeAddedNotify();
    eventsTablePage.loadChildren();
    genericTable.setTable(eventsTablePage.getTable());
    content.add(genericTable);
    report.setContent(content);
    report.setTemplate(ReportTypeCodeType.DefaultCode.ID, null);
    report.createReport(ReportType.HTML, false, null, true);
    Assert.assertNotNull(report.getSubreportDirectory());
  }

  @Test
  public void testForNotBlankNullStrings() throws Exception {
    FMilaGenericTableReport report = new FMilaGenericTableReport();
    List<GenericTableReportContent> content = new ArrayList<GenericTableReportContent>();
    GenericTableReportContent genericTable = new GenericTableReportContent();
    content.add(genericTable);
    report.setContent(content);
    report.setTemplate(ReportTypeCodeType.DefaultCode.ID, null);
    String path = report.createReport(ReportType.HTML, false, null, false);
    String html = IOUtility.getContent(path).toString();
    Assert.assertTrue("HTML should not contain null", !html.contains("null"));
  }

}
