package com.rtiming.server.club;

import static org.junit.Assert.assertEquals;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.common.database.sql.ClubBean;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class ClubProcessServiceTest {

  @Test
  public void testStore() throws Exception {
    ClubProcessService svc = new ClubProcessService();
    ClubBean bean = new ClubBean();
    bean = svc.create(bean);
    bean.setExtKey("abcdef");
    svc.store(bean);
    bean = svc.load(bean);
    assertEquals("Value stored", "abcdef", bean.getExtKey());
  }

  @Test
  public void testFind() throws Exception {
    ClubProcessService svc = new ClubProcessService();
    svc.findClub("abc");
  }

}
