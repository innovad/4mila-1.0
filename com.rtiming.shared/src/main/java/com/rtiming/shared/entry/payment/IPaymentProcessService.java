package com.rtiming.shared.entry.payment;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.common.database.sql.PaymentBean;

@TunnelToServer
public interface IPaymentProcessService extends IService {

  PaymentBean prepareCreate(PaymentBean bean) throws ProcessingException;

  PaymentBean create(PaymentBean bean) throws ProcessingException;

  PaymentBean load(PaymentBean bean) throws ProcessingException;

  PaymentBean store(PaymentBean bean) throws ProcessingException;

  PaymentBean delete(PaymentBean bean) throws ProcessingException;
}
