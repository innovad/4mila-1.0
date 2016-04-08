package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateCodePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateCodePermission() {
  super("UpdateCode");
  }
}
