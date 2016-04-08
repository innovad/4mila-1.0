package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadCodePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadCodePermission() {
  super("ReadCode");
  }
}
