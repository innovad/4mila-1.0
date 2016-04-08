package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateRunnerPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateRunnerPermission() {
  super("UpdateRunner");
  }
}
