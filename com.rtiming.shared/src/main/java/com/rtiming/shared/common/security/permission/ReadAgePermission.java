package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadAgePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadAgePermission() {
  super("ReadAge");
  }
}
