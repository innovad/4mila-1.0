package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadClubPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadClubPermission() {
  super("ReadClub");
  }
}
