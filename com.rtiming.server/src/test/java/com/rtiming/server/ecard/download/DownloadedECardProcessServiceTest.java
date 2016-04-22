package com.rtiming.server.ecard.download;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey;
import com.rtiming.shared.ecard.download.DownloadedECardFormData;
import com.rtiming.shared.ecard.download.PunchFormData;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class DownloadedECardProcessServiceTest {

  private RtEvent event;
  private RtPunchSession punchSession;

  @Test
  public void testDelete1() throws Exception {
    DownloadedECardProcessService svc = new DownloadedECardProcessService();
    DownloadedECardFormData formData = new DownloadedECardFormData();
    svc.delete(formData);
  }

  @Test
  public void testDelete2() throws Exception {
    DownloadedECardProcessService svc = new DownloadedECardProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete3() throws Exception {
    createPunchSession();

    DownloadedECardProcessService svc = new DownloadedECardProcessService();
    DownloadedECardFormData formData = new DownloadedECardFormData();
    formData.setPunchSessionNr(punchSession.getId().getId());
    svc.delete(formData);

    punchSession = JPA.find(RtPunchSession.class, punchSession.getId());
    assertNull("deleted", punchSession);
    JPA.remove(event);
  }

  @Test
  public void testStore1() throws Exception {
    createPunchSession();
    Date date = new Date();

    DownloadedECardProcessService svc = new DownloadedECardProcessService();
    DownloadedECardFormData formData = new DownloadedECardFormData();
    formData.setPunchSessionNr(punchSession.getId().getId());
    formData.getEvtDownload().setValue(date);
    svc.store(formData);

    RtPunchSession find = JPA.find(RtPunchSession.class, punchSession.getId());
    Assert.assertEquals("updated", date, find.getEvtDownload());
    JPA.remove(punchSession);
    JPA.remove(event);
  }

  @Test
  public void testStore2() throws Exception {
    DownloadedECardProcessService svc = new DownloadedECardProcessService();
    svc.store(null);
  }

  @Test
  public void testStore3() throws Exception {
    DownloadedECardProcessService svc = new DownloadedECardProcessService();
    svc.store(new DownloadedECardFormData());
  }

  @Test
  public void testMatchClass() throws Exception {
    DownloadedECardProcessService svc = new DownloadedECardProcessService();
    createPunchSession();
    Long[] result = svc.matchClass(punchSession.getId().getId(), new ArrayList<PunchFormData>(), event.getId().getId(), event.getEvtZero(), null, null);
    assertTrue("empty", result.length == 0);
  }

  private void createPunchSession() throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    punchSession = new RtPunchSession();
    punchSession.setId(RtPunchSessionKey.create((Long) null));
    punchSession.setEventNr(event.getId().getId());
    JPA.merge(punchSession);
  }

}
