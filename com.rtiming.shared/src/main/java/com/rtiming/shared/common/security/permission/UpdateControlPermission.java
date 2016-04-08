package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateControlPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateControlPermission() {
  super("UpdateControl");
  }
}
