package com.rtiming.server.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.common.database.sql.AddressBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dao.RtAddress;
import com.rtiming.shared.dao.RtAddressKey;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunnerKey;
import com.rtiming.shared.ecard.ECardTypeCodeType;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class RunnerProcessServiceTest {

  @Test
  public void testLoad1() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    svc.load(null);
  }

  @Test
  public void testLoad2() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    svc.load(new RunnerBean());
  }

  @Test
  public void testLoad3() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();

    RtRunner runner = new RtRunner();
    runner.setId(RtRunnerKey.create((Long) null));
    runner.setActive(true);
    runner.setExtKey("A" + System.currentTimeMillis());
    JPA.merge(runner);

    RunnerBean bean = new RunnerBean();
    bean.setRunnerNr(runner.getId().getId());
    bean.setClientNr(runner.getId().getClientNr());
    bean = svc.load(bean);
    Assert.assertEquals("load", runner.getExtKey(), bean.getExtKey());

    JPA.remove(runner);
  }

  @Test
  public void testDelete() throws ProcessingException {
    RtRunner runner = new RtRunner();
    runner.setId(RtRunnerKey.create((Long) null));
    runner.setActive(true);
    JPA.merge(runner);

    RunnerProcessService svc = new RunnerProcessService();
    RunnerBean bean = new RunnerBean();
    bean.setRunnerNr(runner.getId().getId());
    svc.delete(bean);

    RtRunner find = JPA.find(RtRunner.class, runner.getId());
    assertNull("deleted", find);

    JPA.remove(runner);
  }

  @Test
  public void testFindRunner1() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    Long result = svc.findRunner(null);
    assertNull(result);
  }

  @Test
  public void testFindRunner2() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    Long result = svc.findRunner("");
    assertNull(result);
  }

  @Test
  public void testFindRunner3() throws Exception {
    String extKey = "TEST" + System.currentTimeMillis();

    RtRunner runner = new RtRunner();
    runner.setId(RtRunnerKey.create((Long) null));
    runner.setActive(true);
    runner.setExtKey(extKey);
    JPA.merge(runner);

    RunnerProcessService svc = new RunnerProcessService();
    Long result = svc.findRunner(extKey);
    assertNotNull(result);
    Assert.assertEquals("runner found", runner.getId().getId(), result);

    JPA.remove(runner);
  }

  @Test
  public void testFindRunner4() throws Exception {
    String extKey = "TEST" + System.currentTimeMillis();
    RunnerProcessService svc = new RunnerProcessService();
    Long result = svc.findRunner(extKey);
    assertNull(result);
  }

  @Test
  public void testFindRunnerByECard1() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    Long result = svc.findRunnerByECard(null);
    assertNull(result);
  }

  @Test
  public void testFindRunnerByECard2() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    Long result = svc.findRunnerByECard("");
    assertNull(result);
  }

  @Test
  public void testFindRunnerByECard3() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    Long result = svc.findRunnerByECard("TEST" + System.currentTimeMillis());
    assertNull(result);
  }

  @Test
  public void testFindRunnerByECard4() throws Exception {
    String eCardNo = "TEST" + System.currentTimeMillis();

    RtEcard ecard = new RtEcard();
    ecard.setKey(RtEcardKey.create((Long) null));
    ecard.setEcardNo(eCardNo);
    ecard.setTypeUid(ECardTypeCodeType.SICard10Code.ID);
    JPA.merge(ecard);

    RtRunner runner = new RtRunner();
    runner.setId(RtRunnerKey.create((Long) null));
    runner.setActive(true);
    runner.setEcardNr(ecard.getKey().getId());
    JPA.merge(runner);

    RunnerProcessService svc = new RunnerProcessService();
    Long result = svc.findRunnerByECard(eCardNo);
    assertNotNull(result);
    Assert.assertEquals("runner found", runner.getId().getId(), result);

    JPA.remove(runner);
    JPA.remove(ecard);
  }

  @Test
  public void testStore1() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    svc.store(new RunnerBean());
  }

  @Test
  public void testStore2() throws Exception {
    RtAddress address = new RtAddress();
    address.setId(RtAddressKey.create((Long) null));
    JPA.merge(address);

    RtRunner runner = new RtRunner();
    runner.setId(RtRunnerKey.create((Long) null));
    runner.setActive(true);
    JPA.merge(runner);

    RunnerProcessService svc = new RunnerProcessService();
    RunnerBean bean = new RunnerBean();
    bean.setRunnerNr(runner.getId().getId());
    bean = svc.load(bean);
    AddressBean addressBean = new AddressBean();
    addressBean.setAddressNr(address.getId().getId());
    bean.setAddress(addressBean);
    bean.setLastName("Müller");
    svc.store(bean);

    RtRunner find = JPA.find(RtRunner.class, runner.getId());
    assertEquals("updated", address.getId().getId(), find.getAddressNr());
    assertEquals("updated", "Müller", find.getLastName());

    JPA.remove(runner);
    JPA.remove(address);
  }

  @Test
  public void testUpdateOnlineAccountRunnerLinks1() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    svc.updateOnlineAccountRunnerLinks(0L, null, 0L);
  }

  @Test
  public void testUpdateOnlineAccountRunnerLinks2() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    svc.updateOnlineAccountRunnerLinks(null, ServerSession.get().getSessionClientNr(), 0L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateOnlineAccountRunnerLinks3() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    svc.updateOnlineAccountRunnerLinks(null, null, 0L);
  }

  @Test
  public void testUpdateOnlineAccountRunnerLinks4() throws Exception {
    RunnerProcessService svc = new RunnerProcessService();
    svc.updateOnlineAccountRunnerLinks(null, ServerSession.get().getSessionClientNr(), null);
  }

}
