package com.rtiming.server.entry;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;

/**
 * @author amo
 */
@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class SharedCacheServerUtilityTest {

  @Test
  public void testNotifyClient() throws Exception {
    SharedCacheServerUtility.notifyClients();
  }

}
