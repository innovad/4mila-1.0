package com.rtiming.client.common.report.jrxml;

import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.client.ecard.download.PunchForm;
import com.rtiming.client.result.ResultsClassesTablePage;
import com.rtiming.client.result.ResultsClubsTablePage;
import com.rtiming.client.result.ResultsCoursesTablePage;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.client.test.data.ClubTestDataProvider;
import com.rtiming.client.test.data.CourseControlTestDataProvider;
import com.rtiming.client.test.data.DownloadedECardTestDataProvider;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.IRaceService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class ResultReportTest {

  private EventWithIndividualClassTestDataProvider event;
  private ECardStationTestDataProvider eCardStation;
  private ClubTestDataProvider club;

  @Before
  public void before() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    eCardStation = new ECardStationTestDataProvider();
    club = new ClubTestDataProvider();
  }

  @Test
  public void testClassResultReport() throws ProcessingException {

    createEventWithResults();

    ResultsClassesTablePage resultsClasses = new ResultsClassesTablePage(ClientSession.get().getSessionClientNr());
    SingleEventSearchForm searchForm = (SingleEventSearchForm) resultsClasses.getSearchFormInternal();
    searchForm.getEventField().setValue(event.getEventNr());
    Assert.assertEquals(event.getEventNr(), searchForm.getEventField().getValue());
    searchForm.resetSearchFilter();
    resultsClasses.loadChildren();

    Assert.assertEquals(2, resultsClasses.getTable().getRowCount());
    Assert.assertEquals(event.getClassUid(), resultsClasses.getTable().getClassUidColumn().getValue(0));
    Assert.assertEquals(1, resultsClasses.getTable().getRunnersColumn().getValue(0).longValue());
    Assert.assertEquals(1, resultsClasses.getTable().getEntriesColumn().getValue(0).longValue());
    Assert.assertEquals(1, resultsClasses.getTable().getProcessedColumn().getValue(0).longValue());
    Assert.assertEquals(0, resultsClasses.getTable().getMissingColumn().getValue(0).longValue());

    searchForm.getEventField().setValue(event.getEventNr());
    resultsClasses.getTable().selectFirstRow();
    boolean run = resultsClasses.getTable().runMenu(ResultsClassesTablePage.Table.CreatePDFMenu.class);
    Assert.assertTrue(run);
  }

  @Test
  public void testCourseResultReport() throws ProcessingException {

    createEventWithResults();

    ResultsCoursesTablePage resultsCourses = new ResultsCoursesTablePage();
    SingleEventSearchForm searchForm = (SingleEventSearchForm) resultsCourses.getSearchFormInternal();
    searchForm.getEventField().setValue(event.getEventNr());
    Assert.assertEquals(event.getEventNr(), searchForm.getEventField().getValue());
    searchForm.resetSearchFilter();
    resultsCourses.loadChildren();

    Assert.assertEquals(2, resultsCourses.getTable().getRowCount());
    Assert.assertEquals(event.getCourseNr(), resultsCourses.getTable().getCourseNrColumn().getValue(0));
    Assert.assertEquals(1, resultsCourses.getTable().getRunnersColumn().getValue(0).longValue());
    Assert.assertEquals(1, resultsCourses.getTable().getEntriesColumn().getValue(0).longValue());
    Assert.assertEquals(1, resultsCourses.getTable().getProcessedColumn().getValue(0).longValue());
    Assert.assertEquals(0, resultsCourses.getTable().getMissingColumn().getValue(0).longValue());

    searchForm.getEventField().setValue(event.getEventNr());
    resultsCourses.getTable().selectFirstRow();
    boolean run = resultsCourses.getTable().runMenu(ResultsCoursesTablePage.Table.CreatePDFMenu.class);
    Assert.assertTrue(run);
  }

  @Test
  public void testClubResultReport() throws ProcessingException {

    createEventWithResults();

    ResultsClubsTablePage resultsClubs = new ResultsClubsTablePage();
    SingleEventSearchForm searchForm = (SingleEventSearchForm) resultsClubs.getSearchFormInternal();
    searchForm.getEventField().setValue(event.getEventNr());
    Assert.assertEquals(event.getEventNr(), searchForm.getEventField().getValue());
    searchForm.resetSearchFilter();
    resultsClubs.loadChildren();

    Assert.assertEquals(1, resultsClubs.getTable().getRowCount()); // 1, there is no total row here
    Assert.assertEquals(1, resultsClubs.getTable().getRunnersColumn().getValue(0).longValue());
    Assert.assertEquals(1, resultsClubs.getTable().getEntriesColumn().getValue(0).longValue());
    Assert.assertEquals(1, resultsClubs.getTable().getProcessedColumn().getValue(0).longValue());
    Assert.assertEquals(0, resultsClubs.getTable().getMissingColumn().getValue(0).longValue());

    searchForm.getEventField().setValue(event.getEventNr());
    resultsClubs.getTable().selectFirstRow();
    boolean run = resultsClubs.getTable().runMenu(ResultsClubsTablePage.Table.CreatePDFMenu.class);
    Assert.assertTrue(run);
  }

  private void createEventWithResults() throws ProcessingException {
    // Control
    CourseControlTestDataProvider startControl = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.StartCode.ID, 1L, "S");
    CourseControlTestDataProvider control = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.ControlCode.ID, 2L, "31");
    CourseControlTestDataProvider finishControl = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.FinishCode.ID, 3L, "Z");
    Assert.assertNotNull(startControl.getControlNo());
    Assert.assertNotNull(control.getControlNo());
    Assert.assertNotNull(finishControl.getControlNo());

    // Entry
    EntryTestDataProvider entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    // Punch Session including Times
    DownloadedECardTestDataProvider download = new DownloadedECardTestDataProvider(event.getEventNr(), entry.getRaceNr(), entry.getECardNr(), eCardStation.getECardStationNr());
    Assert.assertNotNull(download.getForm().getPunchSessionNr());
    Date start = download.getForm().getStartField().getValue();

    // Punch Form
    PunchForm punch = new PunchForm();
    punch.startNew();
    punch.getPunchSessionField().setValue(download.getForm().getPunchSessionNr());
    punch.getControlNoField().setValue(control.getControlNo());
    punch.getEventField().setValue(event.getEventNr());
    punch.getTimeField().setValue(start);
    punch.getSortCodeField().setValue(1L);
    punch.doOk();

    // Validate (OK)
    BEANS.get(IRaceService.class).validateAndPersistRace(entry.getRaceNr());
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    eCardStation.remove();
    club.remove();
  }

}
