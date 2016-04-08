package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateFeeGroupPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateFeeGroupPermission() {
  super("CreateFeeGroup");
  }
}
