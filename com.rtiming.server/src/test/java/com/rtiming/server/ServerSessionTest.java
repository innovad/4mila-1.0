package com.rtiming.server;

import static org.junit.Assert.assertNotNull;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class ServerSessionTest {
  private ServerSession m_serverSession;

  @Before
  public void before() throws Exception {
    m_serverSession = ServerSession.get();
  }

  @Test
  public void testInstance() throws Exception {
    assertNotNull(m_serverSession);
  }
}
