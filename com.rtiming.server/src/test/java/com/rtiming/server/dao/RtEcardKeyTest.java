package com.rtiming.server.dao;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.dao.RtClassAgeKey;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.dao.util.IKey;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class RtEcardKeyTest {

  @Test
  public void testCreate1() throws Exception {
    IKey pk = RtEcardKey.create(new RtEcardKey());
    Assert.assertNotNull("Not null", pk);
    Assert.assertNotNull("Id", pk.getId());
    Assert.assertEquals("ClientNr", ServerSession.get().getSessionClientNr(), pk.getClientNr());
  }

  @Test
  public void testCreate2() throws Exception {
    RtEcardKey key = new RtEcardKey();
    key.setId(888L);
    IKey pk = RtEcardKey.create(key);
    Assert.assertNotNull("Not null", pk);
    Assert.assertNotNull("Id", pk.getId());
    Assert.assertEquals("Id", 888, pk.getId().longValue());
    Assert.assertEquals("ClientNr", ServerSession.get().getSessionClientNr(), pk.getClientNr());
  }

  @Test
  public void testCreate3() throws Exception {
    RtEcardKey key = new RtEcardKey();
    key.setId(888L);
    key.setClientNr(8888L);
    IKey pk = RtEcardKey.create(key);
    Assert.assertNotNull("Not null", pk);
    Assert.assertNotNull("Id", pk.getId());
    Assert.assertEquals("Id", 888, pk.getId().longValue());
    Assert.assertEquals("ClientNr", 8888, pk.getClientNr().longValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate4() throws Exception {
    RtEcardKey.createKeyInternal(null);
  }

  @Test
  public void testCreate5() throws Exception {
    RtEcardKey key = new RtEcardKey();
    RtEcardKey.create(key);
    Assert.assertNotNull(key.getClientNr());
    Assert.assertEquals("ClientNr", ServerSession.get().getSessionClientNr(), key.getClientNr());
  }

  @Test
  public void testCreate6() throws Exception {
    RtEcardKey key = new RtEcardKey();
    key.setClientNr(777L);
    RtEcardKey.create(key);
    Assert.assertNotNull(key.getClientNr());
    Assert.assertEquals("ClientNr", 777, key.getClientNr().longValue());
  }

  @Test
  public void testCreate7() throws Exception {
    IKey pk = RtClassAgeKey.create((RtClassAgeKey) null);
    Assert.assertNotNull("Not null", pk);
    Assert.assertNotNull("Id", pk.getId());
    Assert.assertEquals("ClientNr", ServerSession.get().getSessionClientNr(), pk.getClientNr());
  }

}
