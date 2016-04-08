package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateRunnerPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateRunnerPermission() {
  super("CreateRunner");
  }
}
