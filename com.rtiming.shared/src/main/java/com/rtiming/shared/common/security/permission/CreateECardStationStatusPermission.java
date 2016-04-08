package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateECardStationStatusPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateECardStationStatusPermission() {
  super("CreateECardStationStatus");
  }
}
