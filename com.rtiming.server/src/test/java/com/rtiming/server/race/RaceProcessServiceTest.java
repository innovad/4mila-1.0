package com.rtiming.server.race;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceControl;
import com.rtiming.shared.dao.RtRaceControlKey;
import com.rtiming.shared.dao.RtRaceKey;
import com.rtiming.shared.ecard.ECardTypeCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class RaceProcessServiceTest {

  private RtRace race;
  private RtRaceControl raceControl;
  private RtPunchSession session;
  private RtEcard ecard;
  private RtEvent event;

  @Test
  public void testDelete1() throws ProcessingException {
    RaceProcessService svc = new RaceProcessService();
    svc.delete();
  }

  @Test
  public void testDelete2() throws ProcessingException {
    RaceProcessService svc = new RaceProcessService();
    svc.delete(0L);
  }

  @Test
  public void testDelete3() throws ProcessingException {
    createRace();
    createRaceControl();
    createPunchSession();

    RaceProcessService svc = new RaceProcessService();
    svc.delete(race.getId().getId());

    RtRace find = JPA.find(RtRace.class, race.getId());
    assertNull("deleted", find);

    RtRaceControl find2 = JPA.find(RtRaceControl.class, raceControl.getId());
    assertNull("deleted", find2);

    RtPunchSession find3 = JPA.find(RtPunchSession.class, session.getId());
    Assert.assertNotNull("not deleted", find3);
    Assert.assertNull("updated", find3.getRtRace());
  }

  @Test
  public void testFindRaceNr1() throws Exception {
    RaceProcessService svc = new RaceProcessService();
    Long result = svc.findRaceNr(0L, null);
    assertNull("not found", result);
  }

  @Test
  public void testFindRaceNr2() throws Exception {
    RaceProcessService svc = new RaceProcessService();
    Long result = svc.findRaceNr(0L, "abcdef");
    assertNull("not found", result);
  }

  @Test
  public void testFindRaceNr3() throws Exception {
    createRaceWithECard();

    RaceProcessService svc = new RaceProcessService();
    Long result = svc.findRaceNr(event.getId().getId(), "ABCDEF");
    assertNotNull("found", result);
  }

  @Test
  public void testFindRaceNr4() throws Exception {
    createRaceWithECard();
    createPunchSessionForRace();

    RaceProcessService svc = new RaceProcessService();
    Long result = svc.findRaceNr(event.getId().getId(), "ABCDEF");
    assertNull("found", result);
  }

  @Test
  public void testStore1() throws Exception {
    RaceProcessService svc = new RaceProcessService();
    RaceBean bean = new RaceBean();
    svc.store(bean);
  }

  @Test
  public void testStore2() throws Exception {
    createRace();
    RaceProcessService svc = new RaceProcessService();
    RaceBean bean = new RaceBean();
    bean.setRaceNr(race.getId().getId());
    bean = svc.load(bean);
    bean.setBibNo("12121");
    svc.store(bean);

    RtRace find = JPA.find(RtRace.class, race.getId());
    assertEquals("updated", "12121", find.getBibNo());
  }

  @Test
  public void testStore3() throws Exception {
    createRace();
    RaceProcessService svc = new RaceProcessService();
    RaceBean bean = new RaceBean();
    bean.setRaceNr(race.getId().getId());
    bean = svc.load(bean);
    bean.setStatusUid(RaceStatusCodeType.DisqualifiedCode.ID);
    svc.store(bean);

    RtRace find = JPA.find(RtRace.class, race.getId());
    assertEquals("updated", RaceStatusCodeType.DisqualifiedCode.ID, find.getStatusUid().longValue());
  }

  @Test
  public void testFindByAccountNr1() throws Exception {
    RaceProcessService svc = new RaceProcessService();
    svc.findByAccountNr(1L);
  }

  private void createPunchSessionForRace() throws ProcessingException {
    session = new RtPunchSession();
    session.setId(RtPunchSessionKey.create((Long) null));
    session.setRaceNr(race.getId().getId());
    JPA.merge(session);
  }

  private void createRaceWithECard() throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    ecard = new RtEcard();
    ecard.setKey(RtEcardKey.create((Long) null));
    ecard.setEcardNo("ABCDEF");
    ecard.setTypeUid(ECardTypeCodeType.SICard10Code.ID);
    JPA.merge(ecard);

    race = new RtRace();
    race.setId(RtRaceKey.create((Long) null));
    race.setEventNr(event.getId().getId());
    race.setEcardNr(ecard.getKey().getId());
    race.setManualStatus(false);
    JPA.merge(race);
  }

  private void createPunchSession() throws ProcessingException {
    session = new RtPunchSession();
    session.setId(RtPunchSessionKey.create((Long) null));
    session.setRtRace(race);
    JPA.merge(session);
  }

  private void createRaceControl() throws ProcessingException {
    raceControl = new RtRaceControl();
    raceControl.setId(RtRaceControlKey.create((Long) null));
    raceControl.setRaceNr(race.getId().getId());
    raceControl.setManualStatus(false);
    JPA.merge(raceControl);
  }

  private void createRace() throws ProcessingException {
    race = new RtRace();
    race.setId(RtRaceKey.create((Long) null));
    race.setManualStatus(false);
    JPA.merge(race);
  }

  @After
  public void after() throws ProcessingException {
    if (session != null) {
      JPA.remove(session);
    }
    if (raceControl != null) {
      raceControl.setRaceNr(null);
      JPA.remove(raceControl);
    }
    if (race != null) {
      JPA.remove(race);
    }
    if (ecard != null) {
      JPA.remove(ecard);
    }
    if (event != null) {
      JPA.remove(event);
    }
  }

}
