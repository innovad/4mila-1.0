package com.rtiming.server.event.course;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControlKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.event.course.ControlFormData;

import org.junit.Assert;

/**
 * @author amo
 */
@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class ControlProcessServiceTest {

  private RtEvent event;
  private RtControl control;
  private RtControl control2;

  @Test
  public void testDelete1() throws Exception {
    ControlProcessService svc = new ControlProcessService();
    ControlFormData formData = new ControlFormData();
    svc.delete(formData);
  }

  @Test
  public void testDelete2() throws Exception {
    ControlProcessService svc = new ControlProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete3() throws Exception {
    control = new RtControl();
    control.setId(RtControlKey.create((Long) null));
    control.setActive(true);
    JPA.persist(control);

    ControlProcessService svc = new ControlProcessService();
    ControlFormData formData = new ControlFormData();
    formData.setControlNr(control.getId().getId());
    svc.delete(formData);

    control = JPA.find(RtControl.class, control.getId());
    assertNull("deleted", control);
  }

  @Test
  public void testFind1() throws Exception {
    ControlProcessService svc = new ControlProcessService();
    ControlFormData result = svc.find(null, 0L);
    assertNotNull("not null", result);
    assertNull("does not exist", result.getControlNr());
  }

  @Test
  public void testFind2() throws Exception {
    createEventWithControl();

    ControlProcessService svc = new ControlProcessService();
    ControlFormData result = svc.find("12345", event.getId().getId());
    assertNotNull("not null", result);
    assertNotNull("does not exist", result.getControlNr());
    assertEquals("control found", control.getId().getId(), result.getControlNr());
  }

  private void createEventWithControl() throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.persist(event);

    control = new RtControl();
    control.setId(RtControlKey.create((Long) null));
    control.setEventNr(event.getId().getId());
    control.setActive(true);
    control.setControlNo("12345");
    JPA.persist(control);
  }

  @Test(expected = VetoException.class)
  public void testStore1() throws Exception {
    ControlProcessService svc = new ControlProcessService();
    ControlFormData formData = new ControlFormData();
    svc.store(formData);
  }

  @Test(expected = VetoException.class)
  public void testStore2() throws Exception {
    createEventWithControl();
    ControlProcessService svc = new ControlProcessService();
    ControlFormData formData = new ControlFormData();
    formData = svc.prepareCreate(formData);
    formData.getNumber().setValue("12345");
    formData.getEvent().setValue(event.getId().getId());
    formData = svc.create(formData);
  }

  @Test
  public void testStore3() throws Exception {
    createEventWithControl();
    ControlProcessService svc = new ControlProcessService();
    ControlFormData formData = new ControlFormData();
    formData.setControlNr(control.getId().getId());
    formData = svc.load(formData);
    formData.getGlobalY().setValue(5.33d);
    svc.store(formData);

    RtControl find = JPA.find(RtControl.class, control.getId());
    assertEquals("updated", 5.33d, find.getGlobaly(), 0.01d);
  }

  @Test
  public void testStore4() throws Exception {
    createEventWithControl();
    ControlProcessService svc = new ControlProcessService();
    ControlFormData formData = new ControlFormData();
    formData.setControlNr(control.getId().getId());
    formData = svc.load(formData);
    formData.getActive().setValue(null);
    svc.store(formData);

    RtControl find = JPA.find(RtControl.class, control.getId());
    assertFalse("updated", find.getActive());
  }

  @Test
  public void testPrepareCreate1() throws Exception {
    ControlProcessService svc = new ControlProcessService();
    ControlFormData formData = new ControlFormData();
    formData = svc.prepareCreate(formData);
    Assert.assertEquals("first control", "31", formData.getNumber().getValue());
  }

  @Test
  public void testPrepareCreate2() throws Exception {
    createEventWithControl();
    ControlProcessService svc = new ControlProcessService();
    ControlFormData formData = new ControlFormData();
    formData.getEvent().setValue(event.getId().getId());
    formData = svc.prepareCreate(formData);
    Assert.assertEquals("first control", "12346", formData.getNumber().getValue());
  }

  @Test
  public void testPrepareCreate3() throws Exception {
    createEventWithControl();

    control2 = new RtControl();
    control2 = new RtControl();
    control2.setId(RtControlKey.create((Long) null));
    control2.setEventNr(event.getId().getId());
    control2.setActive(true);
    control2.setControlNo("S");
    JPA.persist(control2);

    ControlProcessService svc = new ControlProcessService();
    ControlFormData formData = new ControlFormData();
    formData.getEvent().setValue(event.getId().getId());
    formData = svc.prepareCreate(formData);
    Assert.assertEquals("first control", "12346", formData.getNumber().getValue());
  }

  @Test
  public void testCalculateNextControlNo1() throws Exception {
    ControlProcessService svc = new ControlProcessService();
    String result = svc.calculateNextControlNo(null);
    Assert.assertEquals("control no", FMilaUtility.FIRST_CONTROL_NO, result);
  }

  @Test
  public void testCalculateNextControlNo2() throws Exception {
    ControlProcessService svc = new ControlProcessService();
    String result = svc.calculateNextControlNo(new ArrayList<String>());
    Assert.assertEquals("control no", FMilaUtility.FIRST_CONTROL_NO, result);
  }

  @Test
  public void testCalculateNextControlNo3() throws Exception {
    ControlProcessService svc = new ControlProcessService();
    ArrayList<String> list = new ArrayList<String>();
    list.add("S");
    String result = svc.calculateNextControlNo(list);
    Assert.assertEquals("control no", FMilaUtility.FIRST_CONTROL_NO, result);
  }

  @Test
  public void testCalculateNextControlNo4() throws Exception {
    ControlProcessService svc = new ControlProcessService();
    ArrayList<String> list = new ArrayList<String>();
    list.add("50");
    String result = svc.calculateNextControlNo(list);
    Assert.assertEquals("control no", "51", result);
  }

  @Test
  public void testCalculateNextControlNo5() throws Exception {
    ControlProcessService svc = new ControlProcessService();
    ArrayList<String> list = new ArrayList<String>();
    list.add("11");
    String result = svc.calculateNextControlNo(list);
    Assert.assertEquals("control no", "12", result);
  }

  @Test
  public void testCalculateNextControlNo6() throws Exception {
    ControlProcessService svc = new ControlProcessService();
    ArrayList<String> list = new ArrayList<String>();
    list.add("30");
    list.add("77");
    list.add("111");
    list.add("S");
    list.add("50");
    String result = svc.calculateNextControlNo(list);
    Assert.assertEquals("control no", "112", result);
  }

  @After
  public void after() throws ProcessingException {
    if (control != null && JPA.find(RtControl.class, control.getId()) != null) {
      JPA.remove(control);
    }
    if (control2 != null && JPA.find(RtControl.class, control2.getId()) != null) {
      JPA.remove(control2);
    }
    JPA.remove(event);
  }

}
