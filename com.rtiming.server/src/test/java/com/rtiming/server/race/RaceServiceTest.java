package com.rtiming.server.race;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.BeanMetaData;
import org.eclipse.scout.rt.platform.IBean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.eclipse.scout.rt.testing.shared.TestingUtility;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.race.validation.RaceValidationResult;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtPunch;
import com.rtiming.shared.dao.RtPunchKey;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceControl;
import com.rtiming.shared.dao.RtRaceControlKey;
import com.rtiming.shared.dao.RtRaceKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.RaceStatusCodeType;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class RaceServiceTest {

  @SuppressWarnings("rawtypes")
  private IBean<?> registration = null;
  private IEventClassProcessService service;
  private RtRace race;

  private void mockEventClassService() {
    service = Mockito.mock(IEventClassProcessService.class);
    registration = TestingUtility.registerBean(new BeanMetaData(IEventClassProcessService.class, service));
  }

  @Test
  public void testLoadSettings1() throws Exception {
    mockEventClassService();
    Mockito.when(service.load(Mockito.any(EventClassFormData.class))).thenReturn(new EventClassFormData());

    RaceService raceService = new RaceService();
    RaceSettings settings = raceService.loadSettings(1L, 2L);
    Assert.assertFalse("Not using Startlist", settings.isUsingStartlist());
    Assert.assertFalse("Summary Max Time", settings.isSummaryTimeIsMaxTime());
  }

  @Test
  public void testLoadSettings2() throws Exception {
    mockEventClassService();
    EventClassFormData formData = new EventClassFormData();
    formData.getType().setValue(ClassTypeCodeType.IndividualEventCode.ID);
    Mockito.when(service.load(Mockito.any(EventClassFormData.class))).thenReturn(formData);

    RaceService raceService = new RaceService();
    RaceSettings settings = raceService.loadSettings(1L, 2L);
    Assert.assertFalse("Not using Startlist", settings.isUsingStartlist());
    Assert.assertTrue("Summary Max Time", settings.isSummaryTimeIsMaxTime());
  }

  @Test
  public void testInflateRaceControls1() throws Exception {
    RaceService svc = new RaceService();
    svc.inflateRaceControls(null);
  }

  @Test
  public void testInflateRaceControls2() throws Exception {
    RaceService svc = new RaceService();
    svc.inflateRaceControls(new Long[]{});
  }

  @Test
  public void testInflateRaceControls3() throws Exception {
    RtEvent event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    RtUc clazz = new RtUc();
    clazz.setActive(true);
    clazz.setCodeType(ClassCodeType.ID);
    clazz.setId(RtUcKey.create((Long) null));
    JPA.merge(clazz);

    RtEventClass eventClass = new RtEventClass();
    RtEventClassKey id = new RtEventClassKey();
    id.setClassUid(clazz.getId().getId());
    id.setEventNr(event.getId().getId());
    id.setClientNr(ServerSession.get().getSessionClientNr());
    eventClass.setId(id);
    JPA.merge(eventClass);

    RtRace race2 = new RtRace();
    race2.setId(RtRaceKey.create((Long) null));
    race2.setManualStatus(false);
    race2.setEventNr(event.getId().getId());
    race2.setLegClassUid(clazz.getId().getId());
    JPA.merge(race2);

    RaceService svc = new RaceService();
    svc.inflateRaceControls(new Long[]{race2.getId().getId()});

    JPA.remove(race2);
    JPA.remove(eventClass);
    JPA.remove(clazz);
    JPA.remove(event);
  }

  @Test
  public void testResetControls1() throws Exception {
    RaceService svc = new RaceService();
    svc.resetControls(0L, false);
  }

  @Test
  public void testResetControls2() throws Exception {
    RtRaceControl control = createRaceWithControl(ControlStatusCodeType.AdditionalCode.ID, false);

    RaceService svc = new RaceService();
    svc.resetControls(race.getId().getId(), true);

    RtRaceControl find = JPA.find(RtRaceControl.class, control.getId());
    assertNull("deleted", find);
  }

  @Test
  public void testResetControls3() throws Exception {
    RtRaceControl control = createRaceWithControl(ControlStatusCodeType.AdditionalCode.ID, false);

    RaceService svc = new RaceService();
    svc.resetControls(race.getId().getId(), false);

    RtRaceControl find = JPA.find(RtRaceControl.class, control.getId());
    assertNull("deleted", find);
  }

  @Test
  public void testResetControls4() throws Exception {
    RtRaceControl control = createRaceWithControl(ControlStatusCodeType.WrongCode.ID, false);

    RaceService svc = new RaceService();
    svc.resetControls(race.getId().getId(), false);

    RtRaceControl find = JPA.find(RtRaceControl.class, control.getId());
    assertNull("deleted", find);
  }

  @Test
  public void testResetControls5() throws Exception {
    RtRaceControl control = createRaceWithControl(ControlStatusCodeType.MissingCode.ID, false);

    RaceService svc = new RaceService();
    svc.resetControls(race.getId().getId(), false);

    RtRaceControl find = JPA.find(RtRaceControl.class, control.getId());
    assertNotNull("deleted", find);
    assertEquals("Initial Status", ControlStatusCodeType.InitialStatusCode.ID, find.getStatusUid().longValue());
    assertNull("overall time reset", find.getOverallTime());
    assertNull("leg time reset", find.getLegTime());
    JPA.remove(find);
  }

  @Test
  public void testResetControls6() throws Exception {
    RtRaceControl control = createRaceWithControl(ControlStatusCodeType.MissingCode.ID, true);

    RaceService svc = new RaceService();
    svc.resetControls(race.getId().getId(), false);

    RtRaceControl find = JPA.find(RtRaceControl.class, control.getId());
    assertNotNull("deleted", find);
    assertEquals("Initial Status", ControlStatusCodeType.MissingCode.ID, find.getStatusUid().longValue());
    assertEquals("overall time NOT reset", 888L, find.getOverallTime().longValue());
    assertNull("leg time reset", find.getLegTime());
    JPA.remove(find);
  }

  @Test
  public void testLoadPunchedControls1() throws Exception {
    RaceService svc = new RaceService();
    List<RaceControlBean> result = svc.loadPunchedControls(0);
    assertEquals("0 Controls", 0, result.size());
  }

  @Test
  public void testLoadPunchedControls2() throws Exception {
    createRaceWithControl(ControlStatusCodeType.OkCode.ID, false);
    RtPunchSession session = new RtPunchSession();
    session.setId(RtPunchSessionKey.create((Long) null));
    session.setRaceNr(race.getId().getId());
    JPA.persist(session);

    RtPunch punch = new RtPunch();
    RtPunchKey key = new RtPunchKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setId(session.getId().getId());
    key.setSortcode(1L);
    punch.setId(key);
    JPA.persist(punch);

    RaceService svc = new RaceService();
    List<RaceControlBean> result = svc.loadPunchedControls(race.getId().getId());
    assertEquals("1 Control", 1, result.size());

    JPA.remove(punch);
    JPA.remove(session);
  }

  @Test
  public void testStoreRaceControl1() throws Exception {
    RaceService svc = new RaceService();
    RaceControlBean raceControl = new RaceControlBean();
    svc.storeRaceControl(raceControl);
  }

  @Test
  public void testStoreRaceControl2() throws Exception {
    RtRaceControl control = createRaceWithControl(ControlStatusCodeType.OkCode.ID, false);
    RaceService svc = new RaceService();
    RaceControlBean raceControl = new RaceControlBean();
    raceControl.setRaceControlNr(control.getId().getId());
    raceControl.setOverallTime(777L);
    raceControl.setControlStatusUid(ControlStatusCodeType.MissingCode.ID);
    raceControl.setLegTime(555L);
    svc.storeRaceControl(raceControl);

    RtRaceControl find = JPA.find(RtRaceControl.class, control.getId());
    assertEquals("Updated", 777L, find.getOverallTime().longValue());
    assertEquals("Updated", 555L, find.getLegTime().longValue());
    assertEquals("Updated", ControlStatusCodeType.MissingCode.ID, find.getStatusUid().longValue());
  }

  @Test
  public void testLoadPlannedControls1() throws Exception {
    RaceService svc = new RaceService();
    List<RaceControlBean> result = svc.loadPlannedControls(0L);
    assertEquals("Empty", 0, result.size());
  }

  @Test(expected = VetoException.class)
  public void testValidateRaceInternal1() throws Exception {
    RaceService svc = new RaceService();
    RaceBean raceBean = new RaceBean();
    raceBean.setRaceNr(0L);
    RaceValidationResult result = svc.validateRaceInternal(raceBean, new RaceSettings());
    assertNotNull(result);
  }

  @Test
  public void testValidateRaceInternal2() throws Exception {
    createRaceWithControl(ControlStatusCodeType.OkCode.ID, false);
    RtPunchSession session = new RtPunchSession();
    session.setId(RtPunchSessionKey.create((Long) null));
    session.setRaceNr(race.getId().getId());
    session.setStart(1234L);
    JPA.persist(session);

    RaceService svc = new RaceService();
    RaceBean raceBean = new RaceBean();
    raceBean.setRaceNr(race.getId().getId());
    RaceValidationResult result = svc.validateRaceInternal(raceBean, new RaceSettings());
    assertNotNull(result);
    assertEquals("Start time", 1234L, result.getStartTime().longValue());
    assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
  }

  @Test
  public void testUpdateParticipation1() throws Exception {
    RaceService svc = new RaceService();
    svc.updateParticipation(0L, 0L, 0L, true);
  }

  @Test
  public void testUpdateParticipation2() throws Exception {
    RaceService svc = new RaceService();
    svc.updateParticipation(0L, 0L, 0L, false);
  }

  @Test
  public void testUpdateRace1() throws Exception {
    RaceService svc = new RaceService();
    svc.updateRace(new RaceBean(), 0L, 0L, 0L, true, true);
  }

  @Test
  public void testUpdateRace2() throws Exception {
    RaceService svc = new RaceService();
    svc.updateRace(new RaceBean(), 0L, 0L, 0L, false, true);
  }

  @Test
  public void testUpdateRace3() throws Exception {
    RaceService svc = new RaceService();
    svc.updateRace(new RaceBean(), 0L, 0L, 0L, false, false);
  }

  @Test
  public void testUpdateRace4() throws Exception {
    RaceService svc = new RaceService();
    svc.updateRace(new RaceBean(), 0L, 0L, 0L, true, false);
  }

  private RtRaceControl createRaceWithControl(long controlStatusUid, Boolean manualRaceControlStatus) throws ProcessingException {
    race = new RtRace();
    race.setId(RtRaceKey.create((Long) null));
    race.setManualStatus(false);
    JPA.merge(race);

    RtRaceControl control = new RtRaceControl();
    control.setId(RtRaceControlKey.create((Long) null));
    control.setManualStatus(manualRaceControlStatus);
    control.setSortcode(1L);
    control.setStatusUid(controlStatusUid);
    control.setRaceNr(race.getId().getId());
    control.setOverallTime(888L);
    control.setLegTime(444L);
    JPA.merge(control);
    return control;
  }

  @After
  public void after() throws ProcessingException {
    if (registration != null) {
      TestingUtility.unregisterBean(registration);
    }
    if (race != null) {
      BEANS.get(IRaceProcessService.class).delete(race.getId().getId());
    }
  }

}
