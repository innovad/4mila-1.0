package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateFeePermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public UpdateFeePermission() {
  super("UpdateFee");
  }
}
