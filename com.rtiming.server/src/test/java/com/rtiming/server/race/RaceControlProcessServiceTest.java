package com.rtiming.server.race;

import static org.junit.Assert.assertEquals;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtRaceControl;
import com.rtiming.shared.dao.RtRaceControlKey;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.race.RaceControlFormData;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class RaceControlProcessServiceTest {

  @Test
  public void testDelete1() throws ProcessingException {
    RaceControlProcessService svc = new RaceControlProcessService();
    svc.delete();
  }

  @Test
  public void testDelete2() throws ProcessingException {
    RtRaceControl control = new RtRaceControl();
    control.setId(RtRaceControlKey.create((Long) null));
    control.setManualStatus(true);
    JPA.merge(control);

    RaceControlProcessService svc = new RaceControlProcessService();
    svc.delete(control.getId().getId());

    RtRaceControl find = JPA.find(RtRaceControl.class, control.getId());
    Assert.assertNull("deleted", find);
  }

  @Test
  public void testStore1() throws Exception {
    RaceControlProcessService svc = new RaceControlProcessService();
    RaceControlFormData formData = new RaceControlFormData();
    svc.store(formData);
  }

  @Test
  public void testStore2() throws Exception {
    RtRaceControl control = new RtRaceControl();
    control.setId(RtRaceControlKey.create((Long) null));
    control.setManualStatus(true);
    JPA.merge(control);

    RaceControlProcessService svc = new RaceControlProcessService();
    RaceControlFormData formData = new RaceControlFormData();
    formData.setRaceControlNr(control.getId().getId());
    formData = svc.load(formData);
    formData.getControlStatus().setValue(ControlStatusCodeType.MissingCode.ID);
    formData = svc.store(formData);
    formData = svc.load(formData);

    assertEquals("updated", ControlStatusCodeType.MissingCode.ID, formData.getControlStatus().getValue().longValue());
    JPA.remove(control);
  }

}
