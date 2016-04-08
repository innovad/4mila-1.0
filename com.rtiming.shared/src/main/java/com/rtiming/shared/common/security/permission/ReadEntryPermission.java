package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadEntryPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public ReadEntryPermission() {
    super("ReadEntry");
  }
}
