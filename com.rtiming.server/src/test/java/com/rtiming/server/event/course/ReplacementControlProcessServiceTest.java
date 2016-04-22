package com.rtiming.server.event.course;

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
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControlKey;
import com.rtiming.shared.dao.RtControlReplacement;
import com.rtiming.shared.dao.RtControlReplacementKey;
import com.rtiming.shared.event.course.ReplacementControlFormData;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class ReplacementControlProcessServiceTest {

  private RtControlReplacement replacement;
  private RtControl control;

  @Test
  public void testDelete1() throws ProcessingException {
    ReplacementControlProcessService svc = new ReplacementControlProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    ReplacementControlProcessService svc = new ReplacementControlProcessService();
    svc.delete(new ReplacementControlFormData());
  }

  @Test
  public void testDelete3() throws ProcessingException {
    control = new RtControl();
    control.setId(RtControlKey.create((Long) null));
    control.setActive(true);
    JPA.merge(control);

    replacement = new RtControlReplacement();
    RtControlReplacementKey key5 = new RtControlReplacementKey();
    key5.setControlNr(control.getId().getId());
    key5.setReplacementControlNr(control.getId().getId());
    key5.setClientNr(ServerSession.get().getSessionClientNr());
    replacement.setId(key5);
    JPA.merge(replacement);

    ReplacementControlProcessService svc = new ReplacementControlProcessService();
    ReplacementControlFormData formData = new ReplacementControlFormData();
    formData.getControl().setValue(replacement.getId().getControlNr());
    formData.getReplacementControl().setValue(replacement.getId().getReplacementControlNr());
    svc.delete(formData);

    RtControlReplacement find = JPA.find(RtControlReplacement.class, replacement.getId());
    Assert.assertNull("deleted", find);
  }

  @Test
  public void testCreate() throws Exception {
    control = new RtControl();
    control.setId(RtControlKey.create((Long) null));
    control.setActive(true);
    JPA.merge(control);

    ReplacementControlProcessService svc = new ReplacementControlProcessService();
    ReplacementControlFormData formData = new ReplacementControlFormData();
    formData = svc.prepareCreate(formData);
    formData.getControl().setValue(control.getId().getId());
    formData.getReplacementControl().setValue(control.getId().getId());
    formData = svc.create(formData);

    RtControlReplacementKey key = new RtControlReplacementKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setControlNr(control.getId().getId());
    key.setReplacementControlNr(control.getId().getId());
    RtControlReplacement find = JPA.find(RtControlReplacement.class, key);
    Assert.assertNotNull("created", find);
    JPA.remove(find);
  }

  @After
  public void after() throws ProcessingException {
    if (replacement != null) {
      JPA.remove(replacement);
    }
    if (control != null) {
      JPA.remove(control);
    }
  }

}
