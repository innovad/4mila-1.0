package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreatePunchPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreatePunchPermission() {
  super("CreatePunch");
  }
}
