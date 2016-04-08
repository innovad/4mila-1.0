package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadPunchPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadPunchPermission() {
  super("ReadPunch");
  }
}
