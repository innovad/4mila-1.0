package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateTestDataPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateTestDataPermission() {
  super("UpdateTestData");
  }
}
