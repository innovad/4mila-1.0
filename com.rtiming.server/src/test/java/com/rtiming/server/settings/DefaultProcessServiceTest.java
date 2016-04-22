package com.rtiming.server.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtDefault;
import com.rtiming.shared.dao.RtDefaultKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.settings.DefaultCodeType;
import com.rtiming.shared.settings.DefaultCodeType.DefaultEventCode;
import com.rtiming.shared.settings.DefaultFormData;
import com.rtiming.shared.settings.IDefaultProcessService;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class DefaultProcessServiceTest {

  @Test
  public void testLoad() throws Exception {
    RtEvent event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getId().getId());

    DefaultProcessService svc = new DefaultProcessService();
    DefaultFormData formData = new DefaultFormData();
    formData.getDefaultUid().setValue(DefaultCodeType.DefaultEventCode.ID);
    formData = svc.load(formData);

    Assert.assertEquals("default event", event.getId().getId(), formData.getValueInteger().getValue());
  }

  @Test
  public void testBackupDirectory() throws Exception {
    IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);

    service.setBackupDirectory("C:/temp");
    String dir = service.getBackupDirectory();
    Assert.assertEquals("Backup Directory", "C:/temp", dir);
  }

  @Test
  public void testBackupInterval() throws Exception {
    IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);

    service.setBackupInterval(12345L);
    Long value = service.getBackupInterval();
    Assert.assertEquals("Backup Interval", 12345, value.longValue());
  }

  @Test
  public void testGetDefaultEventNr() throws Exception {
    RtEvent event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    DefaultProcessService svc = new DefaultProcessService();
    svc.setDefaultEventNr(event.getId().getId());

    Long eventNr = svc.getDefaultEventNr();
    assertEquals("set", event.getId().getId(), eventNr);

    svc.setDefaultEventNr(null);
    JPA.remove(event);
  }

  @Test
  public void testCreate1() throws Exception {
    doTestCreate();
  }

  @Test
  public void testCreate2() throws Exception {
    RtDefaultKey key = new RtDefaultKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setId(DefaultEventCode.ID);
    RtDefault find = JPA.find(RtDefault.class, key);
    if (find != null) {
      JPA.remove(find);
    }
    doTestCreate();
  }

  private void doTestCreate() throws ProcessingException {
    DefaultProcessService svc = new DefaultProcessService();
    DefaultFormData formData = new DefaultFormData();
    formData.getDefaultUid().setValue(DefaultEventCode.ID);
    svc.create(formData);

    RtDefaultKey key = new RtDefaultKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setId(DefaultEventCode.ID);
    RtDefault find = JPA.find(RtDefault.class, key);
    assertNotNull("created", find);
  }

}
