package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadECardStationStatusPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadECardStationStatusPermission() {
  super("ReadECardStationStatus");
  }
}
