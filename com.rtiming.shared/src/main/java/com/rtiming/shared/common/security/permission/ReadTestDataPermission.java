package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadTestDataPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadTestDataPermission() {
  super("ReadTestData");
  }
}
