package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class ReadAddressPermission extends BasicPermission{

  private static final long serialVersionUID = 0L;

  public ReadAddressPermission() {
  super("ReadAddress");
  }
}
