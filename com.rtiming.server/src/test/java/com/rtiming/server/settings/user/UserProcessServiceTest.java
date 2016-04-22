package com.rtiming.server.settings.user;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.settings.user.RoleCodeType;
import com.rtiming.shared.settings.user.UserFormData;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class UserProcessServiceTest {

  @Test
  public void testLoad() throws Exception {
    UserProcessService svc = new UserProcessService();
    UserFormData formData = new UserFormData();
    svc.load(formData);
  }

  @Test
  public void testStore() throws ProcessingException {
    UserProcessService svc = new UserProcessService();
    UserFormData formData = new UserFormData();
    formData = svc.prepareCreate(formData);
    formData.getUsername().setValue("u" + System.currentTimeMillis());
    formData.getRoles().setValue(new HashSet<Long>(Arrays.asList(new Long[]{RoleCodeType.AdministratorCode.ID})));
    formData = svc.create(formData);
    svc.store(formData);
  }

  @Test(expected = VetoException.class)
  public void testStoreUnique() throws ProcessingException {
    String username = "u" + System.currentTimeMillis();

    UserProcessService svc = new UserProcessService();
    UserFormData formData = new UserFormData();
    formData = svc.prepareCreate(formData);
    formData.getUsername().setValue(username);
    formData.getRoles().setValue(new HashSet<Long>(Arrays.asList(new Long[]{RoleCodeType.AdministratorCode.ID})));
    formData = svc.create(formData);

    formData = new UserFormData();
    formData = svc.prepareCreate(formData);
    formData.getUsername().setValue(username);
    formData.getRoles().setValue(new HashSet<Long>(Arrays.asList(new Long[]{RoleCodeType.AdministratorCode.ID})));
    formData = svc.create(formData);
  }

  @Test
  public void testFind1() throws ProcessingException {
    UserProcessService svc = new UserProcessService();
    UserFormData user = svc.find("u" + System.currentTimeMillis());
    assertNull(user.getUserNr());
  }

  @Test
  public void testFind2() throws ProcessingException {
    String username = "u" + System.currentTimeMillis();

    UserProcessService svc = new UserProcessService();
    UserFormData formData = new UserFormData();
    formData = svc.prepareCreate(formData);
    formData.getUsername().setValue(username);
    formData.getRoles().setValue(new HashSet<Long>(Arrays.asList(new Long[]{RoleCodeType.AdministratorCode.ID})));
    formData = svc.create(formData);

    UserFormData user = svc.find(username);
    assertNotNull(user.getUserNr());
  }

}
