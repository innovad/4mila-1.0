package com.rtiming.client.race;

import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.event.EventForm;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.ecard.download.PunchingSystemCodeType;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.race.RaceStatusCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class RaceFormValidationTest {

  private EventWithIndividualValidatedRaceTestDataProvider event;
  private EventTestDataProvider event2;

  @Test
  public void testFieldStatusSportIdent() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"}, 200, 1200, new Integer[]{500});
    setPunchingSystemUid(PunchingSystemCodeType.SportIdentCode.ID);

    RaceForm race = new RaceForm();
    race.setRaceNr(event.getRaceNr());
    race.startModify();

    ScoutClientAssert.assertDisabled(race.getLegClassUidField());
    ScoutClientAssert.assertDisabled(race.getRunnerNrField());

    ScoutClientAssert.assertDisabled(race.getLegStartTimeField());
    ScoutClientAssert.assertDisabled(race.getLegFinishTimeField());
    ScoutClientAssert.assertDisabled(race.getLegTimeField());
    ScoutClientAssert.assertDisabled(race.getRaceStatusField());

    ScoutClientAssert.assertEnabled(race.getECardNrField());
    ScoutClientAssert.assertEnabled(race.getNationField());
    ScoutClientAssert.assertEnabled(race.getBibNumberField());
    ScoutClientAssert.assertEnabled(race.getClubField());

    Assert.assertFalse("Manual Status not selected", race.getManualStatusField().getValue());

    race.getManualStatusField().setValue(true);

    ScoutClientAssert.assertEnabled(race.getLegStartTimeField());
    ScoutClientAssert.assertEnabled(race.getLegFinishTimeField());
    ScoutClientAssert.assertDisabled(race.getLegTimeField());
    ScoutClientAssert.assertEnabled(race.getRaceStatusField());
    ScoutClientAssert.assertEnabled(race.getManualStatusField());

    race.getManualStatusField().setValue(false);

    ScoutClientAssert.assertDisabled(race.getLegStartTimeField());
    ScoutClientAssert.assertDisabled(race.getLegFinishTimeField());
    ScoutClientAssert.assertDisabled(race.getLegTimeField());
    ScoutClientAssert.assertDisabled(race.getRaceStatusField());
  }

  @Test
  public void testFieldStatusPunchingSystemNone() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"}, 200, 1200, new Integer[]{500});
    setPunchingSystemUid(PunchingSystemCodeType.PunchingSystemNoneCode.ID);

    RaceForm race = new RaceForm();
    race.setRaceNr(event.getRaceNr());
    race.startModify();

    ScoutClientAssert.assertDisabled(race.getLegClassUidField());
    ScoutClientAssert.assertDisabled(race.getRunnerNrField());

    ScoutClientAssert.assertEnabled(race.getLegStartTimeField());
    ScoutClientAssert.assertEnabled(race.getLegFinishTimeField());
    ScoutClientAssert.assertDisabled(race.getLegTimeField());
    ScoutClientAssert.assertEnabled(race.getRaceStatusField());
    ScoutClientAssert.assertDisabled(race.getManualStatusField());

    ScoutClientAssert.assertEnabled(race.getECardNrField());
    ScoutClientAssert.assertEnabled(race.getNationField());
    ScoutClientAssert.assertEnabled(race.getBibNumberField());
    ScoutClientAssert.assertEnabled(race.getClubField());

    Assert.assertTrue("Manual Status selected", race.getManualStatusField().getValue());
  }

  @Test
  public void testTimeDisplay() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"}, 200, 1200, new Integer[]{500});
    setPunchingSystemUid(PunchingSystemCodeType.PunchingSystemNoneCode.ID);

    RaceForm race = new RaceForm();
    race.setRaceNr(event.getRaceNr());
    race.startModify();

    Assert.assertEquals("Leg Time", 1000 * 1000, race.getRawLegTime().longValue());
    Assert.assertEquals("Leg Start Time", 200 * 1000, race.getRawLegStartTime().longValue());

    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(event.getEventNr());
    Long zero = evtZero.getTime();

    Assert.assertEquals("Leg Finish Time Date", zero + 1200 * 1000, race.getLegFinishTimeField().getValue().getTime());
    Assert.assertEquals("Leg Start Time Date", zero + 200 * 1000, race.getLegStartTimeField().getValue().getTime());
    Assert.assertEquals("Leg Time formatted", "16:40", race.getLegTimeField().getValue());
  }

  @Test
  public void testTimeCalculation() throws Exception {
    event2 = new EventTestDataProvider();

    RaceForm race = new RaceForm();
    race.getEventNrField().setValue(event2.getEventNr());
    race.startNew();

    race.getManualStatusField().setValue(true);

    // start and finish required for time
    Date base = new Date();
    Date finish = DateUtility.addHours(base, 1);
    Assert.assertNull("Time empty", race.getLegTimeField().getValue());
    race.getLegStartTimeField().setValue(base);
    Assert.assertNull("Time empty", race.getLegTimeField().getValue());
    race.getLegFinishTimeField().setValue(finish);
    Assert.assertNotNull("Time NOT empty", race.getLegTimeField().getValue());
    race.getLegStartTimeField().setValue(null);
    Assert.assertNull("Time empty", race.getLegTimeField().getValue());
    race.getLegStartTimeField().setValue(base);
    Assert.assertNotNull("Time NOT empty", race.getLegTimeField().getValue());
    race.getLegFinishTimeField().setValue(null);
    Assert.assertNull("Time empty", race.getLegTimeField().getValue());

    // same time
    race.getLegStartTimeField().setValue(base);
    race.getLegFinishTimeField().setValue(base);
    Assert.assertEquals("Formatted Time", "0:00", race.getLegTimeField().getValue());

    // negative time
    finish = FMilaUtility.addMilliSeconds(base, -1L);
    race.getLegFinishTimeField().setValue(finish);
    Assert.assertTrue("Formatted Time", StringUtility.isNullOrEmpty(race.getLegTimeField().getValue()));

    // time
    finish = FMilaUtility.addSeconds(base, 3600);
    race.getLegFinishTimeField().setValue(finish);
    Assert.assertEquals("Formatted Time", "1:00:00", race.getLegTimeField().getValue());
  }

  @Test
  public void testRaceStatus() throws Exception {
    event2 = new EventTestDataProvider();

    RaceForm race = new RaceForm();
    race.getEventNrField().setValue(event2.getEventNr());
    race.startNew();

    race.getManualStatusField().setValue(true);

    race.getRaceStatusField().setValue(null);
    ScoutClientAssert.assertNonMandatory(race.getLegStartTimeField());
    ScoutClientAssert.assertNonMandatory(race.getLegFinishTimeField());

    race.getRaceStatusField().setValue(RaceStatusCodeType.OkCode.ID);
    ScoutClientAssert.assertMandatory(race.getLegStartTimeField());
    ScoutClientAssert.assertMandatory(race.getLegFinishTimeField());

    race.getRaceStatusField().setValue(RaceStatusCodeType.DisqualifiedCode.ID);
    ScoutClientAssert.assertNonMandatory(race.getLegStartTimeField());
    ScoutClientAssert.assertNonMandatory(race.getLegFinishTimeField());

    race.getRaceStatusField().setValue(RaceStatusCodeType.DidNotFinishCode.ID);
    ScoutClientAssert.assertNonMandatory(race.getLegStartTimeField());
    ScoutClientAssert.assertNonMandatory(race.getLegFinishTimeField());
  }

  @Test
  public void testTimeStore() throws ProcessingException {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"}, 200, 1200, new Integer[]{500});
    setPunchingSystemUid(PunchingSystemCodeType.PunchingSystemNoneCode.ID);

    RaceForm race = new RaceForm();
    race.setRaceNr(event.getRaceNr());
    race.startModify();

    Assert.assertEquals("Leg Time", 1000 * 1000, race.getRawLegTime().longValue());
    Assert.assertEquals("Leg Start Time", 200 * 1000, race.getRawLegStartTime().longValue());

    race.getRaceStatusField().setValue(RaceStatusCodeType.DisqualifiedCode.ID); // trigger store
    Assert.assertTrue("Save Needed", race.isSaveNeeded());
    race.doOk();

    race = new RaceForm();
    race.setRaceNr(event.getRaceNr());
    race.startModify();

    // assert store conversion does not affect values
    Assert.assertEquals("Leg Time", 1000 * 1000, race.getRawLegTime().longValue());
    Assert.assertEquals("Leg Start Time", 200 * 1000, race.getRawLegStartTime().longValue());
  }

  @Test
  public void testTimeStoreNull() throws ProcessingException {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"}, 200, 1200, new Integer[]{500});
    setPunchingSystemUid(PunchingSystemCodeType.PunchingSystemNoneCode.ID);

    RaceForm race = new RaceForm();
    race.setRaceNr(event.getRaceNr());
    race.startModify();

    Assert.assertEquals("Leg Time", 1000 * 1000, race.getRawLegTime().longValue());
    Assert.assertEquals("Leg Start Time", 200 * 1000, race.getRawLegStartTime().longValue());
    race.getManualStatusField().setValue(true);
    race.getRaceStatusField().setValue(RaceStatusCodeType.DisqualifiedCode.ID);
    race.getLegStartTimeField().setValue(null);
    race.doOk();

    race = new RaceForm();
    race.setRaceNr(event.getRaceNr());
    race.startModify();

    // assert store null
    Assert.assertNull("Leg Time", race.getRawLegTime());
    Assert.assertNull("Leg Time", race.getLegTimeField().getValue());
    Assert.assertNull("Leg Start Time", race.getRawLegStartTime());
    Assert.assertNull("Leg Start Time", race.getLegStartTimeField().getValue());
  }

  @Test
  public void testSetStartTime() throws Exception {
    event2 = new EventTestDataProvider();

    RaceForm race = new RaceForm();
    race.getEventNrField().setValue(event2.getEventNr());
    race.startNew();

    Assert.assertNull("Start Time is Null", race.getLegStartTimeField().getValue());
    race.getLegStartTimeField().setTimeValue(7777d);
    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(event2.getEventNr());

    Assert.assertTrue("Same day as evt zero", DateUtility.isSameDay(race.getLegStartTimeField().getValue(), evtZero));
  }

  @Test
  public void testSetFinishTime() throws Exception {
    event2 = new EventTestDataProvider();

    RaceForm race = new RaceForm();
    race.getEventNrField().setValue(event2.getEventNr());
    race.startNew();

    Assert.assertNull("Finish Time is Null", race.getLegFinishTimeField().getValue());
    race.getLegFinishTimeField().setTimeValue(7777d);
    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(event2.getEventNr());

    Assert.assertTrue("Same day as evt zero", DateUtility.isSameDay(race.getLegFinishTimeField().getValue(), evtZero));
  }

  private void setPunchingSystemUid(long id) throws ProcessingException {
    EventForm eventForm = new EventForm();
    eventForm.setEventNr(event.getEventNr());
    eventForm.startModify();
    eventForm.getPunchingSystemField().setValue(id);
    eventForm.doOk();
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
    if (event2 != null) {
      event2.remove();
    }
  }

}
