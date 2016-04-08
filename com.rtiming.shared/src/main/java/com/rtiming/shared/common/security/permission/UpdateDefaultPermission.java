package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateDefaultPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateDefaultPermission() {
  super("UpdateDefault");
  }
}
