package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadControlPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadControlPermission() {
  super("ReadControl");
  }
}
