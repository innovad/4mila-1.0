package com.rtiming.server.entry.startblock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtEventStartblock;
import com.rtiming.shared.dao.RtEventStartblockKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.entry.startblock.EventStartblockFormData;
import com.rtiming.shared.entry.startblock.StartblockCodeType;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class EventStartblockProcessServiceTest {

  private RtEvent event;
  private RtUc startblock;
  private RtEventStartblock eventStartblock;
  private static boolean runAfter;

  @Before
  public void before() {
    runAfter = true;
  }

  @Test
  public void testLoad1() throws Exception {
    EventStartblockProcessService svc = new EventStartblockProcessService();
    EventStartblockFormData formData = new EventStartblockFormData();
    svc.load(formData);
  }

  @Test
  public void testLoad2() throws Exception {
    createEventStartblock();
    EventStartblockProcessService svc = new EventStartblockProcessService();
    EventStartblockFormData formData = new EventStartblockFormData();
    formData.getStartblockUid().setValue(eventStartblock.getId().getId());
    formData.setEventNr(event.getId().getId());
    formData = svc.load(formData);

    assertEquals("load", eventStartblock.getSortcode(), formData.getSortCode().getValue());
  }

  @Test
  public void testPrepareCreate1() throws Exception {
    EventStartblockProcessService svc = new EventStartblockProcessService();
    EventStartblockFormData formData = new EventStartblockFormData();
    formData = svc.prepareCreate(formData);
    Assert.assertEquals("default sortcode", 1L, formData.getSortCode().getValue().longValue());
  }

  @Test
  public void testPrepareCreate2() throws Exception {
    createEventStartblock();
    EventStartblockProcessService svc = new EventStartblockProcessService();
    EventStartblockFormData formData = new EventStartblockFormData();
    formData.setEventNr(event.getId().getId());
    formData = svc.prepareCreate(formData);
    Assert.assertEquals("default sortcode", 1L, formData.getSortCode().getValue().longValue());
  }

  @Test
  public void testStore() throws ProcessingException {
    createEventStartblock();

    EventStartblockProcessService svc = new EventStartblockProcessService();
    EventStartblockFormData formData = new EventStartblockFormData();
    formData.getStartblockUid().setValue(eventStartblock.getId().getId());
    formData.setEventNr(event.getId().getId());
    formData.getSortCode().setValue(777L);
    formData = svc.store(formData);

    RtEventStartblock es = JPA.find(RtEventStartblock.class, eventStartblock.getId());
    assertEquals("updated", 777L, es.getSortcode().longValue());

    svc.delete(formData);
  }

  @Test
  public void testDelete1() throws ProcessingException {
    EventStartblockProcessService svc = new EventStartblockProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    EventStartblockProcessService svc = new EventStartblockProcessService();
    EventStartblockFormData formData = new EventStartblockFormData();
    svc.delete(formData);
  }

  @Test
  public void testDelete3() throws Exception {
    createEventStartblock();

    EventStartblockProcessService svc = new EventStartblockProcessService();
    EventStartblockFormData formData = new EventStartblockFormData();
    formData.getStartblockUid().setValue(eventStartblock.getId().getId());
    formData.setEventNr(event.getId().getId());
    svc.delete(formData);

    RtEventStartblock result = JPA.find(RtEventStartblock.class, eventStartblock.getId());
    assertNull("deleted", result);
  }

  @Test
  public void testCreate1() throws Exception {
    createEventStartblock();
    JPA.remove(eventStartblock);

    EventStartblockProcessService svc = new EventStartblockProcessService();
    EventStartblockFormData formData = new EventStartblockFormData();
    formData.setEventNr(event.getId().getId());
    formData.getStartblockUid().setValue(startblock.getId().getId());
    svc.create(formData);

    svc.delete(formData);
  }

  @Test(expected = VetoException.class)
  public void testCreate2() throws Exception {
    runAfter = false;
    createEventStartblock();
    JPA.remove(eventStartblock);

    EventStartblockProcessService svc = new EventStartblockProcessService();
    EventStartblockFormData formData = new EventStartblockFormData();
    formData.setEventNr(event.getId().getId());
    formData.getStartblockUid().setValue(startblock.getId().getId());
    svc.create(formData);
    svc.create(formData);
  }

  @After
  public void after() throws ProcessingException {
    if (!runAfter) {
      return;
    }
    if (eventStartblock != null) {
      JPA.remove(eventStartblock);
    }
    if (startblock != null) {
      JPA.remove(startblock);
    }
    if (event != null) {
      JPA.remove(event);
    }
  }

  private void createEventStartblock() throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create(new RtEventKey()));
    JPA.merge(event);

    startblock = new RtUc();
    startblock.setCodeType(StartblockCodeType.ID);
    startblock.setActive(true);
    startblock.setId(RtUcKey.create((Long) null));
    JPA.merge(startblock);

    eventStartblock = new RtEventStartblock();
    RtEventStartblockKey key = new RtEventStartblockKey();
    key.setEventNr(event.getId().getId());
    key.setId(startblock.getId().getId());
    eventStartblock.setId(RtEventStartblockKey.create(key));
    JPA.merge(eventStartblock);
  }

}
