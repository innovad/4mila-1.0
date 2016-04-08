package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateReplacementControlPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateReplacementControlPermission() {
  super("CreateReplacementControl");
  }
}
