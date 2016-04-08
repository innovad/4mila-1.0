package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateCodePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateCodePermission() {
  super("CreateCode");
  }
}
