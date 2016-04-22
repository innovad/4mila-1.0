package com.rtiming.server.common.web;

import static org.junit.Assert.assertNotNull;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.settings.account.AccountFormData;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class WebServiceTest {

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNewSession1() throws ProcessingException {
    WebService svc = new WebService();
    svc.createNewSession(null, null, null);
  }

  @Test
  public void testCreateNewSession2() throws ProcessingException {
    WebService svc = new WebService();
    svc.createNewSession(new AccountFormData(), "", "");
  }

  @Test
  public void testCreateNewSession3() throws ProcessingException {
    WebService svc = new WebService();
    String key = svc.createNewSession(new AccountFormData(), "ip", "safari");
    assertNotNull(key);
  }

  @Test
  public void testGetAccountNrFromSessionKey() throws ProcessingException {
    WebService svc = new WebService();
    svc.getAccountNrFromSessionKey(null);
  }

}
