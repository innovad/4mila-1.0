package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateECardPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateECardPermission() {
  super("CreateECard");
  }
}
