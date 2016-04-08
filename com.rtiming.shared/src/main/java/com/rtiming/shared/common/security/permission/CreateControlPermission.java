package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateControlPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateControlPermission() {
  super("CreateControl");
  }
}
