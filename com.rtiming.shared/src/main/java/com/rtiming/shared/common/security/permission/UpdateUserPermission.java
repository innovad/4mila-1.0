package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateUserPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateUserPermission() {
  super("UpdateUser");
  }
}
