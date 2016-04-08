package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadUserPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadUserPermission() {
  super("ReadUser");
  }
}
