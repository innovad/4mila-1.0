package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateCurrencyPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateCurrencyPermission() {
  super("UpdateCurrency");
  }
}
