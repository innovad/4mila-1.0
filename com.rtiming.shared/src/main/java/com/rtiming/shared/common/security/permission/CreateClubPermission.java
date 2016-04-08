package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateClubPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateClubPermission() {
  super("CreateClub");
  }
}
