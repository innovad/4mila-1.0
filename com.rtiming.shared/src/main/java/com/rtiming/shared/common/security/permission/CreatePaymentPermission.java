package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreatePaymentPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreatePaymentPermission() {
  super("CreatePayment");
  }
}
