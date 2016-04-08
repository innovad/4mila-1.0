package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateECardPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateECardPermission() {
  super("UpdateECard");
  }
}
