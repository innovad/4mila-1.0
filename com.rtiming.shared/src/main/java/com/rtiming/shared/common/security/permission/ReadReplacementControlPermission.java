package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadReplacementControlPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadReplacementControlPermission() {
  super("ReadReplacementControl");
  }
}
