package com.rtiming.client.common.report.jrxml;

import java.util.Date;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
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

import com.rtiming.client.ecard.download.PunchForm;
import com.rtiming.client.entry.EntriesClassesTablePage;
import com.rtiming.client.entry.EntriesClubsTablePage;
import com.rtiming.client.entry.EntriesCoursesTablePage;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.client.test.data.ClubTestDataProvider;
import com.rtiming.client.test.data.CourseControlTestDataProvider;
import com.rtiming.client.test.data.DownloadedECardTestDataProvider;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class StartListReportTest {

  private EventWithIndividualClassTestDataProvider event;
  private ECardStationTestDataProvider eCardStation;
  private ClubTestDataProvider club;

  @Before
  public void before() throws ProcessingException {
    club = new ClubTestDataProvider();
    event = new EventWithIndividualClassTestDataProvider();
    eCardStation = new ECardStationTestDataProvider();
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());
  }

  @Test
  public void testClassStartlistReport() throws ProcessingException {

    createEventWithResults();

    EntriesClassesTablePage entriesClasses = new EntriesClassesTablePage();
    entriesClasses.nodeAddedNotify();
    SingleEventSearchForm searchForm = (SingleEventSearchForm) entriesClasses.getSearchFormInternal();
    searchForm.getEventField().setValue(event.getEventNr());
    Assert.assertEquals(event.getEventNr(), searchForm.getEventField().getValue());
    searchForm.resetSearchFilter();
    entriesClasses.loadChildren();

    Assert.assertEquals(2, entriesClasses.getTable().getRowCount());
    Assert.assertEquals(event.getClassUid(), entriesClasses.getTable().getClassUidColumn().getValue(0));
    Assert.assertEquals(1, entriesClasses.getTable().getRunnersColumn().getValue(0).longValue());
    Assert.assertEquals(1, entriesClasses.getTable().getEntriesColumn().getValue(0).longValue());

    searchForm.getEventField().setValue(event.getEventNr());
    entriesClasses.getTable().selectFirstRow();
    boolean run = entriesClasses.getTable().runMenu(EntriesClassesTablePage.Table.CreatePDFMenu.class);
    Assert.assertTrue(run);
  }

  @Test
  public void testCourseStartlistReport() throws ProcessingException {

    createEventWithResults();

    EntriesCoursesTablePage entriesCourses = new EntriesCoursesTablePage();
    entriesCourses.nodeAddedNotify();
    SingleEventSearchForm searchForm = (SingleEventSearchForm) entriesCourses.getSearchFormInternal();
    searchForm.getEventField().setValue(event.getEventNr());
    Assert.assertEquals(event.getEventNr(), searchForm.getEventField().getValue());
    searchForm.resetSearchFilter();
    entriesCourses.loadChildren();

    Assert.assertEquals(2, entriesCourses.getTable().getRowCount());
    Assert.assertEquals(event.getCourseNr(), entriesCourses.getTable().getCourseNrColumn().getValue(0));
    Assert.assertEquals(1, entriesCourses.getTable().getRunnersColumn().getValue(0).longValue());
    Assert.assertEquals(1, entriesCourses.getTable().getEntriesColumn().getValue(0).longValue());

    searchForm.getEventField().setValue(event.getEventNr());
    entriesCourses.getTable().selectFirstRow();
    boolean run = entriesCourses.getTable().runMenu(EntriesCoursesTablePage.Table.CreatePDFMenu.class);
    Assert.assertTrue(run);
  }

  @Test
  public void testClubStartlistReport() throws ProcessingException {

    createEventWithResults();

    EntriesClubsTablePage entriesClubs = new EntriesClubsTablePage();
    entriesClubs.nodeAddedNotify();
    SingleEventSearchForm searchForm = (SingleEventSearchForm) entriesClubs.getSearchFormInternal();
    searchForm.getEventField().setValue(event.getEventNr());
    Assert.assertEquals(event.getEventNr(), searchForm.getEventField().getValue());
    searchForm.resetSearchFilter();
    entriesClubs.loadChildren();

    Assert.assertEquals(1, entriesClubs.getTable().getRowCount()); // 1, there is no total row here
    Assert.assertEquals(1, entriesClubs.getTable().getRunnersColumn().getValue(0).longValue());
    Assert.assertEquals(1, entriesClubs.getTable().getEntriesColumn().getValue(0).longValue());
    Assert.assertEquals("Club", club.getClubNr(), entriesClubs.getTable().getClubNrColumn().getValue(0));

    searchForm.getEventField().setValue(event.getEventNr());
    entriesClubs.getTable().selectFirstRow();
    boolean run = entriesClubs.getTable().runMenu(EntriesClubsTablePage.Table.CreatePDFMenu.class);
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

    RaceBean bean = new RaceBean();
    bean.setRaceNr(entry.getRaceNr());
    bean = BEANS.get(IRaceProcessService.class).load(bean);
    bean.setClubNr(club.getClubNr());
    BEANS.get(IRaceProcessService.class).store(bean);

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
