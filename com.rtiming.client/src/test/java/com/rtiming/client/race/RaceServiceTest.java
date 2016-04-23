package com.rtiming.client.race;

import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.entry.startlist.StartlistSettingForm;
import com.rtiming.client.result.ResultsTablePage;
import com.rtiming.client.test.data.DownloadedECardTestDataProvider;
import com.rtiming.client.test.data.ECardStationTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.client.test.data.EventWithTeamCombinedCourseClassTestDataProvider;
import com.rtiming.shared.common.database.sql.EntryBean;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.entry.IEntryProcessService;
import com.rtiming.shared.entry.startlist.IStartlistService;
import com.rtiming.shared.entry.startlist.StartlistTypeCodeType;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.RaceControlRowData;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class RaceServiceTest {

  private EventTestDataProvider event;
  private ECardStationTestDataProvider station;
  private DownloadedECardTestDataProvider download;

  @Test
  public void testTeamValidation() throws Exception {
    event = new EventWithTeamCombinedCourseClassTestDataProvider(3L, 3L);
    EventWithTeamCombinedCourseClassTestDataProvider teamEvent = (EventWithTeamCombinedCourseClassTestDataProvider) event;
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());

    EntryTestDataProvider entry = new EntryTestDataProvider(event.getEventNr(), teamEvent.getClassUid(), teamEvent.getClassUid(), teamEvent.getClassUid());

    Long firstRaceNr = entry.getRaceNrs()[0];

    station = new ECardStationTestDataProvider();
    download = new DownloadedECardTestDataProvider(event.getEventNr(), firstRaceNr, entry.getECardNrs()[0], station.getECardStationNr());

    BEANS.get(IRaceService.class).validateAndPersistRace(firstRaceNr);

    ResultsTablePage results = new ResultsTablePage(ClientSession.get().getSessionClientNr(), teamEvent.getClassUid(), null, null);
    results.nodeAddedNotify();
    results.loadChildren();

    Assert.assertEquals("3 Runners", 3, results.getTable().getRowCount());
    Assert.assertEquals("Race Status Set", RaceStatusCodeType.OkCode.ID, results.getTable().getRaceStatusColumn().getValue(0).longValue());
    Assert.assertEquals("Race Status Set", RaceStatusCodeType.OkCode.ID, results.getTable().getRaceStatusColumn().getValue(1).longValue());
    Assert.assertEquals("Race Status Set", RaceStatusCodeType.OkCode.ID, results.getTable().getRaceStatusColumn().getValue(2).longValue());
  }

  @Test
  public void testRaceReset() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    EventWithIndividualValidatedRaceTestDataProvider validatedEvent = (EventWithIndividualValidatedRaceTestDataProvider) event;
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());

    BEANS.get(IRaceService.class).reset(validatedEvent.getRaceNr());

    RaceBean race = new RaceBean();
    race.setRaceNr(validatedEvent.getRaceNr());
    race = BEANS.get(IRaceProcessService.class).load(race);

    Assert.assertNull("Start Time", race.getLegStartTime());
    Assert.assertNull("Leg Time", race.getLegTime());
    Assert.assertNull("Status", race.getStatusUid());

    List<RaceControlRowData> data = BEANS.get(IEventsOutlineService.class).getRaceControlTableData(ClientSession.get().getSessionClientNr(), race.getRaceNr());
    Assert.assertEquals("Race Controls", 3, data.size());

    EntryBean entry = new EntryBean();
    entry.setEntryNr(validatedEvent.getEntryNr());
    entry = BEANS.get(IEntryProcessService.class).load(entry);
    Assert.assertNull("Start Time", entry.getParticipations().get(0).getStartTime());
  }

  @Test
  public void testRaceResetWithStarttime() throws Exception {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    EventWithIndividualValidatedRaceTestDataProvider validatedEvent = (EventWithIndividualValidatedRaceTestDataProvider) event;
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());

    RaceBean race1 = new RaceBean();
    race1.setRaceNr(validatedEvent.getRaceNr());
    race1 = BEANS.get(IRaceProcessService.class).load(race1);

    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(race1.getLegClassUid());
    form.setParticipationCount(1L);
    form.setEventNr(event.getEventNr());
    form.startNew();
    form.getTypeUidField().setValue(StartlistTypeCodeType.MassStartCode.ID);
    form.doOk();

    BEANS.get(IStartlistService.class).createStartlists(new Long[]{form.getStartlistSettingNr()});
    RaceBean race2 = new RaceBean();
    race2.setRaceNr(validatedEvent.getRaceNr());
    race2 = BEANS.get(IRaceProcessService.class).load(race2);
    Assert.assertNotNull("Start Time", race2.getLegStartTime());
    Assert.assertNotNull("Leg Time", race2.getLegTime());

    EntryBean entry1 = new EntryBean();
    entry1.setEntryNr(validatedEvent.getEntryNr());
    entry1 = BEANS.get(IEntryProcessService.class).load(entry1);
    Assert.assertNotNull("Start Time", entry1.getParticipations().get(0).getStartTime());

    BEANS.get(IRaceService.class).reset(validatedEvent.getRaceNr());

    RaceBean race3 = new RaceBean();
    race3.setRaceNr(validatedEvent.getRaceNr());
    race3 = BEANS.get(IRaceProcessService.class).load(race3);

    Assert.assertNotNull("Leg Start Time", race3.getLegStartTime());
    Assert.assertEquals("Leg Start time did not change", race2.getLegStartTime(), race3.getLegStartTime());
    Assert.assertNull("Leg Time", race3.getLegTime());
    Assert.assertNull("Status", race3.getStatusUid());

    List<RaceControlRowData> data = BEANS.get(IEventsOutlineService.class).getRaceControlTableData(ClientSession.get().getSessionClientNr(), race3.getRaceNr());
    Assert.assertEquals("Race Controls", 3, data.size());

    EntryBean entry2 = new EntryBean();
    entry2.setEntryNr(validatedEvent.getEntryNr());
    entry2 = BEANS.get(IEntryProcessService.class).load(entry2);
    Assert.assertNotNull("Start Time", entry2.getParticipations().get(0).getStartTime());
    Assert.assertEquals("Start time did not change", entry2.getParticipations().get(0).getStartTime(), entry1.getParticipations().get(0).getStartTime());
  }

  @After
  public void after() throws ProcessingException {
    if (download != null) {
      download.remove();
    }
    if (event != null) {
      event.remove();
    }
    if (station != null) {
      station.remove();
    }
  }

}
