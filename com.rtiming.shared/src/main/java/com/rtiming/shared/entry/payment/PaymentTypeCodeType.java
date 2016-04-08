package com.rtiming.shared.entry.payment;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;

public class PaymentTypeCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1900L;

  public PaymentTypeCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.PAYMENT;
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("PaymentType");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public class CashPaymentCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1901L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("CashPayment");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
