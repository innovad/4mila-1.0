package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateAgePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateAgePermission() {
  super("CreateAge");
  }
}
