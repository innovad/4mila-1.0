package com.rtiming.server.runner;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.common.database.sql.AddressBean;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class AddressProcessServiceTest {

  @Test
  public void testLoad() throws ProcessingException {
    AddressProcessService svc = new AddressProcessService();
    AddressBean bean = new AddressBean();
    svc.load(bean);
  }

  @Test
  public void testStore1() throws ProcessingException {
    AddressProcessService svc = new AddressProcessService();
    AddressBean bean = new AddressBean();
    svc.store(bean);
  }

  @Test
  public void testStore2() throws ProcessingException {
    AddressProcessService svc = new AddressProcessService();
    svc.store(null);
  }

}
