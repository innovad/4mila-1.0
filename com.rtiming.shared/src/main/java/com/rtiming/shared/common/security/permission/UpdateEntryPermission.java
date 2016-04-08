package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateEntryPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateEntryPermission() {
    super("UpdateEntry");
  }
}
