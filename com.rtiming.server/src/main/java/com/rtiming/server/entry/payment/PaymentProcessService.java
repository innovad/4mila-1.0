package com.rtiming.server.entry.payment;

import java.util.Date;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.PaymentBean;
import com.rtiming.shared.common.security.permission.CreatePaymentPermission;
import com.rtiming.shared.common.security.permission.ReadPaymentPermission;
import com.rtiming.shared.common.security.permission.UpdatePaymentPermission;
import com.rtiming.shared.dao.RtPayment;
import com.rtiming.shared.dao.RtPaymentKey;
import com.rtiming.shared.entry.payment.IPaymentProcessService;

public class PaymentProcessService  implements IPaymentProcessService {

  @Override
  public PaymentBean prepareCreate(PaymentBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreatePaymentPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    bean.setEvtPayment(new Date());

    return bean;
  }

  @Override
  public PaymentBean create(PaymentBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreatePaymentPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtPaymentKey key = RtPaymentKey.create((Long) null);
    RtPayment payment = new RtPayment();
    payment.setId(key);
    JPA.persist(payment);

    bean.setPaymentNr(payment.getId().getId());
    bean = store(bean);

    return bean;
  }

  @Override
  public PaymentBean load(PaymentBean bean) throws ProcessingException {
    if (!ACCESS.check(new ReadPaymentPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT P.paymentNo, " +
        "P.registrationNr, " +
        "P.evtPayment, " +
        "P.amount, " +
        "P.currencyUid, " +
        "P.typeUid " +
        "FROM RtPayment P " +
        "WHERE P.id.paymentNr = :paymentNr " +
        "AND P.id.clientNr = :sessionClientNr " +
        "INTO :paymentNo, " +
        ":registrationNr, " +
        ":evtPayment, " +
        ":amount, " +
        ":currencyUid, " +
        ":typeUid ", bean);

    return bean;
  }

  @Override
  public PaymentBean store(PaymentBean bean) throws ProcessingException {
    if (!ACCESS.check(new UpdatePaymentPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    String queryString = "UPDATE RtPayment " +
        "SET paymentNo = :paymentNo, " +
        "rtRegistration.id.registrationNr = :registrationNr, " +
        "evtPayment = :evtPayment, " +
        "amount = :amount, " +
        "currencyUid = :currencyUid, " +
        "typeUid = :typeUid " +
        "WHERE id.paymentNr = :paymentNr " +
        "AND id.clientNr = :sessionClientNr ";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, bean);
    query.executeUpdate();

    return bean;
  }

  @Override
  public PaymentBean delete(PaymentBean bean) throws ProcessingException {

    String queryString = "DELETE FROM RtPayment WHERE id.paymentNr = :paymentNr AND id.clientNr = :sessionClientNr";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, bean);
    query.executeUpdate();

    return bean;
  }
}
