package com.rtiming.server.map;

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
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtEventMap;
import com.rtiming.shared.dao.RtEventMapKey;
import com.rtiming.shared.dao.RtMap;
import com.rtiming.shared.dao.RtMapKey;
import com.rtiming.shared.event.EventMapFormData;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class EventMapProcessServiceTest {

  private RtMap map;
  private RtEvent event;
  private RtEventMap eventMap;

  @Test
  public void testDelete1() throws ProcessingException {
    EventMapProcessService svc = new EventMapProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    EventMapProcessService svc = new EventMapProcessService();
    svc.delete(new EventMapFormData());
  }

  @Test
  public void testDelete3() throws ProcessingException {
    createEventWithMap();

    EventMapProcessService svc = new EventMapProcessService();
    EventMapFormData formData = new EventMapFormData();
    formData.getEvent().setValue(event.getId().getId());
    formData.getMap().setValue(map.getId().getId());
    svc.delete(formData);

    RtEventMap find = JPA.find(RtEventMap.class, eventMap.getId());
    Assert.assertNull("deleted", find);
  }

  @Test
  public void testCreate1() throws Exception {
    createEventWithMap();
    JPA.remove(eventMap);
    EventMapFormData formData = new EventMapFormData();
    formData.getEvent().setValue(event.getId().getId());
    formData.getMap().setValue(map.getId().getId());
    EventMapProcessService svc = new EventMapProcessService();
    formData = svc.create(formData);

    RtEventMap find = JPA.find(RtEventMap.class, eventMap.getId());
    Assert.assertNotNull("created", find);
  }

  @Test
  public void testCreate2() throws Exception {
// TODO MIG    
//    ServerJob job = new ServerJob("test", ServerSession.get()) {
//      @Override
//      protected IStatus runTransaction(IProgressMonitor monitor) throws Exception {
//        createEventWithMap();
//        EventMapFormData formData = new EventMapFormData();
//        formData.getEvent().setValue(event.getId().getId());
//        formData.getMap().setValue(map.getId().getId());
//        EventMapProcessService svc = new EventMapProcessService();
//        formData = svc.create(formData);
//        return null;
//      }
//    };
//    IStatus result = job.runNow(null);
//    Assert.assertTrue("error message", result.getMessage().startsWith(TEXTS.get("DuplicateKeyMessage")));
  }

  private void createEventWithMap() throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create(event.getId()));
    JPA.merge(event);

    map = new RtMap();
    map.setId(RtMapKey.create(map.getId()));
    JPA.merge(map);

    eventMap = new RtEventMap();
    RtEventMapKey eventMapKey = new RtEventMapKey();
    eventMapKey.setMapNr(map.getId().getId());
    eventMapKey.setEventNr(event.getId().getId());
    eventMapKey.setClientNr(ServerSession.get().getSessionClientNr());
    eventMap.setId(eventMapKey);
    JPA.merge(eventMap);
  }

  @After
  public void after() throws ProcessingException {
    if (eventMap != null && JPA.find(RtEventMap.class, eventMap.getId()) != null) {
      JPA.remove(eventMap);
    }
    if (map != null && JPA.find(RtMap.class, map.getId()) != null) {
      JPA.remove(map);
    }
    if (event != null && JPA.find(RtEvent.class, event.getId()) != null) {
      JPA.remove(event);
    }
  }

}
