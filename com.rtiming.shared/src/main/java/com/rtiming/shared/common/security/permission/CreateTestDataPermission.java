package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateTestDataPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateTestDataPermission() {
  super("CreateTestData");
  }
}
