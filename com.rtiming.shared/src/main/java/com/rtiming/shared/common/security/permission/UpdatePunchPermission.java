package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdatePunchPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdatePunchPermission() {
  super("UpdatePunch");
  }
}
