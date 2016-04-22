package com.rtiming.server.map;

import java.util.List;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
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

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class MapProcessServiceTest {

  @Test
  public void testGetAllMaps() throws Exception {
    MapProcessService svc = new MapProcessService();

    RtMap map = new RtMap();
    map.setName("Gugel");
    map.setScale(10000L);
    map = svc.create(map);

    List<Long> mapNrs = svc.getAllMaps(ServerSession.get().getSessionClientNr());
    Assert.assertTrue("Map exists", mapNrs.contains(map.getId().getId()));

    svc.delete(map.getId());
  }

  @Test
  public void testFindMap1() throws Exception {
    RtEvent event = new RtEvent();
    event.setId(RtEventKey.create(event.getId()));
    JPA.merge(event);

    RtMap map = new RtMap();
    map.setId(RtMapKey.create(map.getId()));
    JPA.merge(map);

    RtEventMap eventMap = new RtEventMap();
    RtEventMapKey eventMapKey = new RtEventMapKey();
    eventMapKey.setMapNr(map.getId().getId());
    eventMapKey.setEventNr(event.getId().getId());
    eventMapKey.setClientNr(ServerSession.get().getSessionClientNr());
    eventMap.setId(eventMapKey);
    JPA.merge(eventMap);

    MapProcessService svc = new MapProcessService();
    RtMap mapFound = svc.findMap(event.getId().getId(), event.getId().getClientNr());
    Assert.assertNotNull("Map exists", mapFound);
    Assert.assertEquals("Correct Map found", map.getId(), mapFound.getId());

    JPA.remove(eventMap);
    JPA.remove(event);
    JPA.remove(map);
  }

  @Test
  public void testFindMap2() throws Exception {
    MapProcessService svc = new MapProcessService();
    RtMap mapFound = svc.findMap(-5L, -2L);
    Assert.assertNotNull("Map exists", mapFound);
    Assert.assertEquals("Map Client Nr", -2, mapFound.getId().getClientNr().longValue());
    Assert.assertNull("Map Nr null", mapFound.getId().getId());
  }

  @Test
  public void testDelete1() throws Exception {
    MapProcessService svc = new MapProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws Exception {
    MapProcessService svc = new MapProcessService();

    RtMap map = new RtMap();
    map.setId(RtMapKey.create(map.getId()));
    JPA.merge(map);

    svc.delete(map.getId());

    RtMap map2 = JPA.find(RtMap.class, map.getId());
    Assert.assertNull("map should be deleted", map2);
  }

  @Test
  public void testLoad1() throws Exception {
    MapProcessService svc = new MapProcessService();
    svc.load(null);
  }

  @Test
  public void testLoad2() throws Exception {
    MapProcessService svc = new MapProcessService();

    RtMap map = new RtMap();
    map.setId(RtMapKey.create(map.getId()));
    JPA.merge(map);
    RtMap map2 = svc.load(map.getId());
    Assert.assertNotNull("map loaded", map2);
    Assert.assertEquals("Key", map.getId(), map2.getId());

    svc.delete(map.getId());
  }

}
