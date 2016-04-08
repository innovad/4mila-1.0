package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateAgePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateAgePermission() {
  super("UpdateAge");
  }
}
