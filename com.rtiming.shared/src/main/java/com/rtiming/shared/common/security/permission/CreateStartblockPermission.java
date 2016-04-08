package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateStartblockPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateStartblockPermission() {
  super("CreateStartblock");
  }
}
