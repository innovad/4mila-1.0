package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateCityPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateCityPermission() {
  super("CreateCity");
  }
}
