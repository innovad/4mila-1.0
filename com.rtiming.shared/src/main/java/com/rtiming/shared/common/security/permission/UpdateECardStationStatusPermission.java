package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateECardStationStatusPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateECardStationStatusPermission() {
  super("UpdateECardStationStatus");
  }
}
