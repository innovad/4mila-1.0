package com.rtiming.client.common.report.jrxml;

import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ecard.download.DownloadedECardForm;
import com.rtiming.client.event.course.CourseForm;
import com.rtiming.client.settings.CodeForm;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.entry.EntryFormData;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

/**
 * @author amo
 */

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class SplitTimesReportTest {

  @Test
  public void test() throws ProcessingException {
    // Event
    EventTestDataProvider event = new EventTestDataProvider();

    // ECard
    ECardTestDataProvider ecard = new ECardTestDataProvider();

    // Class
    CodeForm clazz = FMilaClientTestUtility.createClass();

    // Course
    CourseForm course = FMilaClientTestUtility.createCourse(event.getEventNr());

    // Control
    FMilaClientTestUtility.createCourseControl(event.getEventNr(), course.getCourseNr(), ControlTypeCodeType.StartCode.ID, 1L);

    // Event Class
    FMilaClientTestUtility.createEventClass(event.getEventNr(), clazz.getCodeUid(), course.getCourseNr(), ClassTypeCodeType.IndividualEventCode.ID, CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);

    // Entry
    EntryFormData entry = FMilaClientTestUtility.createIndividualEntry(event.getEventNr(), clazz.getCodeUid());

    // Race and E-Card
    Long raceNr = entry.getRaces().getRows()[entry.getRaces().getRowCount() - 1].getRaceNr();
    Assert.assertNotNull(raceNr);

    Long eCardNr = entry.getRaces().getRows()[entry.getRaces().getRowCount() - 1].getECard();
    Assert.assertNotNull(eCardNr);

    Long runnerNr = entry.getRaces().getRows()[entry.getRaces().getRowCount() - 1].getRunnerNr();
    Assert.assertNotNull(runnerNr);

    // ECard Download Station
    ECardStationTestDataProvider stationForm = new ECardStationTestDataProvider();

    // Punch Session
    DownloadedECardForm punchSession = new DownloadedECardForm();
    punchSession.startNew();
    punchSession.getEventField().setValue(event.getEventNr());
    punchSession.getRaceField().setValue(raceNr);
    punchSession.getECardField().setValue(eCardNr);
    punchSession.getECardStationField().setValue(stationForm.getECardStationNr());
    punchSession.getStartField().setValue(DateUtility.addDays(new Date(), -0.2));
    punchSession.getFinishField().setValue(new Date());
    punchSession.doOk();

    BEANS.get(IRaceService.class).validateAndPersistRace(raceNr);
    RaceBean raceFormData = new RaceBean();
    raceFormData.setRaceNr(raceNr);
    raceFormData = BEANS.get(IRaceProcessService.class).load(raceFormData);
    Assert.assertEquals(raceFormData.getStatusUid().longValue(), RaceStatusCodeType.OkCode.ID);

    // Report
    SplitTimesReport.openSplitTimesReport(new Long[]{raceNr});

    // Delete
    BEANS.get(IEventProcessService.class).delete(event.getEventNr(), true);

    RunnerBean runner = new RunnerBean();
    runner.setRunnerNr(runnerNr);
    BEANS.get(IRunnerProcessService.class).delete(runner);

    stationForm.remove();
    ecard.remove();
  }

}
