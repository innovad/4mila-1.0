package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateFeePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateFeePermission() {
  super("CreateFee");
  }
}
