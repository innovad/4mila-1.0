package com.rtiming.server.entry.payment;

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
import com.rtiming.shared.dao.RtRegistration;
import com.rtiming.shared.dao.RtRegistrationKey;
import com.rtiming.shared.entry.RegistrationFormData;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class RegistrationProcessServiceTest {

  @Test
  public void testStore() throws ProcessingException {
    RegistrationProcessService svc = new RegistrationProcessService();
    RegistrationFormData formData = new RegistrationFormData();
    formData = svc.prepareCreate(formData);
    formData = svc.create(formData);
    formData = svc.load(formData);
    formData.getStartlistSettingOptionGroupBox().setValue(StartlistSettingOptionCodeType.SplitRegistrationsCode.ID);
    svc.store(formData);
    formData = svc.load(formData);
    assertEquals(StartlistSettingOptionCodeType.SplitRegistrationsCode.ID, formData.getStartlistSettingOptionGroupBox().getValue());
    svc.delete(formData);
  }

  @Test
  public void testFind() throws ProcessingException {
    RegistrationProcessService svc = new RegistrationProcessService();
    RegistrationFormData formData = svc.find("abc" + System.currentTimeMillis());
    Assert.assertNull("not found", formData.getRegistrationNr());
  }

  @Test
  public void testDelete() throws ProcessingException {
    RtRegistration registration = new RtRegistration();
    registration.setId(RtRegistrationKey.create((Long) null));
    JPA.merge(registration);

    RegistrationProcessService svc = new RegistrationProcessService();
    RegistrationFormData formData = new RegistrationFormData();
    formData.setRegistrationNr(registration.getId().getId());
    svc.delete(formData);

    RtRegistration result = JPA.find(RtRegistration.class, RtRegistrationKey.create(registration.getId().getId()));
    Assert.assertNull("deleted", result);
  }

}
