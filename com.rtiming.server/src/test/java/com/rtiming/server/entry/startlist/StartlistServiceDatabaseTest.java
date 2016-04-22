package com.rtiming.server.entry.startlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.event.EventProcessService;
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourseKey;
import com.rtiming.shared.dao.RtEntry;
import com.rtiming.shared.dao.RtEntryKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtEventStartblock;
import com.rtiming.shared.dao.RtEventStartblockKey;
import com.rtiming.shared.dao.RtParticipation;
import com.rtiming.shared.dao.RtParticipationKey;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceKey;
import com.rtiming.shared.dao.RtStartlistSetting;
import com.rtiming.shared.dao.RtStartlistSettingKey;
import com.rtiming.shared.dao.RtStartlistSettingVacant;
import com.rtiming.shared.dao.RtStartlistSettingVacantKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.entry.startblock.StartblockCodeType;
import com.rtiming.shared.entry.startlist.IStartlistSettingProcessService;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData;
import com.rtiming.shared.event.course.ClassCodeType;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class StartlistServiceDatabaseTest {

  private RtEvent event;
  private RtEntry entry;
  private RtParticipation participation;
  private RtRace race;
  private RtEventClass eventClass;
  private RtUc clazz;
  private RtStartlistSetting setting;
  private RtCourse course;

  @Test(expected = IllegalArgumentException.class)
  public void testGetMaxBibNoFromDatabase1() throws Exception {
    StartlistService svc = new StartlistService();
    svc.getMaxBibNoFromDatabase(null, null, null);
  }

  @Test
  public void testGetMaxBibNoFromDatabase2() throws Exception {
    StartlistService svc = new StartlistService();
    List<StartlistSettingBean> list = new ArrayList<>();
    Long result = svc.getMaxBibNoFromDatabase(null, null, list);
    assertEquals("max bib no", 0, result.longValue());
  }

  @Test
  public void testGetMaxBibNoFromDatabase3() throws Exception {
    createEventWithRace(true, true);
    StartlistService svc = new StartlistService();
    List<StartlistSettingBean> list = new ArrayList<>();
    Long result = svc.getMaxBibNoFromDatabase(event.getId().getId(), null, list);
    assertEquals("max bib no", 12345, result.longValue());
  }

  @Test
  public void testGetMaxBibNoFromDatabase4() throws Exception {
    createEventWithRace(true, true);
    StartlistService svc = new StartlistService();
    List<StartlistSettingBean> list = new ArrayList<>();
    Long result = svc.getMaxBibNoFromDatabase(event.getId().getId(), null, list);
    assertEquals("max bib no", 12345, result.longValue());
  }

  @Test
  public void testGetMaxBibNoFromDatabase5() throws Exception {
    createEventWithRace(true, true);
    Long result = doGetMaxBibNoFromDatabaseTest();
    assertEquals("max bib no", 0, result.longValue());
  }

  @Test
  public void testGetMaxBibNoFromDatabase6() throws Exception {
    createEventWithRace(true, false);
    Long result = doGetMaxBibNoFromDatabaseTest();
    assertEquals("max bib no", 0, result.longValue());
  }

  @Test
  public void testGetMaxBibNoFromDatabase7() throws Exception {
    createEventWithRace(false, true);
    Long result = doGetMaxBibNoFromDatabaseTest();
    assertEquals("max bib no", 0, result.longValue());
  }

  @Test
  public void testGetMaxBibNoFromDatabase8() throws Exception {
    createEventWithRace(false, false);
    Long result = doGetMaxBibNoFromDatabaseTest();
    assertEquals("max bib no", 12345, result.longValue());
  }

  @Test
  public void testGetOrderedStartblocksForEvent1() throws Exception {
    StartlistService svc = new StartlistService();
    Long[] result = svc.getOrderedStartblocksForEvent(null);
    assertEquals("result size", 0, result.length);
  }

  @Test
  public void testGetOrderedStartblocksForEvent2() throws Exception {
    event = new RtEvent();
    event.setId(RtEventKey.create(new RtEventKey()));
    JPA.merge(event);
    Long id = createStartblock(7L);

    StartlistService svc = new StartlistService();
    Long[] result = svc.getOrderedStartblocksForEvent(event.getId().getId());
    assertEquals("result size", 1, result.length);
    assertEquals("startblock", id, result[0]);
  }

  @Test
  public void testGetOrderedStartblocksForEvent3() throws Exception {
    event = new RtEvent();
    event.setId(RtEventKey.create(new RtEventKey()));
    JPA.merge(event);
    Long first = createStartblock(9L);
    Long second = createStartblock(5L);

    StartlistService svc = new StartlistService();
    Long[] result = svc.getOrderedStartblocksForEvent(event.getId().getId());
    assertEquals("result size", 2, result.length);
    assertEquals("startblock", second, result[0]);
    assertEquals("startblock", first, result[1]);
  }

  @Test
  public void testStoreStarttimes1() throws Exception {
    StartlistService svc = new StartlistService();
    svc.storeStarttimes(null);
  }

  @Test
  public void testStoreStarttimes2() throws Exception {
    StartlistService svc = new StartlistService();
    List<StartlistSettingBean> allStartlists = new ArrayList<StartlistSettingBean>();
    svc.storeStarttimes(allStartlists);
  }

  @Test
  public void testStoreStarttimes3() throws Exception {
    createEventWithRace(true, false);
    StartlistService svc = new StartlistService();
    List<StartlistSettingBean> allStartlists = new ArrayList<StartlistSettingBean>();
    LinkedList<StartlistParticipationBean> list = new LinkedList<>();
    StartlistParticipationBean partBean = new StartlistParticipationBean();
    partBean.setEntryNr(entry.getId().getId());
    partBean.setEventNr(event.getId().getId());
    partBean.setStartTime(12345L);
    partBean.setBibNo(555L);
    list.add(partBean);
    StartlistSettingBean bean = new StartlistSettingBean(setting.getId().getId(), event.getId().getId(), null, list);
    allStartlists.add(bean);
    svc.storeStarttimes(allStartlists);

    RtParticipation findParticipation = JPA.find(RtParticipation.class, participation.getId());
    assertEquals("stored", 12345, findParticipation.getStartTime().longValue());
    RtRace findRace = JPA.find(RtRace.class, race.getId());
    assertEquals("stored", 12345, findRace.getLegStartTime().longValue());
    assertEquals("stored", "555", findRace.getBibNo());
  }

  @Test
  public void testStoreStarttimes4() throws Exception {
    createEventWithRace(true, false);
    StartlistService svc = new StartlistService();
    List<StartlistSettingBean> allStartlists = new ArrayList<StartlistSettingBean>();
    LinkedList<StartlistParticipationBean> list = new LinkedList<>();
    StartlistParticipationBean partBean = new StartlistParticipationBean();
    partBean.setStartlistSettingNr(setting.getId().getId());
    partBean.setVacant(true);
    partBean.setStartTime(777L);
    partBean.setBibNo(888L);
    list.add(partBean);
    StartlistSettingBean bean = new StartlistSettingBean(setting.getId().getId(), event.getId().getId(), null, list);
    allStartlists.add(bean);
    svc.storeStarttimes(allStartlists);

    RtStartlistSettingVacantKey key = new RtStartlistSettingVacantKey();
    key.setStartlistSettingNr(setting.getId().getId());
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setBibNo("888");
    key.setStartTime(777L);
    RtStartlistSettingVacant vacant = JPA.find(RtStartlistSettingVacant.class, key);
    Assert.assertNotNull("vacant created", vacant);
  }

  @Test
  public void testExistsRaceWithStartTime1() throws Exception {
    StartlistService svc = new StartlistService();
    boolean result = svc.existsRaceWithStartTime(null);
    assertFalse("not exists", result);
  }

  @Test
  public void testExistsRaceWithStartTime2() throws Exception {
    StartlistService svc = new StartlistService();
    boolean result = svc.existsRaceWithStartTime(new Long[]{});
    assertFalse("not exists", result);
  }

  @Test
  public void testExistsRaceWithStartTime3() throws Exception {
    createEventWithRace(true, false);
    StartlistService svc = new StartlistService();
    boolean result = svc.existsRaceWithStartTime(new Long[]{setting.getId().getId()});
    assertFalse("not exists", result);
  }

  @Test
  public void testExistsRaceWithStartTime4() throws Exception {
    createEventWithRace(true, false);
    RtParticipation find = JPA.find(RtParticipation.class, participation.getId());
    find.setStartTime(12345L);
    JPA.merge(find);
    StartlistService svc = new StartlistService();
    boolean result = svc.existsRaceWithStartTime(new Long[]{setting.getId().getId()});
    assertTrue("exists", result);
  }

  @Test
  public void testExistsRaceWithStartTime5() throws Exception {
    createEventWithRace(false, true);
    RtParticipation find = JPA.find(RtParticipation.class, participation.getId());
    find.setStartTime(12345L);
    JPA.merge(find);
    StartlistService svc = new StartlistService();
    boolean result = svc.existsRaceWithStartTime(new Long[]{setting.getId().getId()});
    assertTrue("exists", result);
  }

  @Test
  public void testDrawStartlistBlock1() throws Exception {
    StartlistService svc = new StartlistService();
    List<StartlistParticipationBean> result = svc.drawStartlistBlock(null, null);
    assertEquals("Empty", 0, result.size());
  }

  @Test
  public void testDrawStartlistBlock2() throws Exception {
    createEventWithRace(true, false);
    Long startblockUid = createStartblock(1L);
    RtParticipation find = JPA.find(RtParticipation.class, participation.getId());
    find.setStartblockUid(startblockUid);
    JPA.merge(find);

    StartlistService svc = new StartlistService();
    StartlistSettingFormData formData = new StartlistSettingFormData();
    formData.setStartlistSettingNr(setting.getId().getId());
    formData = BEANS.get(IStartlistSettingProcessService.class).load(formData);
    List<StartlistParticipationBean> result = svc.drawStartlistBlock(startblockUid, formData);
    assertEquals("1 result", 1, result.size());
    assertEquals("entry", entry.getId().getId(), result.get(0).getEntryNr());
  }

  private Long createStartblock(Long sortcode) throws ProcessingException {
    RtUc startblock = new RtUc();
    startblock.setCodeType(StartblockCodeType.ID);
    startblock.setActive(true);
    startblock.setId(RtUcKey.create((Long) null));
    JPA.merge(startblock);

    RtEventStartblock eventStartblock = new RtEventStartblock();
    RtEventStartblockKey key = new RtEventStartblockKey();
    key.setEventNr(event.getId().getId());
    key.setId(startblock.getId().getId());
    eventStartblock.setId(RtEventStartblockKey.create(key));
    eventStartblock.setSortcode(sortcode);
    JPA.merge(eventStartblock);

    return startblock.getId().getId();
  }

  private Long doGetMaxBibNoFromDatabaseTest() throws ProcessingException {
    StartlistService svc = new StartlistService();
    List<StartlistSettingBean> list = new ArrayList<>();
    StartlistSettingBean bean = new StartlistSettingBean(setting.getId().getId(), event.getId().getId(), null, null);
    list.add(bean);
    Long result = svc.getMaxBibNoFromDatabase(event.getId().getId(), null, list);
    return result;
  }

  private void createEventWithRace(boolean startlistSettingForEventClass, boolean startlistSettingForCourse) throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    entry = new RtEntry();
    entry.setId(RtEntryKey.create((Long) null));
    JPA.merge(entry);

    clazz = new RtUc();
    clazz.setActive(true);
    clazz.setCodeType(ClassCodeType.ID);
    clazz.setId(RtUcKey.create((Long) null));
    JPA.merge(clazz);

    setting = new RtStartlistSetting();
    setting.setId(RtStartlistSettingKey.create((Long) null));
    setting.setEventNr(event.getId().getId());
    JPA.merge(setting);

    course = new RtCourse();
    course.setId(RtCourseKey.create((Long) null));
    course.setEventNr(event.getId().getId());
    if (startlistSettingForCourse) {
      course.setStartlistSettingNr(setting.getId().getId());
    }
    JPA.merge(course);

    eventClass = new RtEventClass();
    RtEventClassKey id = new RtEventClassKey();
    id.setClassUid(clazz.getId().getId());
    id.setEventNr(event.getId().getId());
    id.setClientNr(ServerSession.get().getSessionClientNr());
    eventClass.setId(id);
    if (startlistSettingForEventClass) {
      eventClass.setStartlistSettingNr(setting.getId().getId());
    }
    eventClass.setCourseNr(course.getId().getId());
    JPA.merge(eventClass);

    participation = new RtParticipation();
    RtParticipationKey key = new RtParticipationKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setEntryNr(entry.getId().getId());
    key.setEventNr(event.getId().getId());
    participation.setId(key);
    participation.setClassUid(clazz.getId().getId());
    JPA.merge(participation);

    race = new RtRace();
    race.setId(RtRaceKey.create((Long) null));
    race.setEventNr(event.getId().getId());
    race.setEntryNr(entry.getId().getId());
    race.setManualStatus(false);
    race.setBibNo("12345");
    race.setLegClassUid(clazz.getId().getId());
    JPA.merge(race);
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      EventProcessService svc = new EventProcessService();
      svc.delete(event.getId().getId(), true);
    }
  }

}
