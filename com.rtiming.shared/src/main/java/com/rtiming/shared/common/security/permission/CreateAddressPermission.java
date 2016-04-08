package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateAddressPermission extends BasicPermission{

  private static final long serialVersionUID = 0L;

  public CreateAddressPermission() {
  super("CreateAddress");
  }
}
