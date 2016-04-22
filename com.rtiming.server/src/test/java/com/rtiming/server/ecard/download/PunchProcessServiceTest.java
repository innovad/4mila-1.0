package com.rtiming.server.ecard.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtPunch;
import com.rtiming.shared.dao.RtPunchKey;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey;
import com.rtiming.shared.ecard.download.PunchFormData;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class PunchProcessServiceTest {

  private RtPunchSession session;
  private RtPunch punch;
  private RtEvent event;

  @Test
  public void testPrepareCreate1() throws Exception {
    createPunchSession();

    PunchProcessService svc = new PunchProcessService();
    PunchFormData formData = new PunchFormData();
    formData.getPunchSession().setValue(session.getId().getId());
    formData = svc.prepareCreate(formData);

    assertEquals("event", event.getId().getId(), formData.getEvent().getValue());
  }

  @Test
  public void testPrepareCreate2() throws Exception {
    PunchProcessService svc = new PunchProcessService();
    PunchFormData formData = new PunchFormData();
    formData = svc.prepareCreate(formData);

    assertNull("event", formData.getEvent().getValue());
  }

  @Test
  public void testPrepareCreate3() throws Exception {
    PunchProcessService svc = new PunchProcessService();
    svc.prepareCreate(null);
  }

  @Test
  public void testDelete1() throws ProcessingException {
    PunchProcessService svc = new PunchProcessService();
    svc.delete(0L, null);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    createPunchSession();

    PunchProcessService svc = new PunchProcessService();
    svc.delete(session.getId().getId(), null);

    RtPunchSession findSession = JPA.find(RtPunchSession.class, session.getId());
    assertNotNull("not deleted", findSession);

    RtPunch findPunch = JPA.find(RtPunch.class, punch.getId());
    assertNull("deleted", findPunch);
  }

  @Test
  public void testDelete3() throws ProcessingException {
    createPunchSession();

    PunchProcessService svc = new PunchProcessService();
    svc.delete(session.getId().getId(), new Long[]{1L});

    RtPunchSession findSession = JPA.find(RtPunchSession.class, session.getId());
    assertNotNull("not deleted", findSession);

    RtPunch findPunch = JPA.find(RtPunch.class, punch.getId());
    assertNull("deleted", findPunch);
  }

  @Test
  public void testDelete4() throws ProcessingException {
    createPunchSession();

    PunchProcessService svc = new PunchProcessService();
    svc.delete(session.getId().getId(), new Long[]{2L});

    RtPunchSession findSession = JPA.find(RtPunchSession.class, session.getId());
    assertNotNull("not deleted", findSession);

    RtPunch findPunch = JPA.find(RtPunch.class, punch.getId());
    assertNotNull("not deleted", findPunch);
  }

  @Test
  public void testDelete5() throws ProcessingException {
    createPunchSession();

    PunchProcessService svc = new PunchProcessService();
    svc.delete(session.getId().getId(), new Long[]{});

    RtPunchSession findSession = JPA.find(RtPunchSession.class, session.getId());
    assertNotNull("not deleted", findSession);

    RtPunch findPunch = JPA.find(RtPunch.class, punch.getId());
    assertNull("deleted", findPunch);
  }

  @Test
  public void testStore() throws Exception {
    createPunchSession();

    PunchProcessService svc = new PunchProcessService();
    PunchFormData formData = new PunchFormData();
    formData.getPunchSession().setValue(session.getId().getId());
    formData.getSortCode().setValue(punch.getId().getSortcode());
    formData.getControlNo().setValue("99");
    formData = svc.store(formData);
    formData = svc.load(formData);

    RtPunchKey key = new RtPunchKey();
    key.setId(session.getId().getId());
    key.setSortcode(punch.getId().getSortcode());
    RtPunch find = JPA.find(RtPunch.class, RtPunchKey.create(key));

    Assert.assertEquals("updated", "99", find.getControlNo());
  }

  @Test
  public void testCreate1() throws Exception {
    createPunchSession();

    PunchProcessService svc = new PunchProcessService();
    PunchFormData formData = new PunchFormData();
    formData.getPunchSession().setValue(session.getId().getId());
    formData.getSortCode().setValue(3L);
    formData.getControlNo().setValue("999");
    formData = svc.create(formData);

    RtPunchKey key = new RtPunchKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setId(session.getId().getId());
    key.setSortcode(3L);
    RtPunch find = JPA.find(RtPunch.class, key);
    assertNotNull(find);

    JPA.remove(find);
  }

  @Test
  public void testCreateTestData1() throws Exception {
    createPunchSession();
    PunchProcessService svc = new PunchProcessService();
    svc.createTestData(session.getId().getId(), null, event.getId().getId(), null);
  }

  private void createPunchSession() throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    session = new RtPunchSession();
    session.setId(RtPunchSessionKey.create((Long) null));
    session.setEventNr(event.getId().getId());
    JPA.merge(session);

    punch = new RtPunch();
    RtPunchKey key = new RtPunchKey();
    key.setId(session.getId().getId());
    key.setSortcode(1L);
    key.setClientNr(ServerSession.get().getSessionClientNr());
    punch.setId(key);
    JPA.merge(punch);
  }

  @After
  public void after() throws ProcessingException {
    if (punch != null) {
      JPA.remove(punch);
    }
    if (session != null) {
      JPA.remove(session);
    }
    if (event != null) {
      JPA.remove(event);
    }
  }

}
