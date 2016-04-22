package com.rtiming.client.common.report.dynamic;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.report.FMilaReport;
import com.rtiming.client.event.EventsTablePage;
import com.rtiming.shared.FMilaUtility;

/**
 * @author amo
 */
@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class GenericTableDynamicReportTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNull1() throws Exception {
    GenericTableDynamicReport report = new GenericTableDynamicReport(null, null, null);
    report.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNull2() throws Exception {
    List<GenericTableReportContent> list = new ArrayList<GenericTableReportContent>();
    GenericTableDynamicReport report = new GenericTableDynamicReport(list, null, null);
    report.build();
  }

  @Test(expected = ProcessingException.class)
  public void testNull3() throws Exception {
    List<GenericTableReportContent> list = new ArrayList<GenericTableReportContent>();
    GenericTableDynamicReport report = new GenericTableDynamicReport(list, null, "bla");
    report.build();
  }

  @Test
  public void testReport1() throws Exception {
    List<GenericTableReportContent> list = new ArrayList<GenericTableReportContent>();
    GenericTableReportContent content = new GenericTableReportContent();
    list.add(content);
    GenericTableDynamicReport report = new GenericTableDynamicReport(list, null, getTemplate());
    report.build();
  }

  @Test
  public void testReport2() throws Exception {
    List<GenericTableReportContent> list = new ArrayList<GenericTableReportContent>();
    GenericTableReportContent content = new GenericTableReportContent();
    EventsTablePage eventsTablePage = new EventsTablePage(ClientSession.get().getSessionClientNr());
    eventsTablePage.nodeAddedNotify();
    eventsTablePage.loadChildren();
    content.setTable(eventsTablePage.getTable());
    list.add(content);
    GenericTableDynamicReport report = new GenericTableDynamicReport(list, null, getTemplate());
    report.build();
  }

  private String getTemplate() throws ProcessingException {
    URL jasperFile = FMilaUtility.findFileLocation(FMilaReport.LOCAL_REPORT_DIR + "headerFooterTemplate.jrxml", "");
    return jasperFile.getPath();
  }
}
