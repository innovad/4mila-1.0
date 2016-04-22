package com.rtiming.server.settings.addinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtAdditionalInformationDef;
import com.rtiming.shared.dao.RtAdditionalInformationDefKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventAdditionalInformation;
import com.rtiming.shared.dao.RtEventAdditionalInformationKey;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.EventAdditionalInformationFormData;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class EventAdditionalInformationProcessServiceTest {

  private RtEvent event;
  private RtUc additionalInformation;
  private RtAdditionalInformationDef additionalInformationDef;
  private RtEventAdditionalInformation eventAdditionalInformation;

  @Test
  public void testDelete1() throws ProcessingException {
    EventAdditionalInformationProcessService svc = new EventAdditionalInformationProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    EventAdditionalInformationProcessService svc = new EventAdditionalInformationProcessService();
    svc.delete(new EventAdditionalInformationFormData());
  }

  @Test
  public void testCreate1() throws Exception {
    createTestData();
    RtEventAdditionalInformationKey key = eventAdditionalInformation.getId();
    JPA.remove(eventAdditionalInformation);
    eventAdditionalInformation = null;

    EventAdditionalInformationProcessService svc = new EventAdditionalInformationProcessService();
    EventAdditionalInformationFormData formData = new EventAdditionalInformationFormData();
    formData.getEvent().setValue(event.getId().getId());
    formData.getAdditionalInformation().setValue(additionalInformation.getId().getId());
    svc.create(formData);

    eventAdditionalInformation = JPA.find(RtEventAdditionalInformation.class, key);
    assertNotNull("created", eventAdditionalInformation);
  }

  @Test
  public void testCreate2() throws Exception {
    createTestData();
    RtEventAdditionalInformationKey key = eventAdditionalInformation.getId();

    EventAdditionalInformationProcessService svc = new EventAdditionalInformationProcessService();
    EventAdditionalInformationFormData formData = new EventAdditionalInformationFormData();
    formData.getEvent().setValue(event.getId().getId());
    formData.getAdditionalInformation().setValue(additionalInformation.getId().getId());
    svc.create(formData);

    eventAdditionalInformation = JPA.find(RtEventAdditionalInformation.class, key);
    assertNotNull("created", eventAdditionalInformation);
  }

  @Test
  public void testLoadConfiguration1() throws Exception {
    EventAdditionalInformationProcessService svc = new EventAdditionalInformationProcessService();
    Map<Long, long[]> result = svc.loadEventAdditionalInformationConfiguration();
    assertNotNull(result);
  }

  @Test
  public void testLoadConfiguration2() throws Exception {
    createTestData();
    EventAdditionalInformationProcessService svc = new EventAdditionalInformationProcessService();
    Map<Long, long[]> result = svc.loadEventAdditionalInformationConfiguration();
    long[] eventNrs = result.get(eventAdditionalInformation.getId().getAdditionalInformationUid());
    assertNotNull("event config", eventNrs);
    assertEquals("event config size", 1, eventNrs.length);
    assertEquals("event", event.getId().getId().longValue(), eventNrs[0]);
  }

  private void createTestData() throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    additionalInformation = new RtUc();
    additionalInformation.setCodeType(AdditionalInformationCodeType.ID);
    additionalInformation.setActive(true);
    additionalInformation.setId(RtUcKey.create((Long) null));
    JPA.merge(additionalInformation);

    additionalInformationDef = new RtAdditionalInformationDef();
    RtAdditionalInformationDefKey key = new RtAdditionalInformationDefKey();
    key.setId(additionalInformation.getId().getId());
    key.setClientNr(ServerSession.get().getSessionClientNr());
    additionalInformationDef.setId(key);
    JPA.merge(additionalInformationDef);

    eventAdditionalInformation = new RtEventAdditionalInformation();
    RtEventAdditionalInformationKey key2 = new RtEventAdditionalInformationKey();
    key2.setAdditionalInformationUid(additionalInformation.getId().getId());
    key2.setEventNr(event.getId().getId());
    key2.setClientNr(ServerSession.get().getSessionClientNr());
    eventAdditionalInformation.setId(key2);
    JPA.merge(eventAdditionalInformation);
  }

  @After
  public void after() throws ProcessingException {
    if (eventAdditionalInformation != null) {
      JPA.remove(eventAdditionalInformation);
    }
    if (additionalInformation != null) {
      JPA.remove(additionalInformation);
    }
    if (additionalInformationDef != null) {
      JPA.remove(additionalInformationDef);
    }
    if (event != null) {
      JPA.remove(event);
    }
  }

}
