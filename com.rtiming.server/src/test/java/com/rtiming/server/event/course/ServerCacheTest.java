package com.rtiming.server.event.course;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.entry.SharedCache;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class ServerCacheTest {

  // Server Caches
  @Test
  public void testGetReplacementControls() throws Exception {
    ServerCache.getReplacementControls(null);
  }

  // Shared Caches
  @Test
  public void testGetAddInfoForEntity() throws Exception {
    List<AdditionalInformationValueBean> result = SharedCache.getAddInfoForEntity(null, null);
    assertNotNull("not null", result);
    assertEquals("empty", 0, result.size());
  }

}
