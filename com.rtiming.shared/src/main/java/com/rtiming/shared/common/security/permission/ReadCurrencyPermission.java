package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadCurrencyPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadCurrencyPermission() {
  super("ReadCurrency");
  }
}
