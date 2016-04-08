package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadPaymentPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadPaymentPermission() {
  super("ReadPayment");
  }
}
