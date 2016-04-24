package com.rtiming.client.race;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.ecard.download.PunchForm;
import com.rtiming.client.race.RaceControlForm.MainBox.ManualStatusField;
import com.rtiming.client.race.RaceControlForm.MainBox.ShiftTimeField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.CourseControlTestDataProvider;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.DownloadedECardTestDataProvider;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.PunchTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.entry.EntryFormData;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.race.RaceStatusCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestClientSession.class)
public class RaceControlFormTest extends AbstractFormTest<RaceControlForm> {

  private EventWithIndividualClassTestDataProvider eventForm;
  private Long raceNr;
  private static CurrencyTestDataProvider currency;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    currency = new CurrencyTestDataProvider();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    currency.remove();
  }

  @Override
  public void setUpForm() throws ProcessingException {
    // Event
    eventForm = new EventWithIndividualClassTestDataProvider();

    // Entry
    EntryFormData entry = FMilaClientTestUtility.createIndividualEntry(eventForm.getEventNr(), eventForm.getClassUid());

    // Race and E-Card
    raceNr = entry.getRaces().getRows()[entry.getRaces().getRowCount() - 1].getRaceNr();
    Assert.assertNotNull(raceNr);

    super.setUpForm();
  }

  @Override
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(ManualStatusField.class, true));
    list.add(new FieldValue(ShiftTimeField.class, 0L));
    return list;
  }

  @Override
  protected RaceControlForm getStartedForm() throws ProcessingException {
    RaceControlForm raceControl = new RaceControlForm();
    raceControl.getRaceField().setValue(raceNr);
    raceControl.startNew();
    return raceControl;
  }

  @Override
  protected RaceControlForm getModifyForm() throws ProcessingException {
    RaceControlForm raceControl = new RaceControlForm();
    raceControl.setRaceControlNr(getForm().getRaceControlNr());
    raceControl.startModify();
    return raceControl;
  }

  @Override
  public void cleanup() throws ProcessingException {
    eventForm.remove();
  }

  @Test
  public void testValidateRaceMissingPunch() throws ProcessingException {

    // Event
    EventWithIndividualClassTestDataProvider event = new EventWithIndividualClassTestDataProvider();

    // Control
    CourseControlTestDataProvider startControl = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.StartCode.ID, 1L, "S");
    CourseControlTestDataProvider control = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.ControlCode.ID, 2L, "31");
    CourseControlTestDataProvider finishControl = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.FinishCode.ID, 3L, "Z");
    Assert.assertNotNull(startControl.getControlNo());
    Assert.assertNotNull(control.getControlNo());
    Assert.assertNotNull(finishControl.getControlNo());

    // Entry
    EntryTestDataProvider entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    // E-Card Station
    ECardStationTestDataProvider eCardStation = new ECardStationTestDataProvider();

    // Punch Session including Times
    DownloadedECardTestDataProvider download = new DownloadedECardTestDataProvider(event.getEventNr(), entry.getRaceNr(), entry.getECardNr(), eCardStation.getECardStationNr());
    Assert.assertNotNull(download.getForm().getPunchSessionNr());

    // Validate (missing controls)
    BEANS.get(IRaceService.class).validateAndPersistRace(entry.getRaceNr());
    RaceBean race = new RaceBean();
    race.setRaceNr(entry.getRaceNr());
    race = BEANS.get(IRaceProcessService.class).load(race);

    // Assert values
    Assert.assertEquals(race.getStatusUid().longValue(), RaceStatusCodeType.MissingPunchCode.ID);
    Date start = download.getForm().getStartField().getValue();
    Date finish = download.getForm().getFinishField().getValue();
    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(event.getEventNr());
    Assert.assertEquals(race.getLegTime().longValue(), finish.getTime() - start.getTime());
    Assert.assertEquals(race.getLegStartTime().longValue(), start.getTime() - evtZero.getTime());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, getRaceControlStatus(entry.getRaceNr(), startControl.getControlNo()).longValue());
    Assert.assertEquals(ControlStatusCodeType.MissingCode.ID, getRaceControlStatus(entry.getRaceNr(), control.getControlNo()).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, getRaceControlStatus(entry.getRaceNr(), finishControl.getControlNo()).longValue());

    // Delete
    event.remove();
    eCardStation.remove();
    entry.remove();
  }

  @Test
  public void testValidateRaceOk() throws ProcessingException {

    // Event
    EventWithIndividualClassTestDataProvider event = new EventWithIndividualClassTestDataProvider();

    // Control
    CourseControlTestDataProvider startControl = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.StartCode.ID, 1L, "S");
    CourseControlTestDataProvider control = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.ControlCode.ID, 2L, "31");
    CourseControlTestDataProvider finishControl = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.FinishCode.ID, 3L, "Z");
    Assert.assertNotNull(startControl.getControlNo());
    Assert.assertNotNull(control.getControlNo());
    Assert.assertNotNull(finishControl.getControlNo());

    // Entry
    EntryTestDataProvider entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    // E-Card Station
    ECardStationTestDataProvider eCardStation = new ECardStationTestDataProvider();

    // Punch Session including Times
    DownloadedECardTestDataProvider download = new DownloadedECardTestDataProvider(event.getEventNr(), entry.getRaceNr(), entry.getECardNr(), eCardStation.getECardStationNr());
    Assert.assertNotNull(download.getForm().getPunchSessionNr());
    Date start = download.getForm().getStartField().getValue();
    Date finish = download.getForm().getFinishField().getValue();

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
    RaceBean race = new RaceBean();
    race.setRaceNr(entry.getRaceNr());
    race = BEANS.get(IRaceProcessService.class).load(race);

    // Assert values
    Assert.assertEquals(race.getStatusUid().longValue(), RaceStatusCodeType.OkCode.ID);
    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(event.getEventNr());
    Assert.assertEquals(race.getLegTime().longValue(), finish.getTime() - start.getTime());
    Assert.assertEquals(race.getLegStartTime().longValue(), start.getTime() - evtZero.getTime());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, getRaceControlStatus(entry.getRaceNr(), startControl.getControlNo()).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, getRaceControlStatus(entry.getRaceNr(), control.getControlNo()).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, getRaceControlStatus(entry.getRaceNr(), finishControl.getControlNo()).longValue());

    // Delete
    event.remove();
    eCardStation.remove();
    entry.remove();
  }

  @Test
  public void testValidateRaceWrongOrder() throws ProcessingException {

    // Event
    EventWithIndividualClassTestDataProvider event = new EventWithIndividualClassTestDataProvider();

    // Control
    CourseControlTestDataProvider startControl = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.StartCode.ID, 1L, "S");
    CourseControlTestDataProvider control1 = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.ControlCode.ID, 2L, "31");
    CourseControlTestDataProvider control2 = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.ControlCode.ID, 3L, "32");
    CourseControlTestDataProvider finishControl = new CourseControlTestDataProvider(event.getEventNr(), event.getCourseNr(), ControlTypeCodeType.FinishCode.ID, 4L, "Z");
    Assert.assertNotNull(startControl.getControlNo());
    Assert.assertNotNull(control1.getControlNo());
    Assert.assertNotNull(control2.getControlNo());
    Assert.assertNotNull(finishControl.getControlNo());

    // Entry
    EntryTestDataProvider entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    // E-Card Station
    ECardStationTestDataProvider eCardStation = new ECardStationTestDataProvider();

    // Punch Session including Times
    DownloadedECardTestDataProvider download = new DownloadedECardTestDataProvider(event.getEventNr(), entry.getRaceNr(), entry.getECardNr(), eCardStation.getECardStationNr());
    Assert.assertNotNull(download.getForm().getPunchSessionNr());
    Date start = download.getForm().getStartField().getValue();
    Date finish = download.getForm().getFinishField().getValue();

    // Punch Form
    PunchTestDataProvider punch1 = new PunchTestDataProvider(download.getPunchSessionNr(), event.getEventNr(), start, "32", 1L);
    PunchTestDataProvider punch2 = new PunchTestDataProvider(download.getPunchSessionNr(), event.getEventNr(), start, "31", 2L);

    // Validate (Wrong Order)
    BEANS.get(IRaceService.class).validateAndPersistRace(entry.getRaceNr());
    RaceBean race = new RaceBean();
    race.setRaceNr(entry.getRaceNr());
    race = BEANS.get(IRaceProcessService.class).load(race);

    // Assert values
    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, race.getStatusUid().longValue());
    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(event.getEventNr());
    Assert.assertEquals(race.getLegTime().longValue(), finish.getTime() - start.getTime());
    Assert.assertEquals(race.getLegStartTime().longValue(), start.getTime() - evtZero.getTime());

    // Assert control status
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, getRaceControlStatus(entry.getRaceNr(), startControl.getControlNo()).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, getRaceControlStatus(entry.getRaceNr(), punch1.getControlNo(), 1).longValue());
    Assert.assertEquals(ControlStatusCodeType.MissingCode.ID, getRaceControlStatus(entry.getRaceNr(), punch2.getControlNo()).longValue());
    Assert.assertEquals(ControlStatusCodeType.WrongCode.ID, getRaceControlStatus(entry.getRaceNr(), punch2.getControlNo(), 2).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, getRaceControlStatus(entry.getRaceNr(), finishControl.getControlNo()).longValue());

    // Delete
    event.remove();
    eCardStation.remove();
    entry.remove();
  }

  @Test
  public void testValidation() throws Exception {
    RaceControlForm form = new RaceControlForm();
    form.startNew();
    Assert.assertFalse("Not manual status", form.getManualStatusField().getValue());
    ScoutClientAssert.assertDisabled(form.getControlStatusField());
    form.getManualStatusField().setValue(true);
    ScoutClientAssert.assertEnabled(form.getControlStatusField());
    form.getManualStatusField().setValue(false);
    ScoutClientAssert.assertDisabled(form.getControlStatusField());
    form.doClose();
  }

  private Long getRaceControlStatus(Long race, String controlNo) throws ProcessingException {
    return getRaceControlStatus(race, controlNo, 1);
  }

  private Long getRaceControlStatus(Long race, String controlNo, int requestedCount) throws ProcessingException {
    int controlCounter = 0;
    Assert.assertNotNull(race);
    Assert.assertNotNull(controlNo);
    RaceControlsTablePage raceControl = new RaceControlsTablePage(race);
    raceControl.loadChildren();
    for (int i = 0; i < raceControl.getTable().getRowCount(); i++) {
      if (raceControl.getTable().getControlColumn().getValue(i).equals(controlNo)) {
        controlCounter++;
        if (controlCounter >= requestedCount) {
          return raceControl.getTable().getControlStatusColumn().getValue(i);
        }
      }
    }
    return null;
  }

}
