package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateRaceControlPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateRaceControlPermission() {
  super("UpdateRaceControl");
  }
}
