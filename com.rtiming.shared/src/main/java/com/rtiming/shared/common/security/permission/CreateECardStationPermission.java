package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateECardStationPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateECardStationPermission() {
  super("CreateECardStation");
  }
}
