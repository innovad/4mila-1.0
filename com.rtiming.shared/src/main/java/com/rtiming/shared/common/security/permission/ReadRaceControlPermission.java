package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadRaceControlPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadRaceControlPermission() {
  super("ReadRaceControl");
  }
}
