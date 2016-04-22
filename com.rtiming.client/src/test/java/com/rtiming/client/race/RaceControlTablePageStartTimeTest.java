package com.rtiming.client.race;

import java.util.Date;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.entry.startlist.StartlistSettingForm;
import com.rtiming.client.test.data.DownloadedECardTestDataProvider;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.entry.startlist.IStartlistService;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.IRaceService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class RaceControlTablePageStartTimeTest {

  private EventWithIndividualClassTestDataProvider event;
  private ECardStationTestDataProvider eCardStation;
  private EntryTestDataProvider entry;
  private DownloadedECardTestDataProvider download;

  @Test
  public void testStartWithECard() throws Exception {
    createTestData(555);

    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(event.getEventNr());
    eventClass.getClazz().setValue(event.getClassUid());
    eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);

    Assert.assertNull("No Startlist", eventClass.getStartlistSettingNr());

    BEANS.get(IRaceService.class).validateAndPersistRace(entry.getRaceNr());

    RaceBean race = new RaceBean();
    race.setRaceNr(entry.getRaceNr());
    race = BEANS.get(IRaceProcessService.class).load(race);

    Assert.assertEquals("Start Time", 555000, race.getLegStartTime().longValue());
  }

  @Test
  public void testStartWithECardButNoStartPunch() throws Exception {
    createTestData(null);

    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(event.getEventNr());
    eventClass.getClazz().setValue(event.getClassUid());
    eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);

    Assert.assertNull("No Startlist", eventClass.getStartlistSettingNr());

    BEANS.get(IRaceService.class).validateAndPersistRace(entry.getRaceNr());

    RaceBean race = new RaceBean();
    race.setRaceNr(entry.getRaceNr());
    race = BEANS.get(IRaceProcessService.class).load(race);

    Assert.assertEquals("Start Time", null, race.getLegStartTime());
  }

  @Test
  public void testStartWithStartlist() throws Exception {
    createTestData(555);

    StartlistSettingForm startlistSetting = new StartlistSettingForm();
    startlistSetting.setNewClassUid(event.getClassUid());
    startlistSetting.setParticipationCount(1L);
    startlistSetting.setEventNr(event.getEventNr());
    startlistSetting.startNew();
    Date firstStart = new Date();
    firstStart.setTime(startlistSetting.getFirstStartField().getValue().getTime() + 120000);
    startlistSetting.getFirstStartField().setValue(firstStart);
    startlistSetting.doOk();

    BEANS.get(IStartlistService.class).createStartlists(new Long[]{startlistSetting.getStartlistSettingNr()});

    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(event.getEventNr());
    eventClass.getClazz().setValue(event.getClassUid());
    eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);

    Assert.assertNotNull("Startlist Exists", eventClass.getStartlistSettingNr());

    BEANS.get(IRaceService.class).validateAndPersistRace(entry.getRaceNr());

    RaceBean race = new RaceBean();
    race.setRaceNr(entry.getRaceNr());
    race = BEANS.get(IRaceProcessService.class).load(race);

    Assert.assertEquals("Start Time", 120000, race.getLegStartTime().longValue());
  }

  @Test
  public void testStartWithStartlistButNoStartTimeSet() throws Exception {
    createTestData(555);

    StartlistSettingForm startlistSetting = new StartlistSettingForm();
    startlistSetting.setNewClassUid(event.getClassUid());
    startlistSetting.setParticipationCount(1L);
    startlistSetting.setEventNr(event.getEventNr());
    startlistSetting.startNew();
    Date firstStart = new Date();
    firstStart.setTime(startlistSetting.getFirstStartField().getValue().getTime() + 120000);
    startlistSetting.getFirstStartField().setValue(firstStart);
    startlistSetting.doOk();

    // do not create startlist => no start time set

    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(event.getEventNr());
    eventClass.getClazz().setValue(event.getClassUid());
    eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);

    Assert.assertNotNull("Startlist Exists", eventClass.getStartlistSettingNr());

    BEANS.get(IRaceService.class).validateAndPersistRace(entry.getRaceNr());

    RaceBean race = new RaceBean();
    race.setRaceNr(entry.getRaceNr());
    race = BEANS.get(IRaceProcessService.class).load(race);

    Assert.assertNull("Start Time", race.getLegStartTime());
  }

  private void createTestData(Integer startTime) throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    eCardStation = new ECardStationTestDataProvider();
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    download = new DownloadedECardTestDataProvider(event.getEventNr(), entry.getRaceNr(), entry.getECardNr(), eCardStation.getECardStationNr(), startTime, 999);
  }

  @After
  public void after() throws ProcessingException {
    download.remove();
    event.remove();
    eCardStation.remove();
    entry.remove();
  }

}
