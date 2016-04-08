package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadCityPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadCityPermission() {
  super("ReadCity");
  }
}
