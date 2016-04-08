package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateCityPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateCityPermission() {
  super("UpdateCity");
  }
}
