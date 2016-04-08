package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadRunnerPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadRunnerPermission() {
  super("ReadRunner");
  }
}
