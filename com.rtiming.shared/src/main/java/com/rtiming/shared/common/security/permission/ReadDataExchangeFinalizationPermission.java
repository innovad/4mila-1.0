package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadDataExchangeFinalizationPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadDataExchangeFinalizationPermission() {
  super("ReadDataExchangeFinalization");
  }
}
