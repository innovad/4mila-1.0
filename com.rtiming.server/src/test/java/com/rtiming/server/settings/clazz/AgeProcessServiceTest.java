package com.rtiming.server.settings.clazz;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.junit.Test;

import com.rtiming.shared.dao.RtClassAge;

/**
 * @author amo
 */
public class AgeProcessServiceTest {

  @Test(expected = VetoException.class)
  public void testStore() throws Exception {
    AgeProcessService svc = new AgeProcessService();
    RtClassAge bean = new RtClassAge();
    svc.storeInternal(bean);
  }

}
