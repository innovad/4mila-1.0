package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdatePaymentPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdatePaymentPermission() {
  super("UpdatePayment");
  }
}
