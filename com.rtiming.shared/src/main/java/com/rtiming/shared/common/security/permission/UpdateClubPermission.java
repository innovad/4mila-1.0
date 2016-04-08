package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateClubPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateClubPermission() {
  super("UpdateClub");
  }
}
