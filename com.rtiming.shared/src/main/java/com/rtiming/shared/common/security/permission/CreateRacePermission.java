package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateRacePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateRacePermission() {
  super("CreateRace");
  }
}
