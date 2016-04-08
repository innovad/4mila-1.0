package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadECardStationPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadECardStationPermission() {
  super("ReadECardStation");
  }
}
