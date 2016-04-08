package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadRacePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadRacePermission() {
  super("ReadRace");
  }
}
