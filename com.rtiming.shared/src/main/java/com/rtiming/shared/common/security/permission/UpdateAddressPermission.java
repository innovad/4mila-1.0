package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class UpdateAddressPermission extends BasicPermission{

  private static final long serialVersionUID = 0L;

  public UpdateAddressPermission() {
  super("UpdateAddress");
  }
}
