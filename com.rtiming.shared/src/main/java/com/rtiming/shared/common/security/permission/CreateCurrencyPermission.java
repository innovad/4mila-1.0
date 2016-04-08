package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateCurrencyPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateCurrencyPermission() {
  super("CreateCurrency");
  }
}
