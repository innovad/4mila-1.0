package com.rtiming.client.dao;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.shared.dao.RtClassAgeKey;
import com.rtiming.shared.dao.util.IKey;

/**
 * @author amo
 */
@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class RtClassAgeKeyTest {

  @Test
  public void testCreate1() throws Exception {
    IKey pk = RtClassAgeKey.create(5L);
    Assert.assertNotNull("Not null", pk);
    Assert.assertEquals("Id", 5, pk.getId().longValue());
    Assert.assertEquals("ClientNr", ClientSession.get().getSessionClientNr(), pk.getClientNr());
  }

  @Test
  public void testCreate2() throws Exception {
    IKey pk = RtClassAgeKey.create(5L);
    Assert.assertNotNull("Not null", pk);
    Assert.assertEquals("Id", 5, pk.getId().longValue());
    Assert.assertEquals("ClientNr", ClientSession.get().getSessionClientNr(), pk.getClientNr());
  }

  @Test
  public void testCreate3() throws Exception {
    IKey pk = RtClassAgeKey.create((Long) null);
    Assert.assertNotNull("Not null", pk);
    // Client-side no Id is created
    Assert.assertNull("Not null", pk.getId());
    Assert.assertEquals("ClientNr", ClientSession.get().getSessionClientNr(), pk.getClientNr());
  }

}
