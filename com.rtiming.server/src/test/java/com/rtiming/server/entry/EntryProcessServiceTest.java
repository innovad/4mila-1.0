package com.rtiming.server.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.common.database.sql.EntryBean;
import com.rtiming.shared.common.database.sql.ParticipationBean;
import com.rtiming.shared.dao.RtEntry;
import com.rtiming.shared.dao.RtEntryKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtParticipation;
import com.rtiming.shared.dao.RtParticipationKey;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceKey;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunnerKey;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class EntryProcessServiceTest {

  private RtEvent event;
  private RtEntry entry;
  private RtParticipation participation;
  private RtRace race;

  @Test
  public void testDelete1() throws ProcessingException {
    EntryProcessService svc = new EntryProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    EntryProcessService svc = new EntryProcessService();
    svc.delete(new EntryBean());
  }

  @Test
  public void testDelete3() throws ProcessingException {
    EntryProcessService svc = new EntryProcessService();
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    entry = new RtEntry();
    entry.setId(RtEntryKey.create((Long) null));
    JPA.merge(entry);

    RtRunner runner = new RtRunner();
    runner.setId(RtRunnerKey.create((Long) null));
    runner.setActive(true);
    JPA.merge(runner);

    participation = new RtParticipation();
    RtParticipationKey key = new RtParticipationKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setEntryNr(entry.getId().getId());
    key.setEventNr(event.getId().getId());
    participation.setId(key);
    JPA.merge(participation);

    race = new RtRace();
    race.setId(RtRaceKey.create((Long) null));
    race.setEntryNr(entry.getId().getId());
    race.setManualStatus(false);
    race.setRunnerNr(runner.getId().getId());
    JPA.merge(race);

    EntryBean bean = new EntryBean();
    bean.setEntryNr(entry.getId().getId());
    bean = svc.load(bean);
    svc.delete(bean);

    RtEntry find = JPA.find(RtEntry.class, entry.getId());
    assertNull("deleted", find);

    JPA.remove(event);
    JPA.remove(runner);
  }

  @Test
  public void testStoreParticipation1() throws Exception {
    EntryProcessService svc = new EntryProcessService();
    svc.storeParticipation(null, null);
  }

  @Test
  public void testStoreParticipation2() throws Exception {
    EntryProcessService svc = new EntryProcessService();
    svc.storeParticipation(new EntryBean(), null);
  }

  @Test
  public void testStoreParticipation3() throws Exception {
    EntryProcessService svc = new EntryProcessService();
    List<Long> list = new ArrayList<>();
    svc.storeParticipation(new EntryBean(), list);
  }

  @Test
  public void testStoreParticipation4() throws Exception {
    EntryProcessService svc = new EntryProcessService();
    List<Long> list = new ArrayList<>();
    list.add(7L);
    svc.storeParticipation(new EntryBean(), list);
  }

  @Test
  public void testStoreParticipation5() throws Exception {
    EntryProcessService svc = new EntryProcessService();
    createEventWithEntry(false);

    EntryBean bean = new EntryBean();
    bean.setEntryNr(entry.getId().getId());
    List<Long> list = new ArrayList<>();
    list.add(event.getId().getId());
    svc.storeParticipation(bean, list);

    RtParticipation find = JPA.find(RtParticipation.class, participation.getId());
    assertNotNull("NOT deleted", find);

    List<Long> list2 = new ArrayList<>();
    list2.add(-2L);
    svc.storeParticipation(bean, list2);

    find = JPA.find(RtParticipation.class, participation.getId());
    assertNull("deleted", find);
  }

  @Test
  public void testStoreParticipation6() throws Exception {
    EntryProcessService svc = new EntryProcessService();
    createEventWithEntry(true);

    EntryBean bean = new EntryBean();
    bean.setEntryNr(entry.getId().getId());
    ParticipationBean p = new ParticipationBean();
    p.setEntryNr(bean.getEntryNr());
    p.setEventNr(event.getId().getId());
    p.setStartTime(888L);
    bean.getParticipations().add(p);
    List<Long> list = new ArrayList<>();
    list.add(event.getId().getId());
    svc.storeParticipation(bean, list);

    RtParticipation find = JPA.find(RtParticipation.class, participation.getId());
    Assert.assertEquals("updated", 888, find.getStartTime().longValue());
  }

  private void createEventWithEntry(boolean withRace) throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    entry = new RtEntry();
    entry.setId(RtEntryKey.create((Long) null));
    JPA.merge(entry);

    participation = new RtParticipation();
    RtParticipationKey key = new RtParticipationKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setEntryNr(entry.getId().getId());
    key.setEventNr(event.getId().getId());
    participation.setId(key);
    JPA.merge(participation);

    if (withRace) {
      race = new RtRace();
      race.setId(RtRaceKey.create((Long) null));
      race.setEventNr(event.getId().getId());
      race.setEntryNr(entry.getId().getId());
      race.setManualStatus(false);
      JPA.merge(race);
    }
  }

  @Test
  public void testStoreEntry1() throws Exception {
    EntryProcessService svc = new EntryProcessService();
    svc.storeEntry(null);
  }

  @Test
  public void testStoreEntry2() throws Exception {
    EntryProcessService svc = new EntryProcessService();
    svc.storeEntry(new EntryBean());
  }

  @Test
  public void testStoreEntry3() throws Exception {
    Date evtEntry = new Date();
    entry = new RtEntry();
    entry.setId(RtEntryKey.create((Long) null));
    JPA.merge(entry);

    EntryProcessService svc = new EntryProcessService();
    EntryBean bean = new EntryBean();
    bean.setEntryNr(entry.getId().getId());
    bean.setEvtEntry(evtEntry);
    svc.storeEntry(bean);

    RtEntry find = JPA.find(RtEntry.class, entry.getId());
    Assert.assertEquals("updated", evtEntry, find.getEvtEntry());

    JPA.remove(entry);
  }

  @Test
  public void testLoad() throws Exception {
    createEventWithEntry(true);
    EntryProcessService svc = new EntryProcessService();
    EntryBean bean = new EntryBean();
    bean.setEntryNr(entry.getId().getId());
    bean = svc.load(bean);
    assertEquals("participation loaded", 1, bean.getParticipations().size());
    assertEquals("races loaded", 1, bean.getRaces().size());
  }

  @Test
  public void testStoreRaces() throws Exception {
    createEventWithEntry(true);
    EntryProcessService svc = new EntryProcessService();
    EntryBean bean = new EntryBean();
    bean.setEntryNr(entry.getId().getId());
    bean = svc.load(bean);
    List<Long> result = svc.storeRaces(bean);
    assertEquals("size", 1, result.size());
  }

  @After
  public void after() throws ProcessingException {
    if (race != null && JPA.find(RtRace.class, race.getId()) != null) {
      JPA.remove(race);
    }
    if (participation != null && JPA.find(RtParticipation.class, participation.getId()) != null) {
      JPA.remove(participation);
    }
    if (entry != null && JPA.find(RtEntry.class, entry.getId()) != null) {
      JPA.remove(entry);
    }
    if (event != null && JPA.find(RtEvent.class, event.getId()) != null) {
      JPA.remove(event);
    }
  }

}
