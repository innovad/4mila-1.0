package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateDefaultPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateDefaultPermission() {
  super("CreateDefault");
  }
}
