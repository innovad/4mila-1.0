package com.rtiming.server.entry.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.common.database.sql.PaymentBean;
import com.rtiming.shared.dao.RtPayment;
import com.rtiming.shared.dao.RtPaymentKey;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class PaymentProcessServiceTest {

  @Test
  public void testStore() throws Exception {
    RtPayment payment = new RtPayment();
    payment.setId(RtPaymentKey.create(new RtPaymentKey()));
    JPA.merge(payment);

    PaymentProcessService svc = new PaymentProcessService();
    PaymentBean bean = new PaymentBean();
    bean.setPaymentNr(payment.getId().getId());
    bean = svc.load(bean);
    bean.setAmount(777d);
    svc.store(bean);

    bean = new PaymentBean();
    bean.setPaymentNr(payment.getId().getId());
    bean = svc.load(bean);
    assertEquals("Value updated", 777d, bean.getAmount(), 0d);

    JPA.remove(payment);
  }

  @Test
  public void testDelete() throws Exception {
    RtPayment payment = new RtPayment();
    payment.setId(RtPaymentKey.create(new RtPaymentKey()));
    JPA.merge(payment);

    PaymentProcessService svc = new PaymentProcessService();
    PaymentBean bean = new PaymentBean();
    bean.setPaymentNr(payment.getId().getId());
    svc.delete(bean);

    RtPayment result = JPA.find(RtPayment.class, payment.getId());
    assertNull("deleted", result);
  }

}
