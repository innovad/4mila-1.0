package com.rtiming.server.ecard.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtEcardStation;
import com.rtiming.shared.dao.RtEcardStationKey;
import com.rtiming.shared.ecard.download.ECardStationFormData;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class ECardStationProcessServiceTest {

  @Test
  public void testDelete1() throws ProcessingException {
    ECardStationProcessService svc = new ECardStationProcessService();
    svc.delete(null, false);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    ECardStationProcessService svc = new ECardStationProcessService();
    svc.delete(new ECardStationFormData(), false);
  }

  @Test
  public void testDelete3() throws ProcessingException {
    ECardStationProcessService svc = new ECardStationProcessService();
    RtEcardStation station = new RtEcardStation();
    station.setId(RtEcardStationKey.create((Long) null));
    JPA.merge(station);

    ECardStationFormData bean = new ECardStationFormData();
    bean.setECardStationNr(station.getId().getId());
    svc.delete(bean, false);

    RtEcardStation find = JPA.find(RtEcardStation.class, station.getId());
    assertNull("deleted", find);

    JPA.remove(station);
  }

  @Test
  public void testDelete4() throws ProcessingException {
    ECardStationProcessService svc = new ECardStationProcessService();
    ECardStationFormData formData = new ECardStationFormData();
    formData.setECardStationNr(0L);
    svc.delete(formData, true);
  }

  @Test
  public void testFind1() throws Exception {
    ECardStationProcessService svc = new ECardStationProcessService();
    RtEcardStation station = new RtEcardStation();
    station.setId(RtEcardStationKey.create((Long) null));
    station.setPort("PORT" + System.currentTimeMillis());
    station.setClientAddress("ADDRESS" + System.currentTimeMillis());
    JPA.merge(station);

    ECardStationFormData formData = svc.find(station.getPort(), station.getClientAddress());
    assertNotNull("station exists", formData.getECardStationNr());
    assertEquals("saved", station.getId().getId(), formData.getECardStationNr());
    assertEquals("port", station.getPort(), formData.getPort().getValue());
    assertEquals("address", station.getClientAddress(), formData.getClientAddress().getValue());

    JPA.remove(station);
  }

  @Test
  public void testFind2() throws Exception {
    ECardStationProcessService svc = new ECardStationProcessService();
    String port = "Port" + System.currentTimeMillis();
    String ip = "Address" + System.currentTimeMillis();

    ECardStationFormData formData = svc.find(port, ip);
    assertNull("station NOT exists", formData.getECardStationNr());
    assertEquals("port", port.toUpperCase(), formData.getPort().getValue());
    assertEquals("address", ip.toUpperCase(), formData.getClientAddress().getValue());
  }

  @Test
  public void testStore1() throws Exception {
    ECardStationProcessService svc = new ECardStationProcessService();
    svc.store(null);
  }

  @Test
  public void testStore2() throws Exception {
    ECardStationProcessService svc = new ECardStationProcessService();
    svc.store(new ECardStationFormData());
  }

  @Test
  public void testStore3() throws Exception {
    RtEcardStation station = new RtEcardStation();
    station.setId(RtEcardStationKey.create((Long) null));
    station.setPort("abc");
    JPA.merge(station);

    ECardStationProcessService svc = new ECardStationProcessService();
    ECardStationFormData formData = new ECardStationFormData();
    formData.setECardStationNr(station.getId().getId());
    formData = svc.load(formData);
    formData.getPort().setValue("def");
    svc.store(formData);

    RtEcardStation find = JPA.find(RtEcardStation.class, station.getId());
    Assert.assertEquals("updated", "def", find.getPort());

    JPA.remove(station);
  }

}
